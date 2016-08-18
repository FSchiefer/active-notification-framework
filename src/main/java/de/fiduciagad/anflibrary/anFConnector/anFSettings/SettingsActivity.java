package de.fiduciagad.anflibrary.anFConnector.anFSettings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * This acitivity can be connected to any control to start the settings activity. 
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
    
}
