package de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts;

import android.content.Context;
import android.content.res.Resources;

import de.fiduciagad.anflibrary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Klasse für ein Halten und erstellen aller Nachrichtenelemente einer AnF Nachricht ohne validierung
 * anhand eines Strings oder JSON-Objektes zur besseren Verarbeitung der Nachricht.
 */
public class MessageParts {
    private Context context;
    private Resources res;
    private JSONObject anFMessage;

    private String service;
    private String identifier;
    private AnFText anFText;
    private PositionDependency positionDependency;
    private PaymentInformations paymentInformations;
    private ActionAnswers actionAnswers;
    private TimeDependency timeDependency;

    public MessageParts(Context context) {
        this.context = context;
        this.res = context.getResources();
    }

    public void generateMessageParts(String anFMessage) {
        try {
            JSONObject jsonObject = new JSONObject(anFMessage);
            generateMessageParts(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param anFMessage
     */
    public void generateMessageParts(JSONObject anFMessage) {
        this.anFMessage = anFMessage;
        try {
            Iterator iterator = anFMessage.keys();
            String key = null;
            JSONObject jsonObject;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (key.equals(res.getString(R.string.serviceType))) {
                    service = anFMessage.getString(res.getString(R.string.serviceType));
                } else if (key.equals(res.getString(R.string.identifier))) {
                    identifier = anFMessage.getString(res.getString(R.string.identifier));
                } else if (key.equals(res.getString(R.string.actions))) {
                    jsonObject = anFMessage.getJSONObject(key);
                    actionAnswers = new ActionAnswers(jsonObject, context);
                } else if (key.equals(res.getString(R.string.timeDependency))) {
                    jsonObject = anFMessage.getJSONObject(key);
                    timeDependency = new TimeDependency(jsonObject, context);
                } else if (key.equals(res.getString(R.string.paymentInformation))) {
                    jsonObject = anFMessage.getJSONObject(key);
                    paymentInformations = new PaymentInformations(jsonObject, context);
                } else if (key.equals(res.getString(R.string.positionDependency))) {
                    jsonObject = anFMessage.getJSONObject(key);
                    positionDependency = new PositionDependency(jsonObject, context);
                } else if (key.equals(res.getString(R.string.anfText))) {
                    jsonObject = anFMessage.getJSONObject(key);
                    anFText = new AnFText(jsonObject, context);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public AnFText getAnFText() {
        return anFText;
    }

    public PositionDependency getPositionDependency() {
        return positionDependency;
    }

    public PaymentInformations getPaymentInformations() {
        return paymentInformations;
    }

    public ActionAnswers getActionAnswers() {
        return actionAnswers;
    }

    public TimeDependency getTimeDependency() {
        return timeDependency;
    }

    public String getService() {
        return service;
    }

    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return Das unbearbeitete JSON Objekt, von dem die Nachrichtenelemente erstellt wurden
     */
    public JSONObject getAnFMessage() {
        return anFMessage;
    }
}
