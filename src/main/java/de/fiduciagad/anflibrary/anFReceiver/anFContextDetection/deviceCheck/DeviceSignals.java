package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.deviceCheck;

import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;

/**
 * Created by Felix Schiefer on 16.01.2016.
 */
public class DeviceSignals {
    private Context context;

    public DeviceSignals(Context context) {
        this.context = context;
    }

    public boolean checkVibrationIsOn() {
        boolean status = false;
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
            status = true;
        } else if (1 == Settings.System.getInt(context.getContentResolver(), "vibrate_when_ringing", 0)) //vibrate on
            status = true;
        return status;
    }
}
