package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import de.fiduciagad.anflibrary.anFConnector.AnFConnector;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.anFClicked.SummaryClickedService;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDAO;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDB;

import java.util.List;

/**
 * Created by Felix Schiefer on 31.01.2016.
 */
public class AnFNotificationGroupSummaryCompat extends NotificationCompat.Builder {

    private static final String CLASS_NAME = AnFNotificationGroupSummaryCompat.class.getSimpleName();

    /**
     * @param context
     * @inheritDoc
     */
    public AnFNotificationGroupSummaryCompat(Context context, Notification notification1, MessageDAO message) {
        super(context);

        MessageDB messageDB = new MessageDB(context);

        String service = message.getAnFMessageParts().getService();

        List<MessageDAO> unreadMessages = messageDB.getUnreadMessages(service);

        this.setAutoCancel(true);
        this.setSmallIcon(AnFConnector.getAnFImages(context).getSmallIcon(service, NotificationType.SHORT));
        this.setLargeIcon(AnFConnector.getAnFImages(context).getBigIcon(service, NotificationType.SHORT));
        this.setGroupSummary(true);
        this.setGroup(notification1.getGroup());

        NotificationCompat.InboxStyle inBoxStile = new NotificationCompat.InboxStyle();

        int i = 0;
        for (MessageDAO messageMessageDAO : unreadMessages) {
            if (i < 5) {
                Log.i(CLASS_NAME, messageMessageDAO.getAnFMessageParts().getAnFText().getTitle());
                inBoxStile.addLine(messageMessageDAO.getAnFMessageParts().getAnFText().getTitle());
            }
            i++;
        }

        Intent activityToOpen = new Intent(context, SummaryClickedService.class);

/*        Intent testActivity= new Intent(this, "android.magnet.com.anfnotificationsample.NotificationClickedActivity")*/
        PendingIntent reply = PendingIntent.getService(context, 0, activityToOpen, PendingIntent.FLAG_UPDATE_CURRENT);

        setContentIntent(reply);
        this.setContentText(i + " ungelesene Nachrichten");
        inBoxStile.setBigContentTitle(message.getAnFMessageParts().getService());
        inBoxStile.setSummaryText(i + " ungelesene Nachrichten");
        this.setContentTitle(message.getAnFMessageParts().getService());
        this.setStyle(inBoxStile);
    }
}
