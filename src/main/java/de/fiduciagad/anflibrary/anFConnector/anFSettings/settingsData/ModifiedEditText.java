package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * This class is used to create a modified EditTextPreference to see the selection of a User
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

    /**
     * This method is mad to provide the correct summary
     *
     * @return The modified CharSequence
     */
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
