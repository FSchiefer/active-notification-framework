package de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts;

import android.content.Context;

import de.fiduciagad.anflibrary.R;

import org.json.JSONObject;

/**
 * Klasse für die Pflichtelemente einer Nachricht wie :
 * Titel, Kurznachricht, dringlichkeit und vertraulichkeit
 */
public class AnFText extends MessagePart {
    private String shortMessage;
    private String title;
    private String message;
    private Boolean urgency;
    private Boolean confidential;

    public AnFText(JSONObject anfObject, Context context) {
        super(anfObject, context);
        confidential = false;
        urgency = false;

        title = getJSONString(R.string.mofTitle, anfObject);
        shortMessage = getJSONString(R.string.mofShortMessage, anfObject);
 /*       message = getJSONString(R.string.anfMessage, anfObject);*/
        urgency = getJSONBoolean(R.string.urgency_value, anfObject);
        confidential = getJSONBoolean(R.string.confidential_value, anfObject);
        if (message == null) {
            message = shortMessage;
        }
    }

    public boolean isValid() {

        if (title != null && shortMessage != null) {

            return true;
        }
        return false;
    }

    public String getMessage() {
        return message;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getUrgency() {
        return urgency;
    }

    public Boolean getConfidential() {
        return confidential;
    }
}
