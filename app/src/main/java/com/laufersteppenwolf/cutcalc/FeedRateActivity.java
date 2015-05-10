package com.laufersteppenwolf.cutcalc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.laufersteppenwolf.cutcalc.colorpicker.ColorActivity.setSwitchColor;
import static com.laufersteppenwolf.cutcalc.colorpicker.ColorActivity.setSwitchColorHex;
import static com.laufersteppenwolf.cutcalc.colorpicker.ColorActivity.setTextColor;
import static com.laufersteppenwolf.cutcalc.colorpicker.ColorActivity.setTextColorHex;


public class FeedRateActivity extends Activity {

    public static final String LOG_TAG = "cutcalc";

    public static final String DEFAULT = "default";
    public static final String CUSTOM = "custom";

    public static final String DARK_THEME = "dark_theme";
    public static final String CHANGE_BACKGROUND = "change_background";
    public static final String PVC_DEFAULT = "pvc_default";
    public static final String DEFAULT_FEED_MILLING = "default_feed_milling";
    public static final String DEFAULT_FEED_TURNING = "default_feed_turning";
    public static final String DEFAULT_FEED_DRILLING = "default_feed_drilling";
    public static final String DEFAULT_BLADES_MILLING = "default_milling_blades";
    public static final String DEFAULT_BLADES_TURNING = "default_turning_blades";
    public static final String DEFAULT_BLADES_DRILLING = "default_drilling_blades";
    public static final String TEXT_COLOR = "text_color";
    public static final String TEXT_COLOR_HEX = "color_hex_code";
    public static final String DEFAULT_MULTIPLIER = "default_pvc_multiplier_feed";

    private Boolean mDarkTheme;
    private Boolean mChangeBackground;
    private Boolean mPvcDefault;

    private String mFeedMilling;
    private String mFeedTurning;
    private String mFeedDrilling;
    private String mBladesMilling;
    private String mBladesTurning;
    private String mBladesDrilling;
    private String mTextColor;
    private String mTextColorCustom;

