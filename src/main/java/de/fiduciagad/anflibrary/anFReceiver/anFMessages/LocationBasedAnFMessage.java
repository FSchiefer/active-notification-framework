package de.fiduciagad.anflibrary.anFReceiver.anFMessages;

import android.content.Context;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.AnFText;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.PositionDependency;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.AnFNotificationCompat;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.LocationBasedNotification;

import org.json.JSONObject;

/**
 * Created by Felix Schiefer on 03.01.2016.
 */
public class LocationBasedAnFMessage extends AnFMessage {
    public LocationBasedAnFMessage(Context context, JSONObject anfPayload, AnFText anFText, PositionDependency positionDependency) {
        super(context, anfPayload, anFText, positionDependency);
    }

    @Override
    public AnFNotificationCompat getNotificationCompat(ContextAnswer answer, int id) {
        AnFNotificationCompat compat = new LocationBasedNotification(context, id, answer);
        return compat;
    }
}
