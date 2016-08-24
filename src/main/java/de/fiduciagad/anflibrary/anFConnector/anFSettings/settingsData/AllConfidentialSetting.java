package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

import android.content.Context;
import android.content.res.Resources;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;

import de.fiduciagad.anflibrary.R;

//TODO: Check settings for code clones
/**
 * With this class you can create a preference checkbox for a user to set all messages
 * of a specified service as confidential. No Changes needed to use the
 */
public class AllConfidentialSetting extends CheckBoxPreference {

    public AllConfidentialSetting(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * This constructor can be used to create the default values for the allConfidential settings
     *
     * @param context The application context
     * @param service The name of the service for that a user want's to make the setting
     */
    public AllConfidentialSetting(Context context, String service) {
        super(context);
        setValues(service);
    }

    /**
     * This method is made to set the values of the allConfigential setting
     * @param service The name of the service for that a user want's to make the setting
     */
    private void setValues(String service) {
        Resources res = getContext().getResources();

        this.setKey(service + res.getString(R.string.allConfidential_key));
        this.setChecked(true);
        this.setDefaultValue(true);
        this.setSummary(res.getString(R.string.allConfidentialSummary));
        this.setTitle(res.getString(R.string.allConfidential));
    }
}
