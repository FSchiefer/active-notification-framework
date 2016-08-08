package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.deviceCheck;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Felix Schiefer on 08.01.2016.
 */
public class BluetoothDevices {

    private boolean smartwatchAvailable;
    private List<String> availableSmartwatches;

    private static final String CLASS_NAME = BluetoothDevices.class.getSimpleName();

    public BluetoothDevices() {
        availableSmartwatches = new ArrayList<>();
    }

    private void getConnectedDevices() {

        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {

            // TODO Für mehr Geräte erweitern wie Android Auto
            for (BluetoothDevice device : pairedDevices) {
                if (device.getBluetoothClass().getDeviceClass() == BluetoothClass.Device.WEARABLE_WRIST_WATCH) {
                    availableSmartwatches.add(device.getName());
                    smartwatchAvailable = true;
                }
                Log.d(CLASS_NAME, "Paired device: " + device.getName() + ", with address: " + device.getAddress() + " class " + device.getBluetoothClass().getDeviceClass() + " service ");
            }
        } else {
            Log.d(CLASS_NAME, "No paired Device Found");
        }
    }

    public boolean isSmartwatchAvailable() {
        getConnectedDevices();
        return smartwatchAvailable;
    }

    public List<String> getAvailableSmartwatches() {
        getConnectedDevices();
        return availableSmartwatches;
    }
}
