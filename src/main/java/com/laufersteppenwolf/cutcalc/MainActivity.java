package com.laufersteppenwolf.cutcalc;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.UserManager;
import android.text.Layout;
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

    public static int mMode = 0;  // 0 == Milling; 1 == Drilling; 2 == Turning
    public static int mMonkey = 0; //Monkey counter

    Random r = new Random();
    public int randomCount = r.nextInt(16 - 5) + 5;

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
            mVc.setText(R.string.string_vc_drilling);
            mScrollView.setBackgroundResource(R.drawable.drill);
        } else if (mode == 2) {
            mVc.setText(R.string.string_vc_turning);
            mScrollView.setBackgroundResource(R.drawable.turning_chisel);
        } else{
            mVc.setText(R.string.string_vc_milling);
            mScrollView.setBackgroundResource(R.drawable.milling_cutter);
        }
        doCalc();
    }

    public void doCalc() {
        final Button buttonCalc = (Button) findViewById(R.id.buttonCalc);

        buttonCalc.performClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonCalc = (Button) findViewById(R.id.buttonCalc);
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

//        final RelativeLayout mRelLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        final Animation shakeX = AnimationUtils.loadAnimation(this, R.anim.shake_x);

        final Drawable defaultBackground = mDiameter.getBackground();

        pvcSwitch.setChecked(false); // Don't use PVC data by default

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
                        double vc = Double.parseDouble(mVc.getText().toString());
                        double diameter = Double.parseDouble(mDiameter.getText().toString());

                        if (diameter == 0 ) {
                            mDiameter.setBackgroundColor(Color.RED);
                            buttonCalc.startAnimation(shakeX);
                            return;
                        } else {
                            mDiameter.setBackground(defaultBackground);
                        }

                        double result = round((vc * 1000)/(Math.PI * diameter),2);

                        if (pvcSwitch.isChecked()) {
                            result = multiplier * result;
                        }
                        mRpm.setText(Double.toString(result));

                        //Just for fun :D
                        mMonkey++;
                        if (mMonkey == randomCount && mMode == 0) {
                            mMonkey = 0;
                            randomCount = r.nextInt(21 - 7) + 7;

                            if (ActivityManager.isUserAMonkey()) {
                                Toast.makeText(getApplicationContext(), getString(R.string.string_monkey),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.string_no_monkey),
                                        Toast.LENGTH_SHORT).show();
                            }
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
        changeMode(mMode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                return true;
            case R.id.action_features_feedRate:
                intent.putExtra("RPM", getRpm().toString());
                intent.putExtra("Mode", mMode);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
