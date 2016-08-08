package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews;

import android.app.Notification;
import android.content.Context;

import de.fiduciagad.anflibrary.anFConnector.NoFConnector;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;

/**
 * Created by Felix Schiefer on 04.11.2015.
 */
public class ShortMessageNotification extends AnFNotificationCompat {

    /**
     * Constructor.
     * <p/>
     * Automatically sets the when field to {@link System#currentTimeMillis()
     * System.currentTimeMillis()} and the audio stream to the
     * {@link Notification#STREAM_DEFAULT}.
     *
     * @param context A {@link Context} that will be used to construct the
     *                RemoteViews. The Context will not be held past the lifetime of this
     *                Builder object.
     */
    public ShortMessageNotification(Context context, int id, ContextAnswer answer) {
        super(context, id, answer);

        this.setSmallIcon(NoFConnector.getNoFImages(context).getSmallIcon(service, NotificationType.SHORT));

        if (NoFConnector.isSeperateByService()) {
            this.setGroup(nofMessage.getService() + "_" + NotificationType.SHORT.toString());
        } else {
            this.setGroup(NotificationType.SHORT.toString());
        }
    }
}
