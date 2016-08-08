package de.fiduciagad.anflibrary.anFConnector;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import de.fiduciagad.anflibrary.anFMessageCreator.CreateNoFMessage;
import de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationControl.GeofenceHandling;
import de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger.InstantMessageTriggerService;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.AnFMessage;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDAO;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDB;
import de.fiduciagad.noflibrary.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * NoF-Messages which should be used by the Framework need to be given to the functions
 * in this class
 */
public class NoFController {

    private Activity mActivity;

    private MessageDB messageDB;

    private static final String CLASS_NAME = NoFController.class.getSimpleName();

    public NoFController(Activity mActivity) {
        this.mActivity = mActivity;
        PreferenceManager.setDefaultValues(mActivity, R.xml.contextpreferences, false);
        messageDB = new MessageDB(mActivity);
    }

    /**
     * @param payload The NoF-message as string for the conversion in an JSON-Object
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
     * @param anFMessage A NoF-message which is created with the CreateNoFMessage class
     * @return Boolean which says if the given message is valid
     */
    public boolean handleMessage(CreateNoFMessage anFMessage) {

        return handleMessage(anFMessage.getJSONObject());
    }

    /**
     * @param message The NoF-Message in a Boolean representation
     * @return Boolean which says if the given message is valid
     */
    public boolean handleMessage(JSONObject message) {

        MessageDAO messageDAO = new MessageDAO(mActivity);
        Log.i(CLASS_NAME, "M is " + message.toString());
        long starttime = System.currentTimeMillis();
        AnFMessage m = AnFMessage.getMessage(mActivity, message);
        Log.i(CLASS_NAME, "Duratation: " + (System.currentTimeMillis() - starttime));
        if (m != null && m.isValid()) {
            Intent notificationController = new Intent(mActivity, InstantMessageTriggerService.class);
            Log.i(CLASS_NAME, "M is " + m.isValid() + " Service " + m.getService());

            messageDAO.insert(m);

            if (m.getPositiondependency() != null) {
                if (m.getPositiondependency().isTrigger()) {
                    GeofenceHandling handling = GeofenceHandling.getInstance(mActivity);
                    handling.addMessageToGeofenceList(m.getMessageParts(), messageDAO.getId());
                    handling.connect();
                    return true;
                }
            }

            mActivity.startService(notificationController);

            return true;
        } else {
            Log.e(CLASS_NAME, "Payload is no NoF Object");
        }

        return false;
    }
}
