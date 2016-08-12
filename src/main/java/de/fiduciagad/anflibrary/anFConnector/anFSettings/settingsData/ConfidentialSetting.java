package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

import android.content.Context;
import android.content.res.Resources;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;

import de.fiduciagad.anflibrary.R;

/**
 * Created by Felix Schiefer on 06.01.2016.
 */
public class ConfidentialSetting extends CheckBoxPreference {
    public ConfidentialSetting(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ConfidentialSetting(Context context, String service) {
        super(context);
        setValues(service);
    }

    private void setValues(String service) {
        Resources res = getContext().getResources();

        this.setKey(service + res.getString(R.string.confidential_key));
        this.setChecked(true);
        this.setDefaultValue(true);
        this.setSummary(res.getString(R.string.useConfidential));
        this.setTitle(res.getString(R.string.confidential));
    }
}
