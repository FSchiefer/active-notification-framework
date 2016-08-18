package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

import android.content.Context;
import android.content.res.Resources;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;

import de.fiduciagad.anflibrary.R;

/**
 * With this class you can create a preference checkbox for a user to set all messages
 * of a specified service as urgent
 */
public class AllUrgentSetting extends CheckBoxPreference {

    public AllUrgentSetting(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * This constructor can be used to create the default values for the urgent settings
     *
     * @param context The application context
     * @param service The name of the service for that a user want's to make the setting
     */
    public AllUrgentSetting(Context context, String service) {
        super(context);
        setValues(service);
    }

    /**
     * This method is made to set the values of the allUrgent setting
     *
     * @param service The name of the service for that a user want's to make the setting
     */
    private void setValues(String service) {
        Resources res = getContext().getResources();

        this.setKey(service + res.getString(R.string.allUrgent_key));
        this.setChecked(false);
        this.setDefaultValue(false);
        this.setSummary(res.getString(R.string.allUrgentSummary));
        this.setTitle(res.getString(R.string.allUrgent));
    }
}
