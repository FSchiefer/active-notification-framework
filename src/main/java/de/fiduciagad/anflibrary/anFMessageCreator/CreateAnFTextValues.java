package de.fiduciagad.anflibrary.anFMessageCreator;

import android.content.Context;

import org.json.JSONObject;

import de.fiduciagad.anflibrary.R;

/**
 * This class is used to set the default values that are needed for a anfmessage
 * in a jsonObject
 */
public class CreateAnFTextValues extends ValueCreator {

    private JSONObject mofText;

    public CreateAnFTextValues(Context context) {
        super(context);
        mofText = new JSONObject();
    }

    /**
     * This method is used to set the Title of a notification
     * @param value The string value that should be used for the title
     */
    public void setTitle(String value) {
        setValue(R.string.mofTitle, mofText, value);
    }

    /**
     * This method is used to set the message that should be displayed
     * in the smartphone notification or on small displays
     * @param value The string value of the shortmessage
     */
    public void setShortMessage(String value) {
        setValue(R.string.mofShortMessage, mofText, value);
    }

    /**
     * This method is used to set the message that should be displayed
     * when a user has tie to read them
     * @param value The string value of the message
     */
    public void setMessage(String value) {
        setValue(R.string.mofMessage, mofText, value);
    }

    //TODO: Make urgency and confidential mandatory
    /**
     * This method is used to set a message as urgent
     * @param value True if the message is urgent false if not
     */
    public void setUrgency(Boolean value) {
        setValue(R.string.urgency_value, mofText, value);
    }

    /**
     * This method is used to set a message as confidential
     * @param value True if the message is confidential false if not
     */
    public void setConfidential(Boolean value) {
        setValue(R.string.confidential_value, mofText, value);
    }

    @Override
    public JSONObject getJSONObject() {
        return mofText;
    }
}
