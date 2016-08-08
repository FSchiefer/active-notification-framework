package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.deviceCheck;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.util.Log;
import android.view.Display;

/**
 * Created by Felix Schiefer on 13.01.2016.
 */
public class DeviceState {
    private static final String CLASS_NAME = DeviceState.class.getSimpleName();

    private Context mContext;

    public DeviceState(Context context) {
        mContext = context;
    }

    public boolean isDisplayRunning() {
        DisplayManager dm = (DisplayManager) mContext.getSystemService(Context.DISPLAY_SERVICE);
        for (Display display : dm.getDisplays()) {
            Log.i(CLASS_NAME, "Display: " + display.getName() + " DisplayManger has " + dm.getDisplays().length + " Displays");
            if (display.getState() != Display.STATE_OFF) {
                Log.i(CLASS_NAME, "Display is running");
                return true;
            }
        }
        return false;
    }
}
