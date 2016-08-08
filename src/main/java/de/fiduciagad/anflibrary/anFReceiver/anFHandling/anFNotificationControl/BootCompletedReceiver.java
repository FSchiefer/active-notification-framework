package de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationControl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Benachrichtigung zusammenbauen

         /*   Intent notificationController = new Intent(context, InstantMessageTriggerService.class);
            context.startService(notificationController);

            Intent geofenceTrigger = new Intent(context, GeofenceTriggerService.class);
            context.startService(geofenceTrigger);*/

        }
    }
}
