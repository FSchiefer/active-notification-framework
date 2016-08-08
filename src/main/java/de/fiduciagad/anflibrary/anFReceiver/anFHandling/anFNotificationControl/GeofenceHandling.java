package de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationControl;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.MessageParts;
import de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger.LocationMessageTriggerService;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.PositionDependency;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Felix Schiefer on 10.01.2016.
 */

public class GeofenceHandling implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    protected static final String TAG = GeofenceHandling.class.getSimpleName();

    protected GoogleApiClient mGoogleApiClient;

    protected ArrayList<Geofence> mGeofenceList;

    private ArrayList<MessageParts> messageList;

    private boolean mGeofencesAdded;

    private PendingIntent mGeofencePendingIntent;

    private SharedPreferences mSharedPreferences;

    Context context;

    private static GeofenceHandling instance;
    // Verhindere die Erzeugung des Objektes über andere Methoden

    // Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein konkretes
    // Objekt erzeugt und dieses zurückliefert.
    public static GeofenceHandling getInstance(Context context) {
        if (GeofenceHandling.instance == null) {
            GeofenceHandling.instance = new GeofenceHandling(context);
        }
        return GeofenceHandling.instance;
    }

    public GeofenceHandling(Context context) {

        this.context = context;
        mGeofenceList = new ArrayList<>();
        messageList = new ArrayList<>();

        buildGoogleApiClient();
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        if (!mGoogleApiClient.isConnected()) {
            return;
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent()).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
        mGoogleApiClient.disconnect();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
        addGeofences();
    }

    public boolean refreshGeofences() {
        if (mGeofenceList == null) {
            return false;
        }
        if (mGeofenceList.size() == 0) {
            return false;
        }
        if (mGoogleApiClient.isConnected()) {
            return true;
        } else {
            connect();
            /*if (mGoogleApiClient.isConnected()) {
                addGeofences();}*/
        }
        return true;
    }

    private void addGeofences() {
        Log.i(TAG, "Numbers of Geofences" + mGeofenceList.size());
        try {
            LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, getGeofencingRequest(), getGeofencePendingIntent()).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            logSecurityException(securityException);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " + "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    public void onResult(Status status) {

    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(context, LocationMessageTriggerService.class);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void addMessageToGeofenceList(MessageParts locMessage, long id) {
        PositionDependency positionDependency = locMessage.getPositionDependency();
        for (Address address : positionDependency.getAddresses()) {
            if (address != null) {
                Geofence.Builder builder = new Geofence.Builder();
                builder.setRequestId("ID:" + Objects.toString(id) + ";" + address.getAddressLine(0)).setCircularRegion(address.getLatitude(), address.getLongitude(), Float.parseFloat(positionDependency.getDistance())).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT).setNotificationResponsiveness(responsiveness(locMessage));

                if (positionDependency.getDuration() < 1) {
                    builder.setExpirationDuration(-1);
                    Log.d(TAG, "Geofence added with no Expiration");
                } else {
                    builder.setExpirationDuration(positionDependency.getDuration() * 60000);
                    Log.d(TAG, "Geofence added with Expiration of: " + positionDependency.getDuration() + " Minutes");
                }

                if (!mGeofenceList.contains(builder.build()))
                    mGeofenceList.add(builder.build());

                Log.d(TAG, "Numbers of Geofences" + mGeofenceList.size());
            }
        }

        if (mGoogleApiClient.isConnected()) {
            addGeofences();
        }
    }

    private int responsiveness(MessageParts locMessage) {
        if (locMessage.getAnFText().getUrgency()) {
            return 30000;
        }
        return 60000;
    }
}



