package de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationControl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger.InstantMessageTriggerService;

/**
 * Created by Felix Schiefer on 25.01.2016.
 */
public class PowerConnectedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

        Intent notificationController = new Intent(context, InstantMessageTriggerService.class);
        context.startService(notificationController);
    }
}
