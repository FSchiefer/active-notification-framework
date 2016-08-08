package de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts;

import android.content.Context;

import de.fiduciagad.noflibrary.R;

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

    public AnFText(JSONObject nofObject, Context context) {
        super(nofObject, context);
        confidential = false;
        urgency = false;

        title = getJSONString(R.string.mofTitle, nofObject);
        shortMessage = getJSONString(R.string.mofShortMessage, nofObject);
 /*       message = getJSONString(R.string.anfMessage, nofObject);*/
        urgency = getJSONBoolean(R.string.urgency_value, nofObject);
        confidential = getJSONBoolean(R.string.confidential_value, nofObject);
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
