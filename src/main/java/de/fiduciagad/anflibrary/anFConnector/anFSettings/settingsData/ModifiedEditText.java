package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by Felix Schiefer on 06.01.2016.
 */
public class ModifiedEditText extends EditTextPreference {
    public ModifiedEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ModifiedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ModifiedEditText(Context context) {
        super(context);
    }

    // According to ListPreference implementation
    @Override
    public CharSequence getSummary() {
        String text = getText();
        if (TextUtils.isEmpty(text)) {
            return getEditText().getHint();
        } else {
            CharSequence summary = super.getSummary();
            if (summary != null) {
                return String.format(summary.toString(), text);
            } else {
                return null;
            }
        }
    }
}
