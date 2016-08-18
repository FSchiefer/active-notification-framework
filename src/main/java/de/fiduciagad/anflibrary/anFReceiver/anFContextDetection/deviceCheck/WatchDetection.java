package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.deviceCheck;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver.ContextResolverInterfaces.WatchDetectionInterface;

/**
 * Created by Felix Schiefer on 23.01.2016.
 * This class is used to detect if a smartwatch is connected with the smartphone or not
 */
public class WatchDetection implements Runnable {
    Context context;
    private GoogleApiClient gClient;
    private WatchDetectionInterface resolver;
    private static final String CLASS_NAME = WatchDetection.class.getSimpleName();

    public WatchDetection(Context context, WatchDetectionInterface resolver) {
        this.context = context;
        this.resolver = resolver;
    }

    @Override
    public void run() {
        //TODO: Find a way to enable watch detection again. At the moment the code stops at: Wearable.NodeApi.getConnectedNodes(gClient).await().getNodes()
/*        gClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();

        gClient.connect();

        List<Node> connectedNodes = Wearable.NodeApi.getConnectedNodes(gClient).await().getNodes();
        Log.d(CLASS_NAME, "connected nodes" + connectedNodes.size());
        for (Node node : connectedNodes) {
            Log.d(CLASS_NAME, "Display Name " + node.getDisplayName() + " Nearby " + node.isNearby());
            BluetoothDevices devices = new BluetoothDevices();
            if (devices.getAvailableSmartwatches().contains(node.getDisplayName())) {
                resolver.setWatchAvailable(true);
            } else {
                resolver.setWatchAvailable(false);
            }
        }

        gClient.disconnect();*/
        // While API isn't working this class is used
        BluetoothDevices devices = new BluetoothDevices();
        resolver.setWatchAvailable(devices.isSmartwatchAvailable());
    }
}
