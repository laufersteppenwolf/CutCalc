package com.laufersteppenwolf.cutcalc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class FeedRateActivity extends Activity {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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

        final RelativeLayout mRelLayout = (RelativeLayout) findViewById(R.id.relativeLayoutFeed);
        mRelLayout.setBackgroundResource(R.drawable.schaftfraeser_scaled);

        pvcSwitch.setChecked(false); // Don't use PVC data by default
        mRpm.setText(mRpmRpm);       // Use the RPM from the previously calculated data

        buttonCalc.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int multiplier = getResources().getInteger(R.integer.multiplier_switch);
                        double rpm = Double.parseDouble(mRpm.getText().toString());
                        double feed = Double.parseDouble(mFeed.getText().toString());
                        double blades = Double.parseDouble(mBlades.getText().toString());

/*                        if (diameter == 0 ) {
                            mDiameter.setBackgroundColor(Color.RED);
                            buttonCalc.startAnimation(shakeX);
                            return;
                        } else {
                            mDiameter.setBackgroundColor(Color.TRANSPARENT);
                        } */ //TODO: Add warnings and NPE preventions

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
                        mBlades.setText(R.string.string_default_blades_drilling);
                        mRelLayout.setBackgroundResource(R.drawable.bohrer_scaled);
                        buttonCalc.performClick();
                    }
                }
        );
        buttonMilling.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBlades.setText(R.string.string_default_blades_milling);
                        mRelLayout.setBackgroundResource(R.drawable.schaftfraeser_scaled);
                        buttonCalc.performClick();
                    }
                }
        );
        buttonTurning.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBlades.setText(R.string.string_default_blades_turning);
                        mRelLayout.setBackgroundResource(R.drawable.drehmeissel_scaled);
                        buttonCalc.performClick();
                    }
                }
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed_rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