    private int mMultiplier;

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
            mFeedView.setText(mFeedDrilling);
            if (mChangeBackground) {
                mScrollView.setBackgroundResource(R.drawable.drill);
            }
        } else if (mode == 2) {
            mBlades.setText(mBladesTurning);
            mFeedView.setText(mFeedTurning);
            if (mChangeBackground) {
                mScrollView.setBackgroundResource(R.drawable.turning_chisel);
            }
        } else{
            mBlades.setText(mBladesMilling);
            mFeedView.setText(mFeedMilling);
            if (mChangeBackground) {
                mScrollView.setBackgroundResource(R.drawable.milling_cutter);
            }
        }
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
        mDarkTheme = myPreference.getBoolean(DARK_THEME, true);
        mChangeBackground = myPreference.getBoolean(CHANGE_BACKGROUND, true);
        mPvcDefault = myPreference.getBoolean(PVC_DEFAULT, false);
        mFeedMilling = myPreference.getString(DEFAULT_FEED_MILLING, getString(R.string.string_default_feed));
        mFeedTurning = myPreference.getString(DEFAULT_FEED_TURNING, getString(R.string.string_default_feed));
        mFeedDrilling = myPreference.getString(DEFAULT_FEED_DRILLING, getString(R.string.string_default_feed));
        mBladesMilling = myPreference.getString(DEFAULT_BLADES_MILLING, getString(R.string.string_default_blades_milling));
        mBladesTurning = myPreference.getString(DEFAULT_BLADES_TURNING, getString(R.string.string_default_blades_turning));
        mBladesDrilling = myPreference.getString(DEFAULT_BLADES_DRILLING, getString(R.string.string_default_blades_drilling));
        mTextColor = myPreference.getString(TEXT_COLOR, DEFAULT);
        mTextColorCustom = myPreference.getString(TEXT_COLOR_HEX, "#ffffffff");
        mMultiplier = Integer.parseInt(myPreference.getString(DEFAULT_MULTIPLIER, Integer.toString(getResources().getInteger(R.integer.multiplier_switch))));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get preferences
        getPreferences();

        if (mDarkTheme) {
            setTheme(android.R.style.Theme_Holo);
        } else {
            setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
        }
        setContentView(R.layout.activity_feed_rate);

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
        final TextView mRpmText =
                (TextView) findViewById(R.id.rpmBoxFeed);
        final TextView mFeedText =
                (TextView) findViewById(R.id.feedBox);
        final TextView mBladesText =
                (TextView) findViewById(R.id.bladesBoxFeed);
        final TextView mFeedRateText =
                (TextView) findViewById(R.id.feedRateBox);

        final String mRpmRpm =
                intent.getStringExtra("RPM");

        buttonCalc = (Button) findViewById(R.id.buttonCalcFeed);

        mMode = intent.getIntExtra("Mode", 0);

        if (mPvcDefault) {
            pvcSwitch.setChecked(true); // Use PVC data by default
        } else {
            pvcSwitch.setChecked(false); // Don't use PVC data by default
        }

        if (mDarkTheme) {
            MainActivity.setShadows(mRpm);
            MainActivity.setShadows(mFeedView);
            MainActivity.setShadows(mBlades);
            MainActivity.setShadows(mFeedRate);
            MainActivity.setShadows(mRpmText);
            MainActivity.setShadows(mFeedText);
            MainActivity.setShadows(mBladesText);
            MainActivity.setShadows(mFeedRateText);
            MainActivity.setShadowsSwitch(pvcSwitch);
        }

        if (mTextColor != DEFAULT) {
            if (mTextColor.equals(CUSTOM)) {
                Log.d(LOG_TAG, "Parsing custom hex Colors");
                setTextColorHex(mRpm, mTextColorCustom);
                setTextColorHex(mFeedView, mTextColorCustom);
                setTextColorHex(mBlades, mTextColorCustom);
                setTextColorHex(mFeedRate, mTextColorCustom);
                setTextColorHex(mRpmText, mTextColorCustom);
                setTextColorHex(mFeedText, mTextColorCustom);
                setTextColorHex(mBladesText, mTextColorCustom);
                setTextColorHex(mFeedRateText, mTextColorCustom);
                setSwitchColorHex(pvcSwitch, mTextColorCustom);
            } else {
                setTextColor(mRpm, mTextColor);
                setTextColor(mFeedView, mTextColor);
                setTextColor(mBlades, mTextColor);
                setTextColor(mFeedRate, mTextColor);
                setTextColor(mRpmText, mTextColor);
                setTextColor(mFeedText, mTextColor);
                setTextColor(mBladesText, mTextColor);
                setTextColor(mFeedRateText, mTextColor);
                setSwitchColor(pvcSwitch, mTextColor);
            }
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
                        if (mRpm.getText() != null && !mRpm.getText().toString().equals("")) {
                            if (mFeedView.getText() != null && !mFeedView.getText().toString().equals("")) {
                                if (mBlades.getText() != null && !mBlades.getText().toString().equals("")) {

                                    double rpm = Double.parseDouble(mRpm.getText().toString());
                                    double feed = Double.parseDouble(mFeedView.getText().toString());
                                    double blades = Double.parseDouble(mBlades.getText().toString());

                                    double result = round(rpm * feed * blades, 2);

                                    if (pvcSwitch.isChecked()) {
                                        result = mMultiplier * result;
                                    }
                                    mFeedRate.setText(Double.toString(result));
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.toast_add_blades),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.toast_add_feed),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_add_rpm),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        buttonDrilling.setOnClickListener(
                new Button.OnClickListener() {
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
        if ((mDarkTheme != MainActivity.darkThemeStore) ||
                (mTextColor != MainActivity.textColorStore) ||
                (mTextColorCustom != MainActivity.textColorCustomStore)) {
            this.recreate();
        }
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