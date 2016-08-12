package de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationControl.GeofenceHandling;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDB;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix Schiefer on 15.02.2016.
 */
public class GeofenceTriggerService extends IntentService {
    private static String CLASS_NAME = GeofenceTriggerService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public GeofenceTriggerService() {
        super(CLASS_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofenceHandling handling = GeofenceHandling.getInstance(this);
        if (!handling.refreshGeofences()) {
            Log.i(CLASS_NAME, "No Available Geofences");
            MessageDB messageDB = new MessageDB(this);

            List<MessageDAO> messages = messageDB.getPositionDependentMessages();
            List<MessageDAO> permanentFences = new ArrayList<>();
            for (MessageDAO message : messages) {
                if (message.getAnFMessageParts().getPositionDependency().getDuration() < 1) {
                    permanentFences.add(message);
                }
            }
            if (permanentFences.size() > 0) {
                for (MessageDAO message : permanentFences) {
                    handling.addMessageToGeofenceList(message.getAnFMessageParts(), message.getId());
                }
            }
        }

        handling.connect();
    }
}
