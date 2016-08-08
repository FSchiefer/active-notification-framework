package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextLevelEnum;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.AnFMessage;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.ActionAnswers;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.ViewConstants;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.anFClicked.NotificationClickedService;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDB;
import de.fiduciagad.noflibrary.R;
import de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData.VibrationPatterns;

import java.util.ArrayList;

/**
 * Created by Felix Schiefer on 17.12.2015.
 * Diese Klasse stellt die Basis für die Erstellung der Notifications dar.
 */
public class AnFNotificationCompat extends NotificationCompat.Builder {

    protected AnFMessage nofMessage;
    protected String service;
    private ContextAnswer answer;
    private NotificationCompat.WearableExtender extender;

    private Context context;
    private int id;
    private static final String CLASS_NAME = AnFNotificationCompat.class.getSimpleName();

    /**
     * @param context Der Anwendungskontext
     * @param id      Die Id der Notification wegen der benachrichtigt werden soll
     * @param answer  Der Kontextwert um die Signale passend anzupassen
     *                <p/>
     *                Der AnFNotificationCompat muss für Notifications die dargestellt werden eingebunden werden.
     *                Hier werden alle Typunabhängigen Aspekte wie beispielsweise Aktionen und Signale gesetzt.
     */
    public AnFNotificationCompat(Context context, int id, ContextAnswer answer) {
        super(context);
        this.context = context;
        this.answer = answer;
        this.id = id;

        this.setAutoCancel(true);

        MessageDB messageDB = new MessageDB(context);
        this.nofMessage = messageDB.getMessage(id);
        String name = nofMessage.getService();

        service = nofMessage.getService();
        extender = new NotificationCompat.WearableExtender();
        Intent activityToOpen = new Intent(context, NotificationClickedService.class);
        activityToOpen.putExtra(ViewConstants.ID_EXTRA, id);

        Log.d(CLASS_NAME, "Notification for Context Level: " + answer.getContextLevel().toString());

        PendingIntent reply = PendingIntent.getService(context, (int) System.currentTimeMillis(), activityToOpen, PendingIntent.FLAG_UPDATE_CURRENT);

        setContentIntent(reply);
        setSignal(context, name);

        setActions();

        extend(extender);

        setNotification(nofMessage);
    }

    /**
     * Diese Funktion wird verwendet um die relevanten Signale die eine Notification benutzen kann zu setzen.
     *
     * @param context Der Anwendungskontext der für die Erstellung von Android Oberflächenkomponenten benötigt wird
     * @param name    Der Name des Service um auf die relevanten Einstellungen zugreifen zu können.
     */
    private void setSignal(Context context, String name) {

        Log.i(CLASS_NAME, "AnFMessage is urgent " + nofMessage.isUrgent());
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        setVibration(settings, name);

        setSound();

        if (nofMessage.isUrgent()) {
            this.setPriority(NotificationCompat.PRIORITY_MAX);
        } else {
            this.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        this.setLights(Color.BLUE, 500, 500);
    }

    private void setSound() {
        if (answerEquals(ContextLevelEnum.S1) && nofMessage.isUrgent()) {
            Log.i(CLASS_NAME, "Sound is Set");
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            this.setSound(alarmSound);
        }
    }

    private void setVibration(SharedPreferences settings, String name) {
        // Hier wird überprüft, ob ein Handy einen Vibrationsalarm erlaubt. Da hier aktuell
        // noch Seiteneffekte auftreten wurde auf die Überprüfung verzichtet und die Problematik
        // in der Evaluierung beschrieben.
/*        DeviceSignals signals = new DeviceSignals(context);
        Log.i(CLASS_NAME, "Vibration on Phone " + signals.checkVibrationIsOn());*/

        long[] vibrationPattern = null;
        if (answerEquals(ContextLevelEnum.S3) || answerEquals(ContextLevelEnum.S4) || answerEquals(ContextLevelEnum.S0)) {
            vibrationPattern = new long[]{0, 0, 0};
            Log.i(CLASS_NAME, "No Vibration");
        } else {
            int pattern = Integer.parseInt(settings.getString(name + "_vibration", ""));
            VibrationPatterns patterns = new VibrationPatterns();
            if ((nofMessage.isUrgent() && answer.isWatchAvailable()) || answerEquals(ContextLevelEnum.S1)) {
                vibrationPattern = patterns.getVibrationPattern(pattern);
                Log.i(CLASS_NAME, "Vibration is Set");
            } else {
                vibrationPattern = new long[]{0, 0, 0};
                Log.i(CLASS_NAME, "No Vibration");
            }
        }

        if (vibrationPattern != null)
            this.setVibrate(vibrationPattern);
    }

    private boolean answerEquals(ContextLevelEnum levelEnum) {
        return answer.getContextLevel().equals(levelEnum);
    }

    private void setNotification(AnFMessage nofMessage) {
        this.setContentTitle(nofMessage.getAnFText().getTitle());
        if (nofMessage.isConfidential()) {
            this.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
            this.setContentText("Eine Nachricht des Service " + nofMessage.getService() + " ist angekommen");
        } else {
            this.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            this.setContentText(nofMessage.getAnFText().getMessage());
        }
    }

    protected void setActions() {
        ActionAnswers actions = nofMessage.getAnswers();
        if (actions != null) {
            setAction(actions);
        }
    }

    protected void setPaymentInformations() {
        if (nofMessage.getPaymentInformations() != null) {

        }
    }

    private void setAction(ActionAnswers action) {
        ArrayList<String> answers = action.getQuickAnswer();
        extender.addAction(createVoiceReplyAction(answers));
        if (action.getName() != null) {
            extender.addAction(call(action));
        }
    }

    private NotificationCompat.Action createVoiceReplyAction(ArrayList<String> answers) {

        Intent sendIntent = new Intent(context, AnswerService.class);
        sendIntent.putExtra(NotificationConstants.EXTRA_ID, id);

/*        AnswerService activityToOpen = new Intent(this,AnswerService.class);*/
        PendingIntent reply = PendingIntent.getService(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String replyLabel = "Antwort";
        String[] choices = new String[answers.size()];
        choices = answers.toArray(choices);
        RemoteInput remoteInput = new RemoteInput.Builder(NotificationConstants.EXTRA_VOICE_REPLY).setLabel(replyLabel).setChoices(choices).build();
        return new NotificationCompat.Action.Builder(R.mipmap.mail_icon, "Schnell Antwort", reply).addRemoteInput(remoteInput).build();
    }

    private NotificationCompat.Action call(ActionAnswers actionAnswers) {

        Intent sendIntent = new Intent(context, CallService.class);
        sendIntent.putExtra(ViewConstants.NUMBER_EXTRA, actionAnswers.getNumber());
        String replyLabel = actionAnswers.getName() + " Anrufen";
        PendingIntent reply = PendingIntent.getService(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action.Builder(android.R.drawable.sym_action_call, replyLabel, reply).build();
    }
}
