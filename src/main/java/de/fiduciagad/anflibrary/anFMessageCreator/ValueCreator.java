package de.fiduciagad.anflibrary.anFMessageCreator;

import android.content.Context;
import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * This class is a helper class used to add attributes to an JSON Object.
 */
public abstract class ValueCreator {

    protected Resources res;
    protected Context context;

    public ValueCreator(Context context) {
        this.context = context;
        res = context.getResources();
    }

    protected void setValue(int key, JSONObject jsonObject, String value) {
        addToJSON(key, jsonObject, value);
    }

    protected void setValue(int key, JSONObject jsonObject, Boolean value) {
        addToJSON(key, jsonObject, value);
    }

    protected void setValue(int key, JSONObject jsonObject, JSONObject value) {
        addToJSON(key, jsonObject, value);
    }

    protected void setValue(int key, JSONObject jsonObject, JSONArray value) {
        addToJSON(key, jsonObject, value);
    }

    protected void addToJSONARRAY(JSONArray jsonObject, JSONObject value) {
        try {
            jsonObject.put(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToJSON(int key, JSONObject jsonObject, Object value) {
        try {
            jsonObject.put(res.getString(key), value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract JSONObject getJSONObject();
}
