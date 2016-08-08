package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

import android.content.Context;
import android.content.res.Resources;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;

import de.fiduciagad.noflibrary.R;

/**
 * Created by Felix Schiefer on 06.01.2016.
 */
public class AllUrgentSetting extends CheckBoxPreference {
    public AllUrgentSetting(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AllUrgentSetting(Context context, String service) {
        super(context);
        setValues(service);
    }

    private void setValues(String service) {
        Resources res = getContext().getResources();

        this.setKey(service + res.getString(R.string.allUrgent_key));
        this.setChecked(false);
        this.setDefaultValue(false);
        this.setSummary(res.getString(R.string.allUrgentSummary));
        this.setTitle(res.getString(R.string.allUrgent));
    }
}
