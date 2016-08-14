package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.locationAwareness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver.ContextResolverInterfaces.PositionInterface;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.googleApiHandling.GoogleApiHandling;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.Constants;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

/**
 * Created by Felix Schiefer on 05.01.2016.
 */
public class LocationHandling extends GoogleApiHandling {

    private Location mLastLocation;
    protected static final String TAG = "LocationHandling";
    private Intent intent;
    private PositionInterface positionInterface;
    boolean alreadySent;

    public LocationHandling(Context context, GoogleApiClient gClient, PositionInterface positionInterface) {
        super(context, gClient);
        mReceiver = new AddressResultReceiver();
        this.positionInterface = positionInterface;
        // Create an intent for passing to the intent service responsible for fetching the address.
        intent = new Intent(mContext, FetchAddressIntentService.class);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, new IntentFilter(Constants.RESULT_DATA_KEY));
    }

    @Override
    public void connectHandling() {
        alreadySent = false;

        Log.i(TAG, "Location is called");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(gClient);

        if (mLastLocation != null) {

            if (((System.currentTimeMillis()) > (mLastLocation.getTime() + 300000)) || !(mLastLocation.getAccuracy() < 50)) {
                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(5000);
                mLocationRequest.setFastestInterval(2500);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                // TODO: Implement termination condition after X runs to prevent to many repeats
                LocationServices.FusedLocationApi.requestLocationUpdates(gClient, mLocationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i(TAG, "Update Received");
                        if (location.getAccuracy() < 50) {
                            Log.i(TAG, "New Location is accurate enough");
                            mLastLocation = location;
                            LocationServices.FusedLocationApi.removeLocationUpdates(gClient, this);
                            handleLocation();
                        }
                    }
                });
                return;
            }
            handleLocation();
        }
    }

    private void handleLocation() {
        String mLatitudeText = null;
        String mLongitudeText = null;
        mLatitudeText = String.valueOf(mLastLocation.getLatitude());
        mLongitudeText = String.valueOf(mLastLocation.getLongitude());

        Log.d(TAG, "Lat: " + mLatitudeText + " Long: " + mLongitudeText + " Location Time " + mLastLocation.getTime() + " Actual Time " + System.currentTimeMillis() + " Accuracy " + mLastLocation.getAccuracy() + " Accurate enough " + (mLastLocation.getAccuracy() < 50));

        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);

        mContext.startService(intent);
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            List<String> addressList = intent.getStringArrayListExtra(Constants.RESULT_DATA_KEY);
            if (!alreadySent) {
                if (addressList != null && mLastLocation != null) {
                    alreadySent = true;
                    LocationAnswer locAnswer = new LocationAnswer(context);
                    Log.d(TAG, " Location Answer will be sent ");
                    locAnswer.setCurrentPosition(mLastLocation);

                    positionInterface.setPosition(locAnswer);
                }
            }
        }
    }
}
