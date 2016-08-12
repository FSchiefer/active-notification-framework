package de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts;

import android.content.Context;

import de.fiduciagad.anflibrary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Klasse zum hinterlegen von Aktionen und Antworten auf Notifications
 */

public class ActionAnswers extends MessagePart {

    private Boolean voiceAnswer;
    private ArrayList<String> quickAnswer;

    private String name;
    private String number;

    public ActionAnswers(JSONObject anfObject, Context context) {
        super(anfObject, context);

        voiceAnswer = getJSONBoolean(R.string.voiceAnswer, anfObject);

        getQuickanswerStringArray();
        getCall();
    }

    private void getQuickanswerStringArray() {
        try {
            JSONArray jsonAnswer = getJSONArray(R.string.quickAnswer, anfObject);
            quickAnswer = new ArrayList<String>();
            if (jsonAnswer != null)
                for (int i = 0; i < jsonAnswer.length(); i++) {
                    quickAnswer.add(jsonAnswer.getJSONObject(i).getString(res.getString(R.string.answerValues)));
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCall() {
        name = getJSONString(R.string.callName, anfObject);
        number = getJSONString(R.string.callNumber, anfObject);
    }

    public ArrayList<String> getQuickAnswer() {
        return quickAnswer;
    }

    public Boolean getVoiceAnswer() {
        return voiceAnswer;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean isValid() {

        if (voiceAnswer != null || quickAnswer != null) {
            if (name != null) {
                return checkCall();
            }
            return true;
        } else if (name != null) {
            return checkCall();
        }

        return false;
    }

    private boolean checkCall() {
        return (name != null && number != null);
    }
}
