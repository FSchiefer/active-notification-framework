package de.fiduciagad.anflibrary.anFReceiver.anFMessages;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.AnFText;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.MessagePart;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.MessageParts;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.ActionAnswers;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.PaymentInformations;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.PositionDependency;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.TimeDependency;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.AnFNotificationCompat;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFServiceHandling.ServiceDB;
import de.fiduciagad.anflibrary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Felix Schiefer on 03.01.2016.
 */
public abstract class AnFMessage {
    private static final String CLASS_NAME = AnFMessage.class.getSimpleName();

    protected static Resources res;
    protected Context context;

    protected JSONObject anfPayload;
    protected String service;
    protected boolean trigger;
    protected boolean urgent;
    protected boolean confidential;

    protected AnFText anFText;

    protected ActionAnswers answers;
    protected PositionDependency positionDependency;
    protected TimeDependency timeDependency;
    protected PaymentInformations paymentInformations;

    private MessageParts messageParts;
    private List<MessagePart> messagePartList;

    /**
     * @param context
     * @param anfPayload
     * @param anFText    Konstruktor für das Erstellen der Typunabhängigen Nachrichtenelemente
     */
    protected AnFMessage(Context context, JSONObject anfPayload, AnFText anFText) {
        messagePartList = new ArrayList<>();
        generateAnFBody(context, anFText, anfPayload);
    }

    protected AnFMessage(Context context, JSONObject anfPayload, AnFText anFText, PositionDependency positionDependency) {
        messagePartList = new ArrayList<>();
        generateAnFBody(context, anFText, anfPayload);

        this.positionDependency = positionDependency;
        messagePartList.add(positionDependency);
        setTrigger();
    }

    private void generateAnFBody(Context context, AnFText anFText, JSONObject anfPayload) {
        this.context = context;
        this.anfPayload = anfPayload;
        this.anFText = anFText;
        messagePartList.add(anFText);
        createMessageParts();
        urgent = anFText.getUrgency();
        confidential = anFText.getConfidential();
    }

    public MessageParts getMessageParts() {
        initiateMessageParts();
        return messageParts;
    }

    private void initiateMessageParts() {
        if (messageParts == null) {
            messageParts = new MessageParts(context);
            messageParts.generateMessageParts(anfPayload);
        }
    }

    /**
     * Erstellt die AnF Message mit allen relevanten Objekten, sodass ein einfacher Zugriff möglich
     * ist
     */
    private void createMessageParts() {

        initiateMessageParts();

        if (messageParts.getService() != null) {
            service = messageParts.getService();
        } else if (messageParts.getActionAnswers() != null) {
            answers = messageParts.getActionAnswers();
            messagePartList.add(answers);
        } else if (messageParts.getTimeDependency() != null) {
            timeDependency = messageParts.getTimeDependency();
            messagePartList.add(timeDependency);
        } else if (messageParts.getPaymentInformations() != null) {
            paymentInformations = messageParts.getPaymentInformations();
            messagePartList.add(paymentInformations);
        }
    }

    private static List<String> typeList;

