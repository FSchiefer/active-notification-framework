package de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Felix Schiefer on 15.02.2016.
 */
public class LocationCheckRunnable implements Runnable {
    private static String CLASS_NAME = LocationCheckRunnable.class.getSimpleName();
    private Context context;

    public LocationCheckRunnable(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Log.i(CLASS_NAME, "I'm Waiting");
        SystemClock.sleep(ControllerConstants.GEOFENCE_WAIT_TIME);
        Log.i(CLASS_NAME, "I've waited");
        Intent geofenceTrigger = new Intent(context, GeofenceTriggerService.class);
        context.startService(geofenceTrigger);
    }
}
