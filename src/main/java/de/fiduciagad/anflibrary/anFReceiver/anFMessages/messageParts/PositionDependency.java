package de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts;

import android.content.Context;
import android.location.Address;
import android.util.Log;

import de.fiduciagad.anflibrary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix Schiefer on 02.01.2016.
 */
public class PositionDependency extends MessagePart {

    private static final String CLASS_NAME = PositionDependency.class.getSimpleName();

    private JSONObject position;
    private JSONObject positionTrigger;
    private JSONObject place;
    private JSONArray places;

    private String distance;
    private int duration;
    private String latidude;

    private String placeName;
    private String longitude;
    private String street;
    private String postal;

    private List<Place> placeList;

    private List<Address> addresses;

    private boolean isValidAddress;

    private boolean trigger;

    public boolean isNavigation() {
        return navigation;
    }

    private boolean navigation;

    public PositionDependency(JSONObject anfObject, Context context) {
        super(anfObject, context);

        placeList = new ArrayList<>();

        // Triggerdistanz
        positionTrigger = getJSONObject(R.string.positionTrigger, anfObject);
        if (positionTrigger != null) {
            if (getJSONString(R.string.meter, positionTrigger) != null) {
                distance = getJSONString(R.string.meter, positionTrigger);
            } else {
                distance = "";
            }
        }


        navigation = getJSONBoolean(R.string.navigation, anfObject);

        if (getJSONString(R.string.duration, anfObject) != null) {
            duration = Integer.parseInt(getJSONString(R.string.duration, anfObject));
        } else {
            duration = -1;
        }
        trigger = false;
        if (distance != null && !distance.equals("")) {
            trigger = true;
        }

        // Name des Ortes
        places = getJSONArray(R.string.place, anfObject);

        for (int i = 0; i < places.length(); i++) {
            try {
                place = places.getJSONObject(i);
                placeList.add(createPlace());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

  /*      place = getJSONObject(R.string.place, position);*/

    }

    private Place createPlace() {
        Place placeObject = new Place(context);
        placeName = getJSONString(R.string.placeName, place);
        street = getJSONString(R.string.street, place);
        postal = getJSONString(R.string.postal, place);

        placeObject.setPlaceName(placeName);

        if (street != null && postal != null) {
            placeObject.setStreet(street);
            placeObject.setPostal(postal);
            return placeObject;
        }

        latidude = getJSONString(R.string.latitude, place);
        longitude = getJSONString(R.string.longitude, place);
        placeObject.setLatidude(latidude);
        placeObject.setLongitude(longitude);
        return placeObject;
    }

    private boolean setAddress() {
        addresses = new ArrayList<>();
        isValidAddress = true;
        for (Place placeObject : placeList) {
            if (!placeObject.setAddress()) {
                isValidAddress = false;
                showInvalidAddressError();
                return false;
            }

            addresses.add(placeObject.getAddress());
        }
        return true;
    }

    public boolean isTrigger() {
        Log.i(CLASS_NAME, "Trigger = " + trigger);
        return trigger;
    }

    @Override
    public boolean isValid() {
        if (addresses == null) {
            setAddress();
        }
        if (isValidAddress) {
            if (positionTrigger != null) {
                return (isValidPositions() && isValidPositionTrigger());
            } else {
                return (isValidPositions());
            }
        }
        return false;
    }

    private boolean isValidPositions() {
        if (places != null) {
            return (isValidAddress);
        }
        return false;
    }

    private boolean isValidPositionTrigger() {
        if (distance != null) {
            return true;
        }
        return false;
    }

    public List<Address> getAddresses() {
        if (addresses == null) {
            setAddress();
        }
        return addresses;
    }

    public String getDistance() {
        return distance;
    }

    public String getPlaceName() {
        return placeName;
    }

    public int getDuration() {
        return duration;
    }

    private void showInvalidAddressError(){
        Log.e(CLASS_NAME, "Address: is invalid as a result the sent AnF-Message is invalid ");
    }
}
