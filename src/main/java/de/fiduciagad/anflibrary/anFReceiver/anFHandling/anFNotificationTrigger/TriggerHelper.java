package de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import de.fiduciagad.anflibrary.anFConnector.AnFConnector;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver.ContextResolver;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextLevelEnum;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.AnFMessage;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.NotificationType;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDAO;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDB;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFServiceHandling.ServiceDB;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.AnFNotificationGroupSummaryCompat;

import java.util.List;

/**
 * Created by Felix Schiefer on 26.01.2016.
 */
class TriggerHelper {
    private ContextResolver resolver;
    private ContextAnswer answer;

    public TriggerHelper(ContextResolver resolver, ContextAnswer answer) {
        this.resolver = resolver;
        this.answer = answer;
    }

    public boolean onlyUrgentMessages() {
        return answer.getContextLevel().equals(ContextLevelEnum.S5) || answer.getContextLevel().equals(ContextLevelEnum.S3) || !resolver.batteryStatusOk();
    }

    public boolean messagesHadToWait() {
        return (onlyUrgentMessages() || answer.getContextLevel().equals(ContextLevelEnum.S4));
    }

    public void sendMessages(List<MessageDAO> messages, Context context) {
        if (messages.size() != 0) {

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            for (MessageDAO message : messages) {
           /* if (answer.getContextLevel() < message.getAnFMessageParts().getMessageImportance()) {*/

                int messageID = (int) message.getId();
                AnFMessage anFMessage = message.getAnFMessage();
                NotificationCompat.Builder anfNotification = anFMessage.getNotificationCompat(answer, messageID);
                Notification notification1 = anfNotification.build();
                notificationManager.notify(messageID, notification1);

                MessageDB messageDB = new MessageDB(context);
                message.messageSent();
                List<MessageDAO> unreadMessages = messageDB.getUnreadMessages(message.getAnFMessageParts().getService());
                if (unreadMessages.size() > 1 && !notification1.getGroup().equals(NotificationType.LOCATION.toString())) {
                    showGroupSummary(notificationManager, notification1, message, context);
                }
            }
        }
    }

    private void showGroupSummary(NotificationManagerCompat notificationManager, Notification notification1, MessageDAO message, Context context) {
        AnFNotificationGroupSummaryCompat summaryCompat = new AnFNotificationGroupSummaryCompat(context, notification1, message);
        Notification notification = summaryCompat.build();
        ServiceDB services = new ServiceDB(context);
        List<String> serviceList = services.getServiceList();
        int serviceId = serviceList.indexOf(message.getAnFMessageParts().getService());
        if (serviceId == 0) {
            serviceId = serviceList.size();
        }
        int notificationID = 1000000;
        if (AnFConnector.isSeperateByService()) {
            notificationID = notificationID * serviceId;
        }
        notificationManager.notify(notificationID, notification);
    }
}
