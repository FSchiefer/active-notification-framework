package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.googleApiHandling;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Felix Schiefer on 08.01.2016.
 */
public class ApiHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected GoogleApiClient gClient;
    protected Context mContext;

    protected static final String TAG = "ApiHandler";
    GoogleApiHandling handler;

    public ApiHandler(Context context) {
        mContext = context;
    }

    public void setHandler(GoogleApiHandling handler) {
        this.handler = handler;
    }

    public void connect() {
        gClient.connect();
    }

    public void disconnect() {

        if (!gClient.isConnected()) {

            return;
        }
        gClient.disconnect();
    }

    protected synchronized void buildGoogleApiClient(Api api) {
        gClient = new GoogleApiClient.Builder(mContext).addApi(api).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        handler.connectHandling();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        gClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = ");
    }
}
