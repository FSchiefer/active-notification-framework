package de.fiduciagad.anflibrary.anFMessageCreator;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import de.fiduciagad.anflibrary.R;

/**
 * Created by Felix Schiefer on 27.01.2016.
 * This class is provided to create easier messages with a location dependency
 */
public class CreatePositionDependencyValues extends ValueCreator {

    private JSONObject positionDependency;
    private JSONArray places;

    public CreatePositionDependencyValues(Context context) {
        super(context);

        positionDependency = new JSONObject();
        places = new JSONArray();
    }

    /**
     * Set the distance in meters to the targets where the user should get the notification
     *
     * @param trigger The trigger distance in meters.
     */
    public void setTrigger(String trigger) {
        JSONObject positionTrigger = new JSONObject();
        setValue(R.string.meter, positionTrigger, trigger);
        setValue(R.string.positionTrigger, positionDependency, positionTrigger);
    }

    /**
     * Set optional a duration for a message, where it is valid after being receipt.
     * If no duration is given the message is endlessly active
     *
     * @param duration The time a message is active.
     * @param timeUnit The timeUnit a User wants to use for the time a message is active
     */
    public void setDuration(long duration, TimeUnit timeUnit) {
        long durationInMintues = (TimeUnit.MINUTES.convert(duration, timeUnit));
        setValue(R.string.duration, positionDependency, Long.toString(durationInMintues));
    }

    /**
     * This parameter adds the coordinates from the address
     *
     * @param street    Streetname and address
     * @param postal    The postal zip code from the town.
     * @param placename The name of the location
     */
    public void addPlace(String street, String postal, String placename) {
        JSONObject place = new JSONObject();
        setValue(R.string.street, place, street);
        setValue(R.string.postal, place, postal);
        setValue(R.string.placeName, place, placename);
        addToJSONARRAY(places, place);
    }

    /**
     * This parameter adds the coordinates from the address
     *
     * @param navigation Boolean value true means a short action for navigation should be provided in the notification
     */
    public void addNavigation(Boolean navigation) {
        setValue(R.string.navigation, positionDependency, navigation);
    }

    @Override
    public JSONObject getJSONObject() {
        setValue(R.string.place, positionDependency, places);
        return positionDependency;
    }
}
