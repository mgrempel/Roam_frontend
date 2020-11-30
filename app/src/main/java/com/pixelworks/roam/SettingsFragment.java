package com.pixelworks.roam;

import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        //Get control reference
        Preference signOut = findPreference("signOut");

        //Handle click event
        signOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.d("TEST", "Logout button clicked.");
                //Clear out our UUID and ID shared preferences
                SharedPreferencesHelper.logOut();

                getActivity().finish();
                return true;
            }
        });
    }
}