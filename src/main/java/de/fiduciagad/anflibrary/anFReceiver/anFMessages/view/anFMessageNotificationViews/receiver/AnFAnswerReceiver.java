package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.receiver;

import android.util.Log;

import de.fiduciagad.anflibrary.anFConnector.anFInterfaces.ReceiverInterface;

/**
 * Created by Felix Schiefer on 30.01.2016.
 */
public class AnFAnswerReceiver implements ReceiverInterface {

    private static final String CLASS_NAME = AnFAnswerReceiver.class.getSimpleName();

    @Override
    public void receiveMessage(String message, int ID) {
        Log.i(CLASS_NAME, message + " ID " + ID);
    }
}
