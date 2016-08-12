package de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFServiceHandling;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import de.fiduciagad.anflibrary.R;

/**
 * Created by Felix Schiefer on 06.01.2016.
 */
public class CreateDefaultValues {
    Context context;

    public CreateDefaultValues(Context context) {
        this.context = context;
    }

    public void setDefaultValues(String service) {
        Resources res = context.getResources();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        for (String keys : preferences.getAll().keySet()) {
            Log.d("CreateDefaultValues", keys);
        }

        if (!preferences.getAll().keySet().contains(service + res.getString(R.string.service_key))) {
            editor.putBoolean(service + res.getString(R.string.service_key), true);
            editor.putBoolean(service + res.getString(R.string.urgency_key), true);
            editor.putBoolean(service + res.getString(R.string.confidential_key), true);
            editor.putBoolean(service + res.getString(R.string.allConfidential_key), false);
            editor.putBoolean(service + res.getString(R.string.allUrgent_key), false);
            editor.putString(service + res.getString(R.string.vibration_key), "0");
            editor.commit();
        }
    }
}
