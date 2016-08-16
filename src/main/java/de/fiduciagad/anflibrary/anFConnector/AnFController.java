package de.fiduciagad.anflibrary.anFConnector;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.fiduciagad.anflibrary.R;
import de.fiduciagad.anflibrary.anFMessageCreator.CreateAnFMessage;
import de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationControl.GeofenceHandling;
import de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger.InstantMessageTriggerService;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.AnFMessage;
import de.fiduciagad.anflibrary.anFReceiver.anFPermissions.CheckPositionPermissions;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDAO;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDB;

/**
 * AnF-Messages which should be used by the Framework need to be given to the functions
 * in this class
 */
public class AnFController {

    private Activity mActivity;

    private MessageDB messageDB;


    private static final String CLASS_NAME = AnFController.class.getSimpleName();

    public AnFController(Activity mActivity) {
        this.mActivity = mActivity;
        PreferenceManager.setDefaultValues(mActivity, R.xml.contextpreferences, false);
        messageDB = new MessageDB(mActivity);
    }

    /**
     * @param payload The AnF-message as string for the conversion in an JSON-Object
     * @return Boolean which says if the given message is valid
     */
    public boolean handleMessage(String payload) {

        JSONObject jsonPayload = null;

        try {
            jsonPayload = new JSONObject(payload);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return handleMessage(jsonPayload);
    }

    /**
     * @param anFMessage A AnF-message which is created with the CreateAnFMessage class
     * @return Boolean which says if the given message is valid
     */
    public boolean handleMessage(CreateAnFMessage anFMessage) {

        return handleMessage(anFMessage.getJSONObject());
    }

    /**
     * @param message The AnF-Message in a JSONObject representation
     * @return Boolean which says if the given message is valid
     */
    public boolean handleMessage(JSONObject message) {

        MessageDAO messageDAO = new MessageDAO(mActivity);
        Log.i(CLASS_NAME, "M is " + message.toString());
        long starttime = System.currentTimeMillis();
        AnFMessage m = AnFMessage.getMessage(mActivity, message);
        Log.i(CLASS_NAME, "Duration: " + (System.currentTimeMillis() - starttime));
        if (m != null && m.isValid()) {
            Intent notificationController = new Intent(mActivity, InstantMessageTriggerService.class);
            Log.i(CLASS_NAME, "M is " + m.isValid() + " Service " + m.getService());

            messageDAO.insert(m);
            Boolean x = addPositionDependency(messageDAO, m);
            if (x != null)
                return x;

            mActivity.startService(notificationController);

            return true;
        } else {
            Log.e(CLASS_NAME, "Payload is no AnF Object");
        }

        return false;
    }

    /**
     * This method is used to add Messages with position dependencies to a geofence and request the granted permissions
     * @param messageDAO
     * @param m
     * @return true if geofences can be added to the framework fal
     */
    @Nullable
    private Boolean addPositionDependency(MessageDAO messageDAO, AnFMessage m) {
        if (m.getPositiondependency() != null) {
            CheckPositionPermissions positionPermissions = new CheckPositionPermissions();
            if (m.getPositiondependency().isTrigger()) {
                if (positionPermissions.permissionRequestProvided(mActivity)) {
                    Log.e(CLASS_NAME, "Location detection permission not given, send messages again");
                    messageDAO.deleteAnFMessage();
                    return false;
                } else {
                    Log.d(CLASS_NAME, "FineLocationPermissionGranted");
                    GeofenceHandling handling = GeofenceHandling.getInstance(mActivity);
                    handling.addMessageToGeofenceList(m.getMessageParts(), messageDAO.getId());
                    handling.connect();
                    return true;
                }
            }
        }
        return null;
    }
}
