package de.fiduciagad.anflibrary.anFMessageCreator;

import android.content.Context;

import org.json.JSONObject;

import de.fiduciagad.anflibrary.R;

//TODO FS add javadoc
public class CreateAnFTextValues extends ValueCreator {

    private JSONObject mofText;

    public CreateAnFTextValues(Context context) {
        super(context);
        mofText = new JSONObject();
    }

    public void setTitle(String value) {
        setValue(R.string.mofTitle, mofText, value);
    }

    public void setShortMessage(String value) {
        setValue(R.string.mofShortMessage, mofText, value);
    }

    public void setMessage(String value) {
        setValue(R.string.mofMessage, mofText, value);
    }

    public void setUrgency(Boolean value) {
        setValue(R.string.urgency_value, mofText, value);
    }

    public void setConfidential(Boolean value) {
        setValue(R.string.confidential_value, mofText, value);
    }

    @Override
    public JSONObject getJSONObject() {
        return mofText;
    }
}
