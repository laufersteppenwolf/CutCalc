package com.laufersteppenwolf.cutcalc;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;

public class HelpRpm extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.mDarkTheme) {
            if (!MainActivity.mBackgroundColor.equals(MainActivity.TRANSPARENT)) {
                setTheme(android.R.style.Theme_Holo);
            } else {
                setTheme(android.R.style.Theme_Holo_Wallpaper);
            }
        } else {
            if (!MainActivity.mBackgroundColor.equals(MainActivity.TRANSPARENT)) {
                setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
            } else {
                setTheme(android.R.style.Theme_Holo_Wallpaper);
            }
        }

        setContentView(R.layout.activity_help_rpm);

        if (!MainActivity.mDarkTheme &&
                !MainActivity.mBackgroundColor.equals(MainActivity.TRANSPARENT)) {
            ImageView imageViewHelp = (ImageView) findViewById(R.id.imageViewHelp);
            imageViewHelp.setImageResource(R.drawable.help_formula);
        }
    }

}
