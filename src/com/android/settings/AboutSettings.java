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
        Preference mFnvDonate;
        Preference mFnvGithub;
        Preference mFnvGerrit;
        Preference mFnvIrc;
        Preference mFnvTwitter;
        Preference mFnvFacebook;
        Preference mFnvGoogleplus;
        Preference mFnvToro;
        Preference mFnvMaguro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about_settings);
        
        mFnvWebpage = findPreference("fnv_webpage");
        mFnvDonate = findPreference("fnv_donate");
        mFnvGithub = findPreference("fnv_github");
        mFnvGerrit = findPreference("fnv_gerrit");
        mFnvIrc = findPreference("fnv_irc");
        mFnvTwitter = findPreference("fnv_twitter");
        mFnvFacebook = findPreference("fnv_facebook");
        mFnvGoogleplus = findPreference("fnv_googleplus");
        mFnvToro = findPreference("fnv_toro");
        mFnvMaguro = findPreference("fnv_maguro");
        
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mFnvWebpage) {
            gotoUrl("http://projectfnv.com/");
       } else if (preference == mFnvDonate) {
            gotoUrl("http://goo.gl/3Vo2G");
        } else if (preference == mFnvGithub) {
            gotoUrl("https://github.com/FruitsAndVeggies/");
        } else if (preference == mFnvGerrit) {
            gotoUrl("http://fnv.snipanet.com/");
        } else if (preference == mFnvIrc) {
            gotoUrl("http://webchat.freenode.net/?channels=fnv");
        } else if (preference == mFnvTwitter) {
            gotoUrl("http://twitter.com/ProjectFNV");
        } else if (preference == mFnvFacebook) {
            gotoUrl("http://www.facebook.com/projectfnv");
        } else if (preference == mFnvGoogleplus) {
            gotoUrl("http://gplus.to/projectfnv");
        } else if (preference == mFnvToro) {
            gotoUrl("http://rootzwiki.com/topic/30061-romaosp411-project-fnv-build-008-rc4-now-with-pcb-goodness-home-grown-community-raised/");
        } else if (preference == mFnvMaguro) {
            gotoUrl("http://rootzwiki.com/topic/30514-jbrommaguro-project-fnv-fruits-veggies-a-community-rom/");
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void gotoUrl(String url) {
        Uri page = Uri.parse(url);
        Intent internet = new Intent(Intent.ACTION_VIEW, page);
        getActivity().startActivity(internet);
    }
}
