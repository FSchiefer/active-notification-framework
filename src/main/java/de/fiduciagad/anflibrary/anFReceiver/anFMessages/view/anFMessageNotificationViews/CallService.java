package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.ViewConstants;

/**
 * Created by Felix Schiefer on 05.02.2016.
 */
public class CallService extends IntentService {
    private static final String CLASS_NAME = CallService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CallService() {
        super(CLASS_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String number = intent.getExtras().getString(ViewConstants.NUMBER_EXTRA);
        Intent sendIntent = new Intent(Intent.ACTION_DIAL);
        sendIntent.setData(Uri.parse("tel:" + number));
        startActivity(sendIntent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
