package de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts;

import android.content.Context;
import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Struktur für die Nachrichtenelemente die in einer AnFNachricht verwendet werde können.
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

    protected Boolean getJSONBoolean(int key, JSONObject jsonObject) {

        try {
            return jsonObject.getBoolean(res.getString(key));
        } catch (Exception e) {
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
