package com.example.nextpin.myNextPinApp;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.util.Log;

import net.nextpin.geolib.NextPin;
import net.nextpin.geolib.http.NextPinAPI;

import java.util.HashMap;


/**
 * Created by matejs on 1.12.2015.
 */
public class PreferencesFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    public static final String PREF_TOKEN = "PREF_TOKEN";
    public static final String PREF_DB_NUMBER = "PREF_DB_NUMBER";

    // /DEV option
    public static final String PREF_DEV_SERVER = "PREF_DEV_SERVER";

    private static final String TAG = "PreferencesFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference pref = findPreference(PREF_TOKEN);
        pref.setOnPreferenceChangeListener(this);
        setTokenPrefSummary(pref, NextPin.getToken(getActivity()));

        NextPin nextpin = NextPin.getNextPinInstance(getActivity());
        pref = findPreference(PREF_DB_NUMBER);
        pref.setSummary(String.valueOf(nextpin.getOfflinceRecordsNumber()));

        populateDevOptions();

    }

    /**
     * Adds a developer's options. These should not be visible to the user. This can be completely
     * removed in the real-world application
     * */
    private void populateDevOptions() {
        final NextPinAPI api = NextPinAPI.getAPI(getActivity(), NextPin.getToken(getActivity()));
        if (!api.isDeveloperInstance()){
            return;
        }

        try {
            PreferenceCategory category = new PreferenceCategory(getActivity());
            category.setTitle("Developer's options");
            this.getPreferenceScreen().addPreference(category);

            ListPreference serverListPref = new ListPreference(getActivity());
            serverListPref.setKey(PREF_DEV_SERVER);
            HashMap<String,String> servers = api.getAvailabelServers(getActivity());

            int size = servers.keySet().size();
            serverListPref.setTitle("Server URL");
            serverListPref.setSummary("Currently: " + api.getBaseServer());
            serverListPref.setPersistent(true);

            serverListPref.setEntries(servers.keySet().toArray(new String[size]));
            serverListPref.setEntryValues(servers.values().toArray(new String[size]));

            category.addPreference(serverListPref);

            serverListPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (preference.getKey().equals(PREF_DEV_SERVER)){
                        try {
                            api.setDeveloperServer(getActivity(), (String) newValue);
                            preference.setSummary("Currently: "+api.getBaseServer());
                        }catch (Exception ex){}
                        return true;
                    }
                    return true;
                }
            });

        }catch (Exception ex){}
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.d(TAG, "preference changed: " + preference + ", val: " + newValue);
        if (preference.getKey().equals(PREF_TOKEN)){
            String token = (String)newValue;
            setTokenPrefSummary(preference,token);

            NextPin.setToken(token, getActivity());
            NextPinAPI api = NextPinAPI.getAPI(getActivity().getApplicationContext(),token);
            api.resetSession();
            return true;
        }

        return true;
    }

    /**
     * Sets the summary of the preference, to show proper URL
     * @param _pref
     * @param _token
     */
    private void setTokenPrefSummary(Preference _pref, String _token){
        NextPinAPI api = NextPinAPI.getAPI(getActivity().getApplicationContext(),_token);
        String server = api.getBaseServer();
        String url ="http://"+server+"/?user="+_token;
        String summaryStr = getActivity().getString(R.string.pref_token_summary, url);
        _pref.setSummary(summaryStr);
    }
}
