/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.fnv;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class StatusBar extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String STATUS_BAR_AM_PM = "status_bar_am_pm";
    private static final String STATUS_BAR_CLOCK = "status_bar_show_clock";
    private static final String PREF_ENABLE = "clock_style";

    private ListPreference mStatusBarAmPm;
    private ListPreference mStatusBarClock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.clock_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mStatusBarClock = (ListPreference) prefSet.findPreference(PREF_ENABLE);
        mStatusBarClock.setOnPreferenceChangeListener(this);
        mStatusBarClock.setValue(Integer.toString(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.STATUS_BAR_CLOCK,
                1)));
        mStatusBarAmPm = (ListPreference) prefSet.findPreference(STATUS_BAR_AM_PM);
		mStatusBarAmPm.setOnPreferenceChangeListener(this);
		
		int statusBarAmPm = Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
		Settings.System.STATUS_BAR_AM_PM, 2);
		
		mStatusBarAmPm.setValue(String.valueOf(statusBarAmPm));
        mStatusBarAmPm.setSummary(mStatusBarAmPm.getEntry());
        
        //try {
        //    if (Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
        //            Settings.System.TIME_12_24) == 24) {
        //        mStatusBarAmPm.setEnabled(false);
        //        mStatusBarAmPm.setSummary(R.string.status_bar_am_pm_info);
        //    }
        //} catch (SettingNotFoundException e ) {
        //}

        //int statusBarAmPm = Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
        //        Settings.System.STATUS_BAR_AM_PM, 2);
        
        
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.equals(mStatusBarAmPm)) {
            int statusBarAmPm = Integer.valueOf((String) newValue);
            int index = mStatusBarAmPm.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUS_BAR_AM_PM, statusBarAmPm);
            mStatusBarAmPm.setSummary(mStatusBarAmPm.getEntries()[index]);
            return true;
        } 
        else if (preference.equals(mStatusBarClock)) {
            int clockStyle = Integer.parseInt((String) newValue);
            int index = mStatusBarClock.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUS_BAR_CLOCK, clockStyle);
            mStatusBarClock.setSummary(mStatusBarClock.getEntries()[index]);
            return true;
        }
        return false;
    }

}
