package de.fiduciagad.anflibrary.anFMessageCreator;

import android.content.Context;
import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Felix Schiefer on 27.01.2016.
 */
public abstract class ValueCreator {
    protected Resources res;
    protected Context context;

    public ValueCreator(Context context) {
        this.context = context;
        res = context.getResources();
    }

    protected void setValue(int key, JSONObject jsonObject, String value) {

        try {
            jsonObject.put(res.getString(key), value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setValue(int key, JSONObject jsonObject, Boolean value) {
        try {
            jsonObject.put(res.getString(key), value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setValue(int key, JSONObject jsonObject, JSONObject value) {
        try {
            jsonObject.put(res.getString(key), value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setValue(int key, JSONObject jsonObject, JSONArray value) {
        try {
            jsonObject.put(res.getString(key), value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void addToJSONARRAY(JSONArray jsonObject, JSONObject value) {
        try {
            jsonObject.put(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract JSONObject getJSONObject();
}
