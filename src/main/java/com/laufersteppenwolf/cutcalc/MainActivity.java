package com.laufersteppenwolf.cutcalc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class MainActivity extends Activity {

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
        final TextView mRpmFeed =
                (TextView) findViewById(R.id.rpmFeed);

        final RelativeLayout mRelLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
//        mRelLayout.setBackgroundResource(R.drawable.schaftfraeser_scaled);

        final Animation shakeX = AnimationUtils.loadAnimation(this, R.anim.shake_x);

        pvcSwitch.setChecked(false); // Don't use PVC data by default

        mRpm.setText("0");

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
                            mDiameter.setBackgroundColor(Color.TRANSPARENT);
                        }

                        double result = round((vc * 1000)/(Math.PI * diameter),2);

                        if (pvcSwitch.isChecked()) {
                            result = multiplier * result;
                        }
                        mRpm.setText(Double.toString(result));
                    }
                }
        );
        buttonDrilling.setOnClickListener(
                new Button.OnClickListener()  {
                    @Override
                    public void onClick(View v) {
                        mVc.setText(R.string.string_vc_drilling);
                        mRelLayout.setBackgroundResource(R.drawable.bohrer_scaled);
                        buttonCalc.performClick();
                    }
                }
        );
        buttonMilling.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVc.setText(R.string.string_vc_milling);
                        mRelLayout.setBackgroundResource(R.drawable.schaftfraeser_scaled);
                        buttonCalc.performClick();
                    }
                }
        );
        buttonTurning.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVc.setText(R.string.string_vc_turning);
                        mRelLayout.setBackgroundResource(R.drawable.drehmeissel_scaled);
                        buttonCalc.performClick();
                    }
                }
        );
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
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
