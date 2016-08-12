package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.locationAwareness;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import de.fiduciagad.anflibrary.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by Felix Schiefer on 08.01.2016.
 * Diese Klasse setzt die Ortsabhängigkeit eines
 * Nutzers in Abhängigkeit einer übergebenen Position.
 * Es wird die Distanz der Position zu der in den Einstellungen
 * hinterlegen Heimat- und Arbeitsaddresse hinterlegt.
 */
public class LocationAnswer {

    private Location currentPosition;
    private Context context;
    private boolean atWork = false;
    private boolean atHome = false;
    private static String TAG = "LocationAnswer";

    public LocationAnswer(Context context) {
        this.context = context;
    }

    public void setCurrentPosition(Location currentPosition) {
        this.currentPosition = currentPosition;
        setLocationContext();
    }

    private void setLocationContext() {
        if (currentPosition != null) {
            float homeDistance = 200;
            float workDistance = 200;
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

     /*   for (String keys:  pref.getAll().keySet()
             ) {
            Log.e("CreateDefaultValues",keys + " Value " + pref.getAll().get(keys));

        }*/
            Resources res = context.getResources();
            homeDistance = getDistance((String) pref.getAll().get(res.getString(R.string.home_key)));
            workDistance = getDistance((String) pref.getAll().get(res.getString(R.string.work_key)));

            atHome = (homeDistance < 150);
            atWork = (workDistance < 150);

            if (atWork && atHome) {
                if (workDistance > homeDistance) {
                    atWork = false;
                } else {
                    atHome = false;
                }
            }
        }
    }

    private float getDistance(String address) {
        if (address.equals("")) {
            Log.i(TAG, "No Address in the settings");
            return 2000;
        }
        List<Address> geocodeAddresslist = null;
        Geocoder geocoder = new Geocoder(context);

        Address resolvedAddress;
        try {
            geocodeAddresslist = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        resolvedAddress = geocodeAddresslist.get(0);
        Log.d(TAG, "Address: " + resolvedAddress.getAddressLine(0));
        float[] distance = new float[1];

        Location.distanceBetween(currentPosition.getLatitude(), currentPosition.getLongitude(), resolvedAddress.getLatitude(), resolvedAddress.getLongitude(), distance);
/*
        for (int i = 0; i < distance.length; i++) {
            Log.d(TAG, "Distance: " + distance[i]);
        }*/

        return distance[0];
    }

    public boolean isAtWork() {
        return atWork;
    }

    public boolean isAtHome() {
        return atHome;
    }
}
