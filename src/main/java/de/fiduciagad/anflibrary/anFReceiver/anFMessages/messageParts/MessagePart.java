package de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts;

import android.content.Context;
import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class is used to return all basic elements from a JSON Object, that are
 * needed to create an AnFMessage
 */
public abstract class MessagePart {

    protected Resources res;

    protected JSONObject anfObject;
    protected Context context;

    public MessagePart(JSONObject anfObject, Context context) {
        this.context = context;
        res = context.getResources();
        this.anfObject = anfObject;
    }

    protected String getJSONString(int key, JSONObject jsonObject) {

        try {
            return jsonObject.getString(res.getString(key));
        } catch (Exception e) {
            /*e.printStackTrace();*/
        }
        return null;
    }

    /**
     * This method is used to retrieve a Boolean from a Json Object. If no boolean is given
     * false is returned as default value
     * @param key The key in the jsonObject
     * @param jsonObject The jsonObject where the key should be stored
     * @return The stored boolean or false as default
     */
    protected Boolean getJSONBoolean(int key, JSONObject jsonObject) {

        try {
            return jsonObject.getBoolean(res.getString(key));
        } catch (Exception e) {
            // TODO: Implement exception handling for not existing booleans
          /*  e.printStackTrace();*/
        }
        return false;
    }

    protected JSONObject getJSONObject(int key, JSONObject jsonObject) {
        try {
            return jsonObject.getJSONObject(res.getString(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected JSONArray getJSONArray(int key, JSONObject jsonObject) {
        try {
            return jsonObject.getJSONArray(res.getString(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getAnFJSONObject() {
        return anfObject;
    }

    public abstract boolean isValid();
}
