package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.activityRecognition;

import android.content.Context;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver.ContextResolverInterfaces.ActivityInterface;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.googleApiHandling.ApiHandler;

import com.google.android.gms.location.ActivityRecognition;

/**
 * Klasse zum feststellen der Tätigkeit eines Nutzers
 */
public class ActivityApiHandling extends ApiHandler {
    private ActivityHandling handler;

    public ActivityApiHandling(Context context, ActivityInterface resolver) {
        super(context);

        buildGoogleApiClient(ActivityRecognition.API);
        handler = new ActivityHandling(mContext, gClient, resolver);

        setHandler(handler);
    }

    @Override
    public void disconnect() {

        if (!gClient.isConnected()) {
            Log.d(TAG, "Error while Disconnecting");
            return;
        }
        handler.disconnect();
        gClient.disconnect();
    }
}
