package de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver.ContextResolver;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextInterface;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDAO;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDB;

/**
 * Created by Felix Schiefer on 10.01.2016.
 */
public class LocationMessageTriggerService extends IntentService implements ContextInterface {

    protected static final String TAG = LocationMessageTriggerService.class.getSimpleName();

    private Context mActivity;
    private MessageDB messageDB;

    private ArrayList<String> triggeringGeofencesIdsList;

    private ContextResolver contextResolver;

    public LocationMessageTriggerService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Handles incoming intents.
     *
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        mActivity = getApplicationContext();
        messageDB = new MessageDB(mActivity);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Geofencing Event has Error");
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

        String geofenceTransitionDetails = getGeofenceTransitionDetails(this, geofenceTransition, triggeringGeofences);
        Log.i(TAG, geofenceTransitionDetails);

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            sendNotification();
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            removeNotification();
        } else {
            Log.e(TAG, "Other error occured");
        }
    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param context             The app context.
     * @param geofenceTransition  The ID of the geofence transition.
     * @param triggeringGeofences The geofence(s) triggered.
     * @return The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(Context context, int geofenceTransition, List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            String id = getID(geofence.getRequestId());
            Log.i(TAG, geofence.getRequestId());
            triggeringGeofencesIdsList.add(id);
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    /**
     * Sends message to the context detection.
     */
    private void sendNotification() {
        Log.d(TAG, "Service Started");

        if (contextResolver == null) {
            contextResolver = ContextResolver.getInstance(mActivity);
        }

        int messageCount = contextResolver.batteryStatusOk() ? messageDB.getMessagesById(triggeringGeofencesIdsList).size() : messageDB.getUrgentMessagesById(triggeringGeofencesIdsList).size();

        if (messageCount > 0) {
            Log.i(TAG, "Service Started, messageCount>0");
            contextResolver.getContext(this);
        } else {
            Log.i(TAG, "No service needed because Battery to low");
        }
    }

    /**
     * This method is used to remove notifications when a user is leaving the trigger area
     */
    private void removeNotification() {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mActivity);

        for (String id : triggeringGeofencesIdsList) {
            notificationManager.cancel(Integer.parseInt(id));
        }
    }

    private String getID(String id) {
        return id.substring(id.indexOf(":") + 1, id.indexOf(";"));
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType A transition service constant defined in Geofence
     * @return A String indicating the service of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "Entered";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "Exited";
            default:
                return "Transition";
        }
    }

    @Override
    public void setContext(ContextAnswer answer) {
        Log.d(TAG, "Context Set");

        List<MessageDAO> messages = null;
        TriggerHelper helper = new TriggerHelper(contextResolver, answer);
        Log.i(TAG, " Number of Triggering Fences: " + triggeringGeofencesIdsList.size());
        if (helper.onlyUrgentMessages()) {
            messages = messageDB.getUrgentMessagesById(triggeringGeofencesIdsList);
        } else {
            messages = messageDB.getMessagesById(triggeringGeofencesIdsList);
        }
        Log.i(TAG, " Number of Messages: " + messages.size());
        helper.sendMessages(messages, this);

        if (helper.messagesHadToWait()) {
            Log.i(TAG, " Messages had to Wait");
            LocationCheckRunnable locationCheckRunnable = new LocationCheckRunnable(this);
            Thread t = new Thread(locationCheckRunnable);
            t.start();
        }
    }
}
