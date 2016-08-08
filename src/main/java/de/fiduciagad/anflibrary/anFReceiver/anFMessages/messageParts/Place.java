package de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Felix Schiefer on 26.01.2016.
 */
public class Place {

    private static final String CLASS_NAME = Place.class.getSimpleName();
    private Context context;

    private Address address;

    private String latidude;
    private String placeName;
    private String longitude;

    private String street;
    private String postal;
    private boolean isValidAddress;

    public Place(Context context) {
        this.context = context;
    }

    public boolean setAddress() {
        if (street != null && postal != null) {
            List<Address> geocodeAddresslist = null;
            Geocoder geocoder = new Geocoder(context);

            Address resolvedAddress;
            try {
                geocodeAddresslist = geocoder.getFromLocationName(postal + " " + street, 1);
            } catch (IOException e) {
                e.printStackTrace();
                isValidAddress = false;
                return false;
            }

            if (geocodeAddresslist.size() > 0) {
                resolvedAddress = geocodeAddresslist.get(0);
                this.address = resolvedAddress;
                Log.d(CLASS_NAME, "Address: " + resolvedAddress.getAddressLine(0) + " Lat " + resolvedAddress.getLatitude() + " Long " + resolvedAddress.getLongitude());
                isValidAddress = true;
                return true;
            }
        }

        if (latidude != null && longitude != null) {

            try {
                address = new Address(Locale.getDefault());
                address.setLatitude(Double.parseDouble(latidude));
                address.setLongitude(Double.parseDouble(longitude));
            } catch (Exception e) {
                e.printStackTrace();
                isValidAddress = false;
                return false;
            }
            isValidAddress = true;
            return true;
        }

        isValidAddress = false;
        return false;
    }

    public void setLatidude(String latidude) {
        this.latidude = latidude;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public Address getAddress() {
        return address;
    }

    public boolean isValidAddress() {
        return isValidAddress;
    }
}
