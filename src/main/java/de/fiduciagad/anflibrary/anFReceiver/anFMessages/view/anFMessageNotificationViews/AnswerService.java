package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import de.fiduciagad.anflibrary.anFConnector.NoFConnector;
import de.fiduciagad.anflibrary.anFConnector.anFInterfaces.ReceiverInterface;

/**
 * Created by Felix Schiefer on 30.01.2016.
 */
public class AnswerService extends IntentService {
    private static final String CLASS_NAME = AnswerService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AnswerService() {
        super(CLASS_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ReceiverInterface receiver = NoFConnector.getReceiver();
        Log.i(CLASS_NAME, "Hallo Welt");
        if (intent != null) {
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            if (remoteInput != null) {
                receiver.receiveMessage(remoteInput.getCharSequence(NotificationConstants.EXTRA_VOICE_REPLY).toString(), intent.getExtras().getInt(NotificationConstants.EXTRA_ID));
            }
        }
    }
}
