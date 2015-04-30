package com.laufersteppenwolf.cutcalc.colorpicker;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

import com.laufersteppenwolf.cutcalc.MainActivity;
import com.laufersteppenwolf.cutcalc.R;

public class ColorActivity extends MainActivity {

    private static final String LOG_TAG = "cutcalc";

    private static final String DEFAULT = "default";
    private static final String BLACK = "black";
    private static final String WHITE = "white";
    private static final String RED = "red";
    private static final String BLUE = "blue";
    private static final String BRIGHT_BLUE = "bright_blue";
    private static final String YELLOW = "yellow";
    private static final String GREEN = "green";
    private static final String BRIGHT_GREEN = "bright_green";

    public static int getColorCode(String color) {

        Context mContext = getContext();

        switch (color) {
            case DEFAULT:
                return -1;
            case BLACK:
                return mContext.getResources().getColor(R.color.black);
            case WHITE:
                return mContext.getResources().getColor(R.color.white);
            case RED:
                return mContext.getResources().getColor(R.color.red);
            case BLUE:
                return mContext.getResources().getColor(R.color.blue);
            case BRIGHT_BLUE:
                return mContext.getResources().getColor(R.color.bright_blue);
            case YELLOW:
                return mContext.getResources().getColor(R.color.yellow);
            case GREEN:
                return mContext.getResources().getColor(R.color.green);
            case BRIGHT_GREEN:
                return mContext.getResources().getColor(R.color.bright_green);
        }
        return -1;
    }

    public static void setTextColor(TextView tv, String color) {
        tv.setTextColor(getColorCode(color));
        Log.d(LOG_TAG, "Parsing color: " + color);
    }

    public static void setSwitchColor(Switch sw, String color) {
        sw.setTextColor(getColorCode(color));
        Log.d(LOG_TAG, "Parsing color: " + color);
    }

    public static void setTextColorHex(TextView tv, String code) {
        if (code.contains("#")) {
            tv.setTextColor(Color.parseColor(code));
            Log.d(LOG_TAG, "Parsing color code: " + code);
        }
    }

    public static void setSwitchColorHex(Switch sw, String code) {
        if (code.contains("#")) {
            sw.setTextColor(Color.parseColor(code));
            Log.d(LOG_TAG, "Parsing color code: " + code);
        }
    }

}
