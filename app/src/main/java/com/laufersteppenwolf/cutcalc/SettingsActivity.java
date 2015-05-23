package com.laufersteppenwolf.cutcalc;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaActionSound;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = true;

    public static final String LOG_TAG = MainActivity.LOG_TAG;

    public static final String DEFAULT = "default";
    public static final String CUSTOM = "custom";
    public static final String TEXT_COLOR = "text_color";
    public static final String BACKGROUND_COLOR = "background_color";
    public static final String COLOR_HEX_CODE = "color_hex_code";
    public static final String VERSION = "version";
    public static final String EASTEREGG_KEY = "easteregg";
    public static final String BACKGROUND_COLOR_BACKUP = "background_color_backup";
    public static final String TEXT_COLOR_BACKUP = "text_color_backup";
    public static final String HEX_COLOR_BACKUP = "hex_color_backup";

    public static final int EASTEREGG_CLICKS = 7;

    static SharedPreferences myPreference;
    static Preference hexCode;

    static int versionClickCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // TODO: If Settings has multiple levels, Up should navigate up
            // that hierarchy.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

        final Preference myPref = (Preference) findPreference("no_background");
        myPreference = PreferenceManager.getDefaultSharedPreferences(this);
        final Preference changeBackground = (Preference) findPreference("change_background");
        hexCode = (Preference) findPreference(COLOR_HEX_CODE);
        final Preference version = (Preference) findPreference(VERSION);

        myPreference.registerOnSharedPreferenceChangeListener(this);

        if (myPreference.getBoolean("no_background", false)) {
            changeBackground.setEnabled(false);
        }

        if (myPreference.getString(TEXT_COLOR, DEFAULT).equals(CUSTOM) ||
                myPreference.getString(BACKGROUND_COLOR, DEFAULT).equals(CUSTOM)) {
            hexCode.setEnabled(true);
        } else {
            hexCode.setEnabled(false);
        }

        myPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean isChecked = myPreference.getBoolean("no_background", false);
                if (isChecked) {
                    changeBackground.setEnabled(true);
                } else {
                    changeBackground.setEnabled(false);
                }
                return true;
            }
        });

        version.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                versionClickCounter++;
                if (versionClickCounter >= EASTEREGG_CLICKS) {
                    SharedPreferences.Editor myPreferenceEditor = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).edit();
                    SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
                    if (!MainActivity.mEasteregg) { //Use MainActivity's variable to enforce its recreation
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_easteregg),
                                Toast.LENGTH_LONG).show();

                        myPreferenceEditor.putString(BACKGROUND_COLOR_BACKUP, myPreference.getString(BACKGROUND_COLOR, DEFAULT));
                        myPreferenceEditor.putString(TEXT_COLOR_BACKUP, myPreference.getString(TEXT_COLOR, DEFAULT));
                        myPreferenceEditor.putString(HEX_COLOR_BACKUP, myPreference.getString(COLOR_HEX_CODE, "#ffffff"));

                        myPreferenceEditor.putString(BACKGROUND_COLOR, CUSTOM);
                        myPreferenceEditor.putString(TEXT_COLOR, CUSTOM);
                        myPreferenceEditor.putString(COLOR_HEX_CODE, "#FFFF69B4");
                        myPreferenceEditor.putBoolean(EASTEREGG_KEY, true);

                        myPreferenceEditor.commit();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_easteregg_restore),
                                Toast.LENGTH_LONG).show();

                        myPreferenceEditor.putString(BACKGROUND_COLOR, myPreference.getString(BACKGROUND_COLOR_BACKUP, DEFAULT));
                        myPreferenceEditor.putString(TEXT_COLOR, myPreference.getString(TEXT_COLOR_BACKUP, DEFAULT));
                        myPreferenceEditor.putString(COLOR_HEX_CODE, myPreference.getString(HEX_COLOR_BACKUP, "#ffffff"));
                        myPreferenceEditor.putBoolean(EASTEREGG_KEY, false);

                        myPreferenceEditor.commit();
                    }
                    versionClickCounter = 0;
                }
                return true;
            }
        });

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference("default_milling_cuttingspeed"));
        bindPreferenceSummaryToValue(findPreference("default_turning_cuttingspeed"));
        bindPreferenceSummaryToValue(findPreference("default_drilling_cuttingspeed"));
        bindPreferenceSummaryToValue(findPreference("default_feed_milling"));
        bindPreferenceSummaryToValue(findPreference("default_feed_turning"));
        bindPreferenceSummaryToValue(findPreference("default_feed_drilling"));
        bindPreferenceSummaryToValue(findPreference("default_milling_blades"));
        bindPreferenceSummaryToValue(findPreference("default_turning_blades"));
        bindPreferenceSummaryToValue(findPreference("default_drilling_blades"));
        bindPreferenceSummaryToValue(findPreference("default_pvc_multiplier_feed"));
        bindPreferenceSummaryToValue(findPreference("default_pvc_multiplier_rpm"));

        bindPreferenceSummaryToValue(findPreference("text_color"));
        bindPreferenceSummaryToValue(findPreference("background_color"));
        bindPreferenceSummaryToValue(findPreference("color_hex_code"));

        bindPreferenceSummaryToValue(findPreference("version"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Determines whether the simplified settings UI should be shown. This is
     * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
     * doesn't have newer APIs like {@link PreferenceFragment}, or the device
     * doesn't have an extra-large screen. In these cases, a single-pane
     * "simplified" settings UI should be shown.
     */
    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        if (!isSimplePreferences(this)) {
            loadHeadersFromResource(R.xml.pref_headers, target);
        }
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v(LOG_TAG, "onSharedPreferenceChanged");
        switch (key){
            case TEXT_COLOR:
            case BACKGROUND_COLOR:
                String text = myPreference.getString(TEXT_COLOR, DEFAULT);
                String background = myPreference.getString(BACKGROUND_COLOR, DEFAULT);
                Log.d(LOG_TAG, "text: " + text + "  background: " + background);
                if (text.equals(CUSTOM) || background.equals(CUSTOM)) {
                    hexCode.setEnabled(true);
                } else {
                    hexCode.setEnabled(false);
                }
                break;
        }
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }
    }
}
