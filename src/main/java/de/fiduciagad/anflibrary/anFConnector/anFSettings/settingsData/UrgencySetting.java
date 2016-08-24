package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

import android.content.Context;
import android.content.res.Resources;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;

import de.fiduciagad.anflibrary.R;

/**
 * With this class you can create a preference checkbox for a user to allow the receiving
 * of urgent messages on this device
 */
public class UrgencySetting extends CheckBoxPreference {

    public UrgencySetting(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * This constructor can be used to create the default values for the urgency settings
     *
     * @param context The application context
     * @param service The name of the service for that a user want's to make the setting
     */
    public UrgencySetting(Context context, String service) {
        super(context);
        setValues(service);
    }

    /**
     * This method is made to set the values of the urgency setting
     *
     * @param service The name of the service for that a user want's to make the setting
     */
    private void setValues(String service) {
        Resources res = getContext().getResources();

        this.setKey(service + res.getString(R.string.urgency_key));
        this.setChecked(true);
        this.setDefaultValue(true);
        this.setSummary(res.getString(R.string.useUrgency));
        this.setTitle(res.getString(R.string.urgency));
    }
}
