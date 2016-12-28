package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews;

import android.content.Context;
import android.util.Log;

import de.fiduciagad.anflibrary.anFConnector.AnFConnector;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;

/**
 * This Class is used to provide a Ribbon-Notification for position triggered messages
 * that are while a user is at a specified position
 */
public class LocationBasedNotification extends AnFNotificationCompat {
    private static String CLASS_NAME = LocationBasedNotification.class.getSimpleName();

    /**
     * @param context
     * @param anfMessage
     * @inheritDoc
     */
    public LocationBasedNotification(Context context, int id, ContextAnswer answer) {
        super(context, id, answer);

        Log.i(CLASS_NAME, "Service= " + service);

        this.setSmallIcon(AnFConnector.getAnFImages(context).getSmallIcon(service, NotificationType.LOCATION));
        this.setAutoCancel(false);
        if (AnFConnector.isSeperateByService()) {
            this.setGroup(anfMessage.getService() + "_" + NotificationType.LOCATION.toString());
        } else {
            this.setGroup(NotificationType.LOCATION.toString());
        }
    }
}
