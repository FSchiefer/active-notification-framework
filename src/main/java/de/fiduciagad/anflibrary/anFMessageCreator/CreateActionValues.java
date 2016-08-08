package de.fiduciagad.anflibrary.anFMessageCreator;

import android.content.Context;

import de.fiduciagad.noflibrary.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Felix Schiefer on 27.01.2016.
 */
public class CreateActionValues extends ValueCreator {
    private JSONObject voiceAnswer;
    private JSONArray quickAnswer;
    private JSONObject answer;

    public CreateActionValues(Context context) {
        super(context);
        answer = new JSONObject();
    }

    /**
     * @param answers
     */
    public void setQuickAnswer(List<String> answers) {
        quickAnswer = new JSONArray();

        for (String answerValue : answers) {

            JSONObject value = new JSONObject();
            setValue(R.string.answerValues, value, answerValue);

            addToJSONARRAY(quickAnswer, value);
        }
        setValue(R.string.quickAnswer, answer, quickAnswer);
    }

    public void setVoiceAnswer() {

        setValue(R.string.voiceAnswer, answer, true);
    }

    public void setCall(String name, String number) {
        setValue(R.string.callName, answer, name);
        setValue(R.string.callNumber, answer, number);
    }

    @Override
    public JSONObject getJSONObject() {
        return answer;
    }
}
