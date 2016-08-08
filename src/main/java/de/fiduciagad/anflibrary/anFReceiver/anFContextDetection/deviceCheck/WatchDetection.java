package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.deviceCheck;

import android.content.Context;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver.ContextResolverInterfaces.WatchDetectionInterface;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

/**
 * Created by Felix Schiefer on 23.01.2016.
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
        gClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();

        gClient.connect();

        List<Node> connectedNodes = Wearable.NodeApi.getConnectedNodes(gClient).await().getNodes();
        for (Node node : connectedNodes) {
            Log.d(CLASS_NAME, "Display Name " + node.getDisplayName() + " Nearby " + node.isNearby());
            BluetoothDevices devices = new BluetoothDevices();
            if (devices.getAvailableSmartwatches().contains(node.getDisplayName())) {
                resolver.setWatchAvailable(true);
            } else {
                resolver.setWatchAvailable(false);
            }
        }

        gClient.disconnect();
    }
}
