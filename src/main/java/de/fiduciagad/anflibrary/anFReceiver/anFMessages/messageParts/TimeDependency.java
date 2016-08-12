package de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts;

import android.content.Context;

import org.json.JSONObject;

/**
 * Nachrichtenelement um festzustellen ob eine Nachricht eine Zeitabhängigkeit hat.
 * Wird für den Prototyp des Frameworks noch nicht implementiert
 */
public class TimeDependency extends MessagePart {

    public TimeDependency(JSONObject anfObject, Context context) {
        super(anfObject, context);
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
