package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.googleApiHandling;

import android.content.BroadcastReceiver;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Felix Schiefer on 05.01.2016.
 */
public abstract class GoogleApiHandling {

    protected Context mContext;
    protected GoogleApiClient gClient;
    protected BroadcastReceiver mReceiver;

    protected static final String TAG = GoogleApiHandling.class.getSimpleName();

    public GoogleApiHandling(Context context, GoogleApiClient gClient) {
        this.mContext = context;
        this.gClient = gClient;
    }

    public abstract void connectHandling();
}
