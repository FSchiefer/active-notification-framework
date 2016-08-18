package de.fiduciagad.anflibrary.anFConnector.anFSettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData.AllConfidentialSetting;
import de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData.ServiceAllowedSetting;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFServiceHandling.ServiceDB;
import de.fiduciagad.anflibrary.R;
import de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData.AllUrgentSetting;
import de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData.ConfidentialSetting;
import de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData.UrgencySetting;
import de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData.VibrationSetting;

import java.util.List;

//TODO FS add javadoc
public class SettingsFragment extends PreferenceFragment {

 private static final String CLASS_NAME = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPreferenceScreen(defaultPref());
        addPreferencesFromResource(R.xml.contextpreferences);
    }

    // The first time application is launched this should be read
    private PreferenceScreen defaultPref() {
        final Context context = getActivity();

        Resources res = context.getResources();
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(context);
        PreferenceScreen serviceSite = getPreferenceManager().createPreferenceScreen(context);
        serviceSite.setTitle("Services");
        ServiceDB serviceDB = new ServiceDB(context);
        List<String> c = serviceDB.getServiceList();

        Log.d(CLASS_NAME, "Cursor Count " + c.size());

        for (String service : c) {
            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
            screen.setTitle(service);

            ServiceAllowedSetting allowedSetting = new ServiceAllowedSetting(context, service);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            AllUrgentSetting allUrgentSetting = new AllUrgentSetting(context, service);
            ConfidentialSetting confSetting = new ConfidentialSetting(context, service);
            AllConfidentialSetting allConfidentialSetting = new AllConfidentialSetting(context, service);
            UrgencySetting urgSetting = new UrgencySetting(context, service);
            VibrationSetting vibrations = new VibrationSetting(context, service);

            screen.addPreference(allowedSetting);
            screen.addPreference(confSetting);
            screen.addPreference(allConfidentialSetting);
            screen.addPreference(urgSetting);

            screen.addPreference(allUrgentSetting);
            screen.addPreference(vibrations);

            serviceSite.addPreference(screen);
            Log.d("SettingsActivity", " Messages found " + service);
        }
        root.addPreference(serviceSite);

        return root;
    }
    
}
