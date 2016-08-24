package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

import android.content.Context;
import android.content.res.Resources;
import android.preference.ListPreference;

import de.fiduciagad.anflibrary.R;

/**
 * This class generates a List preference with all Patterns from the Vibration Patterns
 * class
 */
public class VibrationSetting extends ListPreference {

    public VibrationSetting(Context context, String service) {
        super(context);
        setValues(service);
    }

    /**
     * This Mehtod is used to generate the View with default values of the Setting
     *
     * @param service The name of the service for that the pattern is set
     */
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
