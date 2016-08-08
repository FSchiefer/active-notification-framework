package de.fiduciagad.anflibrary.anFConnector;

import android.content.Context;
import android.content.Intent;

import de.fiduciagad.anflibrary.anFConnector.anFInterfaces.AnFImagesInterface;
import de.fiduciagad.anflibrary.anFConnector.anFMessageList.MessageListActivity;
import de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger.InstantMessageTriggerService;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.images.AnFImages;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.receiver.AnFAnswerReceiver;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageViews.MessageActivityBuilder.MessageBuilder;
import de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger.GeofenceTriggerService;
import de.fiduciagad.anflibrary.anFConnector.anFInterfaces.MessageBuilderInterface;
import de.fiduciagad.anflibrary.anFConnector.anFInterfaces.ReceiverInterface;

/**
 * This class is used to link behavior of the framework with an own application

 */
public class NoFConnector {
    private static ReceiverInterface receiver;

    /**
     *
     * With this function the framework uses the instance of the ReceiverInterface
     *
     * @return Object which implements the ReceiverInterface
     * <p/>
     */
    public static ReceiverInterface getReceiver() {
        if (receiver == null) {
            return new AnFAnswerReceiver();
        }
        return receiver;
    }

    /**
     * With this function the application can set an alternative receiver for Answers from a Notification
     *
     * @param receiverInterface An instance of the ReceiverInterface
     */
    public static void setReceiver(ReceiverInterface receiverInterface) {
        receiver = receiverInterface;
    }

    private static MessageBuilderInterface messageBuilder;

    /**
     * With this function an Activity is started after a click on the Notification
     *
     * @return An intent for an Activity
     */
    public static MessageBuilderInterface getMessageActivity() {
        if (messageBuilder == null) {
            return new MessageBuilder();
        }
        return messageBuilder;
    }

    /**
     * With this function an alternative Activity which should be startet after a click on the
     * notification can be set by the developer
     *
     * @param messageBuilderInterface An instance of the ReceiverInterface
     */
    public static void setMessageActivity(MessageBuilderInterface messageBuilderInterface) {
        messageBuilder = messageBuilderInterface;
    }

    private static Intent messageSummary;

    /**
     * With this function an Instance of the SummaryActivity is started after a click
     * Über diese Funktion wird die passende Instanz der SummaryActivity geöffnet nach einem Klick auf diese
     *
     * @param context The application Context
     * @return An activity for the representation if multiple messages of a Service arrived
     */
    public static Intent getSummaryActivity(Context context) {
        if (messageSummary == null) {
            return new Intent(context, MessageListActivity.class);
        }
        return messageSummary;
    }

    /**
     * With this function an own SummaryActivity can be set by the application
     *
     * @param intent An intent which should be startet as Activity after a click on the SummaryNotification
     */
    public static void setSummaryActivity(Intent intent) {
        messageSummary = intent;
    }

    private static boolean seperateByService;

    /**
     * This function is used to check if Summary Notification should be made for different
     * types of messages or services
      *
     * @return Boolean if stacking depends on message type or service
          */
    public static boolean isSeperateByService() {

        return seperateByService;
    }

    /**
     * With this function the stacking behaviour of the Notifications can be defined
     * Über diese Funktion kann für das Framework das Stackingverhalten der Notifications gesetzt werden
     *
     * @param seperateByService True if all messages of a service should be stacked
     */
    public static void setSeperateByService(Boolean seperateByService) {
        seperateByService = seperateByService;
    }

    private static AnFImagesInterface anFImagesInterface;

    /**
     * With this function the Framework receives the appropriate icons for messages
     *
     * @param context The current application context
     * @return The current Instance of the AnFImagesInterface
     */
    public static AnFImagesInterface getNoFImages(Context context) {
        if (anFImagesInterface == null) {
            return new AnFImages(context);
        }
        return anFImagesInterface;
    }

    /**
     * With this function an own Instance for the AnFImagesInterface can be set
     *
     * @param images A AnFImagesInterface to use own Icons for the Notifications
     */
    public static void setNoFImagesInterface(AnFImagesInterface images) {
        anFImagesInterface = images;
    }

    /**
     *  With this function a app developder can resend all Stored and active notifications of the Framework
     *
     * @param context The current application context
     */
    public static void refreshMessageReceiver(Context context) {
        Intent notificationController = new Intent(context, InstantMessageTriggerService.class);
        context.startService(notificationController);

        Intent geofenceTrigger = new Intent(context, GeofenceTriggerService.class);
        context.startService(geofenceTrigger);
    }
}
