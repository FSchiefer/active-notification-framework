package de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationControl;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Objects;

import de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger.LocationMessageTriggerService;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.MessageParts;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.PositionDependency;

/**
 * Created by Felix Schiefer on 10.01.2016.
 * This class is used to Handle the geofence message handling for messages with position dependencies
 */

public class GeofenceHandling implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, ActivityCompat.OnRequestPermissionsResultCallback {

    protected static final String TAG = GeofenceHandling.class.getSimpleName();

    protected GoogleApiClient mGoogleApiClient;

    protected ArrayList<Geofence> mGeofenceList;

    private ArrayList<MessageParts> messageList;

    private Context context;

    private static GeofenceHandling instance;

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

            LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, getGeofencePendingIntent()).setResultCallback(this);
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            Log.e(TAG, "ACCESS_FINE_LOCATION permission needs to be granted for using geofences");
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



    /**
     * This method is used to add new Geofences to the geofence List. Each geofence can only be once
     * in the list
     * @param locMessage The message that should be added to the geofence
     * @param id The id that should be used for the geofence
     */
    public void addMessageToGeofenceList(MessageParts locMessage, long id) {
        PositionDependency positionDependency = locMessage.getPositionDependency();
        if(mGeofenceList.size()>99){
          //TODO: Check if there is an way to find a work around for the automatic fillign with
            // geofences
        }

        for (Address address : positionDependency.getAddresses()) {
            if (address != null) {
                Geofence fence = getGeofence(locMessage, id, positionDependency, address);
              if (!mGeofenceList.contains(fence))
                    mGeofenceList.add(fence);
                Log.d(TAG, "addMessageToGeofenceList Numbers of Geofences" + mGeofenceList.size());
            }
        }

        if (mGoogleApiClient.isConnected()) {
            addGeofences();
        }
    }


    @NonNull
    protected Geofence getGeofence(MessageParts locMessage, long id, PositionDependency positionDependency, Address address) {
        Geofence.Builder builder = new Geofence.Builder();
        // TODO: Think about the posibility of adding multiple Messages with the same place but
        // different content
        builder.setRequestId("ID:" + Objects.toString(id) + ";" +
                address.getAddressLine(0)).setCircularRegion(address.getLatitude(),
                address.getLongitude(), Float.parseFloat(positionDependency.getDistance()))
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setNotificationResponsiveness(responsiveness(locMessage));

        if (positionDependency.getDuration() < 1) {
            builder.setExpirationDuration(-1);
            Log.d(TAG, "Geofence added with no Expiration");
        } else {
            builder.setExpirationDuration(positionDependency.getDuration() * 60000);
            Log.d(TAG, "Geofence added with Expiration of: " + positionDependency.getDuration() + " Minutes");
        }

        return builder.build();
    }


    /**
     * This method is used to add Geofences from the GeofenceList to the LocationServices.
     * If no geofence is available in the list no service is started
     */
    private void addGeofences() {
        Log.i(TAG, "addGeofences() Numbers of Geofences" + mGeofenceList.size());

        try {
            if(mGeofenceList.size()>0) {
                LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, getGeofencingRequest(), getGeofencePendingIntent()).setResultCallback(this);
            }
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

    /**
     * This method checks the importance of the message and set's depending on it  the intervall for
     * checking if a person is currently in the relevant area
     * @param locMessage The message where the Urgency of the message is stored
     * @return The responsivness time value in milliseconds
     */
    private int responsiveness(MessageParts locMessage) {
        if (locMessage.getAnFText().getUrgency()) {
            return 30000;
        }
        return 60000;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }
}



