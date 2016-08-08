package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.locationAwareness;

import android.content.Context;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver.ContextResolver;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.googleApiHandling.ApiHandler;

import com.google.android.gms.location.LocationServices;

/**
 * Created by Felix Schiefer on 08.01.2016.
 */
public class PositionApiHandling extends ApiHandler {

    public PositionApiHandling(Context context, ContextResolver resolver) {
        super(context);

        buildGoogleApiClient(LocationServices.API);

        setHandler(new LocationHandling(mContext, gClient, resolver));
    }
}
