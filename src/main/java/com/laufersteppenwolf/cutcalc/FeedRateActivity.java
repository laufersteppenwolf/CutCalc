package com.laufersteppenwolf.cutcalc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class FeedRateActivity extends Activity {

    public static final String CHANGE_BACKGROUND = "change_background";
    public static final String PVC_DEFAULT = "pvc_default";
    public static final String DEFAULT_FEED = "default_feed";
    public static final String DEFAULT_BLADES_MILLING = "default_milling_blades";
    public static final String DEFAULT_BLADES_TURNING = "default_turning_blades";
    public static final String DEFAULT_BLADES_DRILLING = "default_drilling_blades";

    private Boolean mChangeBackground;
    private Boolean mPvcDefault;

    private String mFeed;
    private String mBladesMilling;
    private String mBladesTurning;
    private String mBladesDrilling;

    public static int mMode = 0;

    private TextView mFeedView;
    private Button buttonCalc;

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void changeMode(int mode) {
        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollViewFeed);
        final TextView mBlades = (TextView) findViewById(R.id.bladesFeed);

        if (mode == 1) {
            mBlades.setText(mBladesDrilling);
            if (mChangeBackground) {
                mScrollView.setBackgroundResource(R.drawable.drill);
            }
        } else if (mode == 2) {
            mBlades.setText(mBladesTurning);
            if (mChangeBackground) {
                mScrollView.setBackgroundResource(R.drawable.turning_chisel);
            }
        } else{
            mBlades.setText(mBladesMilling);
            if (mChangeBackground) {
                mScrollView.setBackgroundResource(R.drawable.milling_cutter);
            }
        }

        mFeedView.setText(mFeed);
        doCalc();
    }

    public void doCalc() {
        buttonCalc.performClick();
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void getPreferences() {
        //Get preferences
        SharedPreferences myPreference= PreferenceManager.getDefaultSharedPreferences(this);
        mChangeBackground = myPreference.getBoolean(CHANGE_BACKGROUND, true);
        mPvcDefault = myPreference.getBoolean(PVC_DEFAULT, false);
        mFeed = myPreference.getString(DEFAULT_FEED, getString(R.string.string_default_feed));
        mBladesMilling = myPreference.getString(DEFAULT_BLADES_MILLING, getString(R.string.string_default_blades_milling));
        mBladesTurning = myPreference.getString(DEFAULT_BLADES_TURNING, getString(R.string.string_default_blades_turning));
        mBladesDrilling = myPreference.getString(DEFAULT_BLADES_DRILLING, getString(R.string.string_default_blades_drilling));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_rate);

        getPreferences();

        final Intent intent = getIntent();

        final Button buttonDrilling = (Button) findViewById(R.id.buttonDrillingFeed);
        final Button buttonMilling = (Button) findViewById(R.id.buttonMillingFeed);
        final Button buttonTurning = (Button) findViewById(R.id.buttonTurningFeed);

        final Switch pvcSwitch = (Switch) findViewById(R.id.switchPvcFeed);

        final TextView mRpm =
                (TextView) findViewById(R.id.rpmFeed);
        mFeedView = (TextView) findViewById(R.id.feed);
        final TextView mBlades =
                (TextView) findViewById(R.id.bladesFeed);
        final TextView mFeedRate =
                (TextView) findViewById(R.id.feedRate);
        final String mRpmRpm =
                intent.getStringExtra("RPM");

        buttonCalc = (Button) findViewById(R.id.buttonCalcFeed);

        mMode = intent.getIntExtra("Mode", 0);

        if (mPvcDefault) {
            pvcSwitch.setChecked(true); // Use PVC data by default
        } else {
            pvcSwitch.setChecked(false); // Don't use PVC data by default
        }

        mRpm.setText(mRpmRpm);       // Use the RPM from the previously calculated data

        changeMode(mMode);

        pvcSwitch.setOnClickListener(
                new Switch.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doCalc();
                    }
                }
        );

        mFeedView.setOnClickListener(
                new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doCalc();
                    }
                }
        );

        mBlades.setOnClickListener(
                new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doCalc();
                    }
                }
        );

        mRpm.setOnClickListener(
                new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doCalc();
                    }
                }
        );

        buttonCalc.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int multiplier = getResources().getInteger(R.integer.multiplier_switch);

                        if (mRpm.getText() != null && !mRpm.getText().toString().equals("")) {
                            if (mFeedView.getText() != null && !mFeedView.getText().toString().equals("")) {
                                if (mBlades.getText() != null && !mBlades.getText().toString().equals("")) {

                                    double rpm = Double.parseDouble(mRpm.getText().toString());
                                    double feed = Double.parseDouble(mFeedView.getText().toString());
                                    double blades = Double.parseDouble(mBlades.getText().toString());

                                    double result = round(rpm * feed * blades, 2);

                                    if (pvcSwitch.isChecked()) {
                                        result = multiplier * result;
                                    }
                                    mFeedRate.setText(Double.toString(result));
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.toast_add_blades),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.toast_add_feed),
                                        Toast.LENGTH_SHORT).show();                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_add_rpm),
                                    Toast.LENGTH_SHORT).show();                        }
                    }
                }
        );
        buttonDrilling.setOnClickListener(
                new Button.OnClickListener()  {
                    @Override
                    public void onClick(View v) {
                        mMode = 1;
                        changeMode(mMode);
                    }
                }
        );
        buttonMilling.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMode = 0;
                        changeMode(mMode);
                    }
                }
        );
        buttonTurning.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMode = 2;
                        changeMode(mMode);
                    }
                }
        );

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getPreferences();
        changeMode(mMode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        boolean installed = appInstalledOrNot("com.laufersteppenwolf.boschdb");
        if(installed) {
            getMenuInflater().inflate(R.menu.menu_main_boschdb, menu);
        }
        getMenuInflater().inflate(R.menu.menu_feed_rate, menu);
        getMenuInflater().inflate(R.menu.menu_feed_rate_features, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(FeedRateActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_features_feedRate:
                MainActivity.mMode = mMode;
                this.finish();
                return true;
            case R.id.action_features_boschdb:
                Intent boschdbIntent = getPackageManager().getLaunchIntentForPackage("com.laufersteppenwolf.boschdb");
                startActivity(boschdbIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
