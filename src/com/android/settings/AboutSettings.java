/**
 * Shamelessly based on the work of syaoran12
 */

package com.android.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.android.settings.R;

public class AboutSettings extends SettingsPreferenceFragment {

        Preference mFnvWebpage;
        Preference mFnvGithub;
        Preference mFnvIrc;
        Preference mFnvTwitter;
        Preference mFnvFacebook;
        Preference mFnvGoogleplus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about_settings);
        
        mFnvWebpage = findPreference("fnv_webpage");
        mFnvGithub = findPreference("fnv_github");
        mFnvIrc = findPreference("fnv_irc");
        mFnvTwitter = findPreference("fnv_twitter");
        mFnvFacebook = findPreference("fnv_facebook");
        mFnvGoogleplus = findPreference("fnv_googleplus");
        
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mFnvWebpage) {
            gotoUrl("http://projectfnv.com/");
        } else if (preference == mFnvGithub) {
            gotoUrl("https://github.com/FruitsAndVeggies/");
        } else if (preference == mFnvIrc) {
            gotoUrl("http://webchat.freenode.net/?channels=fnv");
        } else if (preference == mFnvTwitter) {
            gotoUrl("http://twitter.com/ProjectFNV");
        } else if (preference == mFnvFacebook) {
            gotoUrl("http://www.facebook.com/projectfnv");
        } else if (preference == mFnvGoogleplus) {
            gotoUrl("http://gplus.to/projectfnv");
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void gotoUrl(String url) {
        Uri page = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, page);
        getActivity().startActivity(i);
    }
}
