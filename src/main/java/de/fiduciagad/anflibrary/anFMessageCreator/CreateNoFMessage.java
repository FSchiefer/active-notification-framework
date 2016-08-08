package de.fiduciagad.anflibrary.anFMessageCreator;

import android.content.Context;

import de.fiduciagad.noflibrary.R;

import org.json.JSONObject;

/**
 * Created by Felix Schiefer on 27.01.2016.
 */
public class CreateNoFMessage extends ValueCreator {
    JSONObject anFMessage;

    public CreateNoFMessage(Context context) {
        super(context);
        anFMessage = new JSONObject();
    }

    public void setService(String anFValues) {
        setValue(R.string.serviceType, anFMessage, anFValues);
    }

    public void setIdentifier(String anFValues) {

        setValue(R.string.identifier, anFMessage, anFValues);
    }

    public void setMofText(CreateAnFTextValues anFValues) {
        setValue(R.string.anfText, anFMessage, anFValues.getJSONObject());
    }

    public void setActions(CreateActionValues anFValues) {
        setValue(R.string.actions, anFMessage, anFValues.getJSONObject());
    }

    public void setLocation(CreatePositionDependencyValues anFValues) {
        setValue(R.string.positionDependency, anFMessage, anFValues.getJSONObject());
    }

    @Override
    public JSONObject getJSONObject() {
        return anFMessage;
    }
}
