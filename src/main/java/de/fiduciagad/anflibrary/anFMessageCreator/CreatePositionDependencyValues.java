package de.fiduciagad.anflibrary.anFMessageCreator;

import android.content.Context;

import de.fiduciagad.noflibrary.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Felix Schiefer on 27.01.2016.
 */
public class CreatePositionDependencyValues extends ValueCreator {

    private JSONObject positionDependency;
    private JSONArray places;

    public CreatePositionDependencyValues(Context context) {
        super(context);

        positionDependency = new JSONObject();
        places = new JSONArray();
    }

    public void setTrigger(String trigger) {
        JSONObject positionTrigger = new JSONObject();
        setValue(R.string.meter, positionTrigger, trigger);
        setValue(R.string.positionTrigger, positionDependency, positionTrigger);
    }

    /**
     * @param duration The validity of a message in minutes. If no duration is given the message is endlessly active
     */
    public void setDuration(int duration) {

        setValue(R.string.duration, positionDependency, Integer.toString(duration));
    }

    public void addPlace(String street, String postal, String placename) {
        JSONObject place = new JSONObject();
        setValue(R.string.street, place, street);
        setValue(R.string.postal, place, postal);
        setValue(R.string.placeName, place, placename);
        addToJSONARRAY(places, place);
    }

    public void addNavigation(Boolean navigation) {
        setValue(R.string.navigation, positionDependency, navigation);
    }

    @Override
    public JSONObject getJSONObject() {
        setValue(R.string.place, positionDependency, places);
        return positionDependency;
    }
}
