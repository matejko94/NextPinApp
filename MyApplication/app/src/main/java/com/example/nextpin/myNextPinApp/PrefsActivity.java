package com.example.nextpin.myNextPinApp;

    import android.os.Bundle;
    import android.preference.PreferenceActivity;


    public class PrefsActivity extends PreferenceActivity {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // addPreferencesFromResource(R.xml.preferences);
            getFragmentManager().beginTransaction().replace(android.R.id.content,
                    new PreferencesFragment()).commit();
        }
    }
