package com.laufersteppenwolf.cutcalc;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.UserManager;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;


public class MainActivity extends Activity {

    public static final String LOG_TAG = "CutCalc";

    public static final String CHANGE_BACKGROUND = "change_background";
    public static final String PVC_DEFAULT = "pvc_default";
    public static final String DEFAULT_VC_MILLING = "default_milling_cuttingspeed";
    public static final String DEFAULT_VC_TURNING = "default_turning_cuttingspeed";
    public static final String DEFAULT_VC_DRILLING = "default_drilling_cuttingspeed";

    private Boolean mChangeBackground;
    private Boolean mPvcDefault;

    private String mVcMilling;
    private String mVcTurning;
    private String mVcDrilling;

    public static int mMode = 0;  // 0 == Milling; 1 == Drilling; 2 == Turning
    public static int mMonkey = 0; //Monkey counter

    Button buttonCalc;

    Random r = new Random();
    public int randomCount = r.nextInt(20 - 7) + 7;

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public CharSequence getRpm() {
        final TextView rpm =
                (TextView) findViewById(R.id.rpm);
        return rpm.getText();
    }

    public void changeMode(int mode) {
        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollView);
        final TextView mVc = (TextView) findViewById(R.id.cuttingSpeed);

        if (mode == 1) {
            mVc.setText(mVcDrilling);
            if (mChangeBackground) {
                mScrollView.setBackgroundResource(R.drawable.drill);
            }
        } else if (mode == 2) {
            mVc.setText(mVcTurning);
            if (mChangeBackground) {
                mScrollView.setBackgroundResource(R.drawable.turning_chisel);
            }
        } else{
            mVc.setText(mVcMilling);
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
        mChangeBackground = myPreference.getBoolean(CHANGE_BACKGROUND, true);
        mPvcDefault = myPreference.getBoolean(PVC_DEFAULT, false);
        mVcMilling = myPreference.getString(DEFAULT_VC_MILLING, getString(R.string.string_vc_milling));
        mVcTurning = myPreference.getString(DEFAULT_VC_TURNING, getString(R.string.string_vc_turning));
        mVcDrilling = myPreference.getString(DEFAULT_VC_DRILLING, getString(R.string.string_vc_drilling));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get preferences
        getPreferences();

        final Button buttonDrilling = (Button) findViewById(R.id.buttonDrilling);
        final Button buttonMilling = (Button) findViewById(R.id.buttonMilling);
        final Button buttonTurning = (Button) findViewById(R.id.buttonTurning);

        final Switch pvcSwitch = (Switch) findViewById(R.id.switchPvc);

        final TextView mDiameter =
                (TextView) findViewById(R.id.diameter);
        final TextView mVc =
                (TextView) findViewById(R.id.cuttingSpeed);
        final TextView mRpm =
                (TextView) findViewById(R.id.rpm);

        buttonCalc = (Button) findViewById(R.id.buttonCalc);

        final Animation shakeX = AnimationUtils.loadAnimation(this, R.anim.shake_x);

        final Drawable defaultBackground = mDiameter.getBackground();

        if (mPvcDefault) {
            pvcSwitch.setChecked(true); // Use PVC data by default
        } else {
            pvcSwitch.setChecked(false); // Don't use PVC data by default
        }

        mDiameter.setOnClickListener(
                new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doCalc();
                    }
                }
        );

        mVc.setOnClickListener(
                new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doCalc();
                    }
                }
        );

        pvcSwitch.setOnClickListener(
                new Switch.OnClickListener() {
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

                        Log.d(LOG_TAG, "Diameter:" + mDiameter.getText().toString());

                        if (mVc.getText() != null && !mVc.getText().toString().equals("")) {
                            if (mDiameter.getText() != null && !mDiameter.getText().toString().equals("")) {
                                double vc = Double.parseDouble(mVc.getText().toString());
                                double diameter = Double.parseDouble(mDiameter.getText().toString());

                                if (diameter == 0) {
                                    mDiameter.setBackgroundColor(Color.RED);
                                    buttonCalc.startAnimation(shakeX);
                                    return;
                                } else {
                                    mDiameter.setBackground(defaultBackground);
                                }

                                double result = round((vc * 1000) / (Math.PI * diameter), 2);

                                if (pvcSwitch.isChecked()) {
                                    result = multiplier * result;
                                }
                                mRpm.setText(Double.toString(result));

                                //Just for fun :D
                                mMonkey++;
                                if (mMonkey == randomCount && mMode == 0) {
                                    mMonkey = 0;
                                    randomCount = r.nextInt(30 - 10) + 10;

                                    if (ActivityManager.isUserAMonkey()) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.string_monkey),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), getString(R.string.string_no_monkey),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.toast_add_diameter),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_add_cutting_speed),
                                    Toast.LENGTH_SHORT).show();
                        }
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
        changeMode(mMode);
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
        getMenuInflater().inflate(R.menu.menu_main_features, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final Intent intent = new Intent(MainActivity.this, FeedRateActivity.class);

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                final Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_features_feedRate:
                intent.putExtra("RPM", getRpm().toString());
                intent.putExtra("Mode", mMode);
                startActivity(intent);
                return true;
            case R.id.action_features_boschdb:
                Intent boschdbIntent = getPackageManager().getLaunchIntentForPackage("com.laufersteppenwolf.boschdb");
                startActivity(boschdbIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
