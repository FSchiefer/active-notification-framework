package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

import android.content.Context;
import android.content.res.Resources;
import android.preference.ListPreference;

import de.fiduciagad.anflibrary.R;

/**
 * Created by Felix Schiefer on 06.01.2016.
 */
public class VibrationSetting extends ListPreference {
    public VibrationSetting(Context context, String service) {
        super(context);
        setValues(service);
    }

    private void setValues(String service) {
        Resources res = getContext().getResources();

        this.setKey(service + res.getString(R.string.vibration_key));
        this.setSummary(res.getString(R.string.vibration_summary));
        this.setTitle(res.getString(R.string.vibration_name));
        this.setDefaultValue("0");

        this.setEntries(res.getStringArray(R.array.vibration_names));
        this.setEntryValues(res.getStringArray(R.array.vibration_values));
    }
}
