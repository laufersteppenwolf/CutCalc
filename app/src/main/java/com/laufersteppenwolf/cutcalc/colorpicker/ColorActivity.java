package com.laufersteppenwolf.cutcalc.colorpicker;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String TRANSPARENT = "transparent";

    private static final String[] ALLOWED_CHARS = new String[] {"#", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static final String[] DISALLOWED_CHARS = new String[] {"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "!", "?", "ß", "+", "-", ",", ".", "§", "%", "&", "/", "(", ")", "=", "'", "~", "_", ";", ":", "<", ">", "|", "^", "°"};

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
            case TRANSPARENT:
                return mContext.getResources().getColor(R.color.transparent);
        }
        return -1;
    }

    public static boolean colorIsValid(String color) {
        Context mContext = getContext();

        switch (color.length()) {
            case 6:
                if (color.startsWith("#"))
                    return false; //too few
                break;
            case 7:
                if (color.startsWith("#"))
                    break;
                Log.e(LOG_TAG, "Too many!");
                return false; //too many
            case 8:
                if (color.startsWith("#"))
                    return false; //too few
                break;
            case 9:
                if (color.startsWith("#"))
                    break;
                Log.e(LOG_TAG, "Too many!");
                return false;
            default:
                return false;
        }

        for (String s : DISALLOWED_CHARS) {
            if (color.contains(s)) {
                Log.e(LOG_TAG, "Color code is not valid!");
                Log.e(LOG_TAG, "Detected character: " + s);

                Toast.makeText(mContext.getApplicationContext(), mContext.getString(R.string.toast_invalid_colorcode),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Log.d(LOG_TAG, "Color code is valid!");
        return true;
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
        if (colorIsValid(code)) {
            if (code.startsWith("#")) {
                tv.setTextColor(Color.parseColor(code));
                Log.d(LOG_TAG, "Parsing color code: " + code);
            } else {
                tv.setTextColor(Color.parseColor("#" + code));
                Log.d(LOG_TAG, "Parsing color code: #" + code);
            }
        }
    }

    public static void setSwitchColorHex(Switch sw, String code) {
        if (colorIsValid(code)) {
            if (code.startsWith("#")) {
                sw.setTextColor(Color.parseColor(code));
                Log.d(LOG_TAG, "Parsing color code: " + code);
            } else {
                sw.setTextColor(Color.parseColor("#" + code));
                Log.d(LOG_TAG, "Parsing color code: #" + code);
            }
        }
    }

}