    /**
     * @param context
     * @param anfPayload
     * @return Alternativer Konstruktor, wenn kein JSONObjekt, sondern ein String mit der Nachricht
     * übergeben wird.
     */
    public static AnFMessage getMessage(Context context, String anfPayload) {
        res = context.getResources();

        AnFMessage m = null;
        try {
            JSONObject payload = new JSONObject(anfPayload);
            m = getMessage(context, payload);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return m;
    }

    public static AnFMessage getMessage(Context context, MessageParts messageParts) {
        if (messageParts.getPositionDependency() != null) {
            if (messageParts.getPositionDependency().isTrigger()) {
                return new LocationBasedAnFMessage(context, messageParts.getAnFMessage(), messageParts.getAnFText(), messageParts.getPositionDependency());
            }
            return new ShortAnFMessage(context, messageParts.getAnFMessage(), messageParts.getAnFText(), messageParts.getPositionDependency());
        }

        return new ShortAnFMessage(context, messageParts.getAnFMessage(), messageParts.getAnFText());
    }

    /**
     * @param context
     * @param anfPayload
     * @return Liefert die korrekte instanz der AnFMessage zurück oder null wenn die Nachricht nicht valide ist
     */
    public static AnFMessage getMessage(Context context, JSONObject anfPayload) {
        res = context.getResources();
        AnFMessage m = null;

        try {
            String service = anfPayload.getString(res.getString(R.string.serviceType));
            JSONObject mofTextJson = anfPayload.getJSONObject(res.getString(R.string.anfText));
            AnFText anFTextStatic = new AnFText(mofTextJson, context);
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);

            if (!getTypeList(context).contains(service) || !anFTextStatic.isValid() || !preference.getBoolean(service + res.getString(R.string.service_key), true)) {
                Log.e(CLASS_NAME, "No Matching Service for: " + service);
                return null;
            }

            if ((!preference.getBoolean(service + res.getString(R.string.urgency_key), true) && !(anFTextStatic.getUrgency() || preference.getBoolean(service + res.getString(R.string.allUrgent_key), false)))) {
                Log.e(CLASS_NAME, "No Matching not urgent services for: " + service);
                return null;
            }

            if ((!preference.getBoolean(service + res.getString(R.string.confidential_key), true) && (anFTextStatic.getConfidential() || preference.getBoolean(service + res.getString(R.string.allConfidential_key), false)))) {
                Log.e(CLASS_NAME, "No confidential messages allowed for: " + service);
                return null;
            }

            Iterator iterator = anfPayload.keys();
            String key = null;
            JSONObject jsonObject;

            // Unterscheidung wird durchgeführt um festzulegen, dass für den einen Typ ein anderer Controller genutzt wird, und damit der Aufbau der Seiten anders erfolgt.
            boolean instantmessage = true;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (key.equals(res.getString(R.string.positionDependency))) {
                    jsonObject = anfPayload.getJSONObject(key);
                    PositionDependency positionDependency = new PositionDependency(jsonObject, context);
                    if (!positionDependency.isValid()) {
                        return null;
                    }
                    if (positionDependency.isTrigger()) {
                        m = new LocationBasedAnFMessage(context, anfPayload, anFTextStatic, positionDependency);
                    } else {
                        m = new ShortAnFMessage(context, anfPayload, anFTextStatic, positionDependency);
                    }
                }
            }

            if (m == null) {
                m = new ShortAnFMessage(context, anfPayload, anFTextStatic);
            }

            if (m.isValid())
                return m;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * @return Liefert zurück ob die Nachricht valide ist oder nicht
     */
    public boolean isValid() {
        for (MessagePart messagePart : messagePartList) {
            if (!messagePart.isValid()) {
                return false;
            }
        }
        return true;
    }

    ;

    /**
     * @param answer
     * @param id
     * @return Erhält den durch den Resolver ermittelten Wert für die Kontext Antwort und die
     * ID der zur Darstellung zu bringenden Nachricht um diese per Extra übergeben zu können
     */
    public abstract AnFNotificationCompat getNotificationCompat(ContextAnswer answer, int id);

    /**
     * @param mActivity
     * @return Liefert eine Typliste mit allen beim Framework registrierten Services zurück um eine
     * Validierung der Nachricht zu ermöglichen.
     */
    private static List<String> getTypeList(Context mActivity) {
        ServiceDB serviceDB = new ServiceDB(mActivity);
        typeList = serviceDB.getServiceList();
        return typeList;
    }

    public AnFText getAnFText() {
        return anFText;
    }

    public String getService() {
        return service;
    }

    /**
     * @return Liefert die über eine AnF Message mitgegebenen Aktionen zurück
     */
    public ActionAnswers getAnswers() {
        if (answers != null) {
            if (answers.isValid()) {
                return answers;
            }
        }
        return null;
    }

    /**
     * @return Liefert die über eine AnF Message mitgegebeen Zusatzinformationen zurück
     */
    public PaymentInformations getPaymentInformations() {
        if (paymentInformations != null) {
            if (paymentInformations.isValid()) {
                return paymentInformations;
            }
        }
        return null;
    }

    public PositionDependency getPositiondependency() {
        if (positionDependency != null) {
            return positionDependency;
        }
        return null;
    }

    @Override
    public String toString() {
        return "AnFMessage{" +
                "service='" + service + '\'' +
                ", trigger='" + trigger + '\'' +
                ", anFText=" + anFText +
                '}';
    }

    public JSONObject getAnfPayload() {
        return anfPayload;
    }

    /**
     * @return Liefert zurück ob diese Nachricht einen gesonderte Trigger beötigt.
     * im Prototyp gibt es nur Ortsgetriggerte Zeiten, werden ebenfalls zeitgetriggerte benötigt
     * sollt auf ein Enum zurückgegriffen werden
     */
    public boolean isTriggerNeeded() {
        return trigger;
    }

    public boolean isUrgent() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        if (preference.getBoolean(service + res.getString(R.string.allUrgent_key), false)) {
            return true;
        }

        return urgent;
    }

    public boolean isConfidential() {

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        if (preference.getBoolean(service + res.getString(R.string.allConfidential_key), false)) {
            return true;
        }
        return confidential;
    }

    protected void setTrigger() {
        if (positionDependency.isTrigger()) {
            trigger = true;
        }
    }
}
