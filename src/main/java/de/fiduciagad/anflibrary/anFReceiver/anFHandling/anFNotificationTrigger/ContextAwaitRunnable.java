package de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by Felix Schiefer on 23.01.2016.
 */
class ContextAwaitRunnable implements Runnable {
    private Context context;

    public ContextAwaitRunnable(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        SystemClock.sleep(ControllerConstants.MESSAGE_WAIT_TIME);
        Intent notificationController = new Intent(context, InstantMessageTriggerService.class);
        context.startService(notificationController);
    }
}
