package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.deviceCheck;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by Felix Schiefer on 07.01.2016.
 */
public class BatteryRequestCheck {

    private Intent batteryStatus;

    public BatteryRequestCheck(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = context.registerReceiver(null, ifilter);
    }

    public boolean getChargingBoolean() {
        return batteryStatus.getBooleanExtra(BatteryManager.ACTION_CHARGING, true);
    }

    public int getBatteryCapacity() {
        return batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    }
}
