
package com.android.settings.fnv;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.teameos.jellybean.settings.EOSConstants;

import com.android.settings.R;
import com.android.internal.telephony.Phone;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.settings.SettingsPreferenceFragment;

public class InterfaceSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private Context mContext;
    private ContentResolver mContentResolver;

    private CheckBoxPreference mBatteryIcon;
    private CheckBoxPreference mBatteryText;
    private CheckBoxPreference mSystemUISettings;
    private ListPreference mSystemUISettingsLocation;
    private EosMultiSelectListPreference mEosQuickSettingsView;
    private boolean mEosSettingsEnabled = false;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mContext = getActivity();
        mContentResolver = mContext.getContentResolver();

        addPreferencesFromResource(R.xml.interface_settings);

        mBatteryIcon = (CheckBoxPreference) findPreference("eos_interface_statusbar_battery_icon");
        mBatteryText = (CheckBoxPreference) findPreference("eos_interface_statusbar_battery_text");
        mSystemUISettings = (CheckBoxPreference) findPreference("eos_interface_settings_eos_enabled");
        mEosQuickSettingsView = (EosMultiSelectListPreference) findPreference("eos_interface_eos_quick_enabled");
        mSystemUISettingsLocation = (ListPreference) findPreference("eos_interface_settings_eos_settings_location");

        if (mBatteryIcon != null) {
            mBatteryIcon.setChecked(Settings.System.getInt(mContentResolver,
                    EOSConstants.SYSTEMUI_BATTERY_ICON_VISIBLE,
                    EOSConstants.SYSTEMUI_BATTERY_ICON_VISIBLE_DEF) == 1);
            mBatteryIcon.setOnPreferenceChangeListener(this);
        }

        if (mBatteryText != null) {
            mBatteryText.setChecked(Settings.System.getInt(mContentResolver,
                    EOSConstants.SYSTEMUI_BATTERY_TEXT_VISIBLE,
                    EOSConstants.SYSTEMUI_BATTERY_TEXT_VISIBLE_DEF) == 1);
            mBatteryText.setOnPreferenceChangeListener(this);
        }

        if (mContext.getResources().getBoolean(R.bool.eos_tablet)) {

            if (mSystemUISettings != null) {
                mSystemUISettings.setChecked(Settings.System.getInt(getContentResolver(),
                        EOSConstants.SYSTEMUI_SETTINGS_ENABLED, 0) == 1);
                mSystemUISettings.setOnPreferenceChangeListener(this);
                mEosSettingsEnabled = mSystemUISettings.isChecked();
            }
        } else {

            if (mSystemUISettingsLocation != null) {
                if (Settings.System.getInt(mContentResolver,
                        EOSConstants.SYSTEMUI_SETTINGS_ENABLED,
                        EOSConstants.SYSTEMUI_SETTINGS_ENABLED_DEF) == 0) {
                    mSystemUISettingsLocation.setValue("disabled");
                    mSystemUISettingsLocation.notifyDependencyChange(true);
                    mEosSettingsEnabled = false;
                } else if (Settings.System.getInt(mContentResolver,
                        EOSConstants.SYSTEMUI_SETTINGS_PHONE_TOP,
                        EOSConstants.SYSTEMUI_SETTINGS_PHONE_TOP_DEF) == 1) {
                    mSystemUISettingsLocation.setValue("top");
                    mSystemUISettingsLocation.notifyDependencyChange(false);
                    mEosSettingsEnabled = true;
                } else if (Settings.System.getInt(mContentResolver,
                        EOSConstants.SYSTEMUI_SETTINGS_PHONE_BOTTOM,
                        EOSConstants.SYSTEMUI_SETTINGS_PHONE_BOTTOM_DEF) == 1) {
                    mSystemUISettingsLocation.setValue("bottom");
                    mSystemUISettingsLocation.notifyDependencyChange(false);
                    mEosSettingsEnabled = true;
                }
                mSystemUISettingsLocation.setOnPreferenceChangeListener(this);
            }
        }

        if (mEosQuickSettingsView != null) {
            mEosQuickSettingsView.setOnPreferenceChangeListener(this);
            mEosQuickSettingsView.setEntries(getResources().getStringArray(
                    R.array.eos_quick_enabled_names));
            mEosQuickSettingsView.setEntryValues(getResources().getStringArray(
                    R.array.eos_quick_enabled_preferences));
            mEosQuickSettingsView.setReturnFullList(true);
            populateEosSettingsList();
        }

    }

    private void populateEosSettingsList() {
        LinkedHashSet<String> selectedValues = new LinkedHashSet<String>();
        String enabledControls = Settings.System.getString(getContentResolver(),
                EOSConstants.SYSTEMUI_SETTINGS_ENABLED_CONTROLS);
        if (enabledControls != null) {
            String[] controls = enabledControls.split("\\|");
            selectedValues.addAll(Arrays.asList(controls));
        } else {
            selectedValues.addAll(Arrays.asList(EOSConstants.SYSTEMUI_SETTINGS_DEFAULTS));
        }
        mEosQuickSettingsView.setValues(selectedValues);

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!manager.isNetworkSupported(ConnectivityManager.TYPE_MOBILE)) {
            mEosQuickSettingsView
                    .removeValueEntry(EOSConstants.SYSTEMUI_SETTINGS_MOBILEDATA);
            mEosQuickSettingsView
                    .removeValueEntry(EOSConstants.SYSTEMUI_SETTINGS_WIFITETHER);
            mEosQuickSettingsView.removeValueEntry(EOSConstants.SYSTEMUI_SETTINGS_USBTETHER);
        }
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if(!(tm.getCurrentPhoneType() == TelephonyManager.PHONE_TYPE_CDMA)
                || !(tm.getLteOnCdmaMode() == Phone.LTE_ON_CDMA_TRUE)) {
            mEosQuickSettingsView.removeValueEntry(EOSConstants.SYSTEMUI_SETTINGS_LTE);
		}
        mEosQuickSettingsView.setEnabled(mEosSettingsEnabled);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBatteryIcon) {
            Boolean value = (Boolean) newValue;
            Settings.System.putInt(mContentResolver, EOSConstants.SYSTEMUI_BATTERY_ICON_VISIBLE,
                    value.booleanValue() ? 1 : 0);
            return true;
        } else if (preference == mBatteryText) {
            Boolean value = (Boolean) newValue;
            Settings.System.putInt(mContentResolver, EOSConstants.SYSTEMUI_BATTERY_TEXT_VISIBLE,
                    value.booleanValue() ? 1 : 0);
            return true;
        } else if (preference.equals(mEosQuickSettingsView)) {
            Map<String, Boolean> values = (Map<String, Boolean>) newValue;
            StringBuilder newPreferenceValue = new StringBuilder();
            for (Entry entry : values.entrySet()) {
                newPreferenceValue.append(entry.getKey());
                newPreferenceValue.append("|");
            }
            Settings.System.putString(getContentResolver(),
                    EOSConstants.SYSTEMUI_SETTINGS_ENABLED_CONTROLS,
                    newPreferenceValue.toString());
            return true;
        } else if (preference.equals(mSystemUISettings)) {
            Settings.System.putInt(getContentResolver(),
                    EOSConstants.SYSTEMUI_SETTINGS_ENABLED,
                    ((Boolean) newValue).booleanValue() ? 1 : 0);
            mEosQuickSettingsView.setEnabled(((Boolean) newValue).booleanValue());
            return true;
        } else if (preference.equals(mSystemUISettingsLocation)) {
            String value = (String) newValue;
            if (value.equals("disabled")) {
                Settings.System.putInt(getContentResolver(),
                        EOSConstants.SYSTEMUI_SETTINGS_ENABLED, 0);
                Settings.System.putInt(getContentResolver(),
                        EOSConstants.SYSTEMUI_SETTINGS_PHONE_TOP, 0);
                Settings.System.putInt(getContentResolver(),
                        EOSConstants.SYSTEMUI_SETTINGS_PHONE_BOTTOM, 0);
                preference.notifyDependencyChange(true);
                mEosQuickSettingsView.setEnabled(false);
            } else if (value.equals("top")) {
                Settings.System.putInt(getContentResolver(),
                        EOSConstants.SYSTEMUI_SETTINGS_ENABLED, 1);
                Settings.System.putInt(getContentResolver(),
                        EOSConstants.SYSTEMUI_SETTINGS_PHONE_TOP, 1);
                Settings.System.putInt(getContentResolver(),
                        EOSConstants.SYSTEMUI_SETTINGS_PHONE_BOTTOM, 0);
                preference.notifyDependencyChange(false);
                mEosQuickSettingsView.setEnabled(true);
            } else if (value.equals("bottom")) {
                Settings.System.putInt(getContentResolver(),
                        EOSConstants.SYSTEMUI_SETTINGS_ENABLED, 1);
                Settings.System.putInt(getContentResolver(),
                        EOSConstants.SYSTEMUI_SETTINGS_PHONE_TOP, 0);
                Settings.System.putInt(getContentResolver(),
                        EOSConstants.SYSTEMUI_SETTINGS_PHONE_BOTTOM, 1);
                preference.notifyDependencyChange(false);
                mEosQuickSettingsView.setEnabled(true);
            }
            return true;
        }
        return false;
    }
}
