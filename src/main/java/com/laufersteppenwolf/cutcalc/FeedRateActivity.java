package com.laufersteppenwolf.cutcalc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class FeedRateActivity extends Activity {

    public static int mMode = 0;

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
            mBlades.setText(R.string.string_default_blades_drilling);
            mScrollView.setBackgroundResource(R.drawable.drill);
        } else if (mode == 2) {
            mBlades.setText(R.string.string_default_blades_turning);
            mScrollView.setBackgroundResource(R.drawable.turning_chisel);
        } else{
            mBlades.setText(R.string.string_default_blades_milling);
            mScrollView.setBackgroundResource(R.drawable.milling_cutter);
        }
        doCalc();
    }

    public void doCalc() {
        final Button buttonCalc = (Button) findViewById(R.id.buttonCalcFeed);

        buttonCalc.performClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_rate);

        final Intent intent = getIntent();

        final Button buttonCalc = (Button) findViewById(R.id.buttonCalcFeed);
        final Button buttonDrilling = (Button) findViewById(R.id.buttonDrillingFeed);
        final Button buttonMilling = (Button) findViewById(R.id.buttonMillingFeed);
        final Button buttonTurning = (Button) findViewById(R.id.buttonTurningFeed);

        final Switch pvcSwitch = (Switch) findViewById(R.id.switchPvcFeed);

        final TextView mRpm =
                (TextView) findViewById(R.id.rpmFeed);
        final TextView mFeed =
                (TextView) findViewById(R.id.feed);
        final TextView mBlades =
                (TextView) findViewById(R.id.bladesFeed);
        final TextView mFeedRate =
                (TextView) findViewById(R.id.feedRate);
        final String mRpmRpm =
                intent.getStringExtra("RPM");

        mMode = intent.getIntExtra("Mode", 0);

//        final RelativeLayout mRelLayout = (RelativeLayout) findViewById(R.id.relativeLayoutFeed);

        changeMode(mMode);

        pvcSwitch.setChecked(false); // Don't use PVC data by default
        mRpm.setText(mRpmRpm);       // Use the RPM from the previously calculated data

        pvcSwitch.setOnClickListener(
                new Switch.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doCalc();
                    }
                }
        );

        mFeed.setOnClickListener(
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
                        double rpm = Double.parseDouble(mRpm.getText().toString());
                        double feed = Double.parseDouble(mFeed.getText().toString());
                        double blades = Double.parseDouble(mBlades.getText().toString());

                        double result = round(rpm * feed * blades,2);

                        if (pvcSwitch.isChecked()) {
                            result = multiplier * result;
                        }
                        mFeedRate.setText(Double.toString(result));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                return true;
            case R.id.action_features_feedRate:
                MainActivity.mMode = mMode;
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
