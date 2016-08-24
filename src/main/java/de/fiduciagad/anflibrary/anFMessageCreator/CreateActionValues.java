package de.fiduciagad.anflibrary.anFMessageCreator;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import de.fiduciagad.anflibrary.R;

/**
 * This class is used to Create the json values for actions that could be done
 * in a notification on a watch for example
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
     * This method is used to set multiple strings for a shortanswer in a notification
     *
     * @param answers A List of possible short ansers for a user
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

    /**
     * This method is used to enable a notification on android wear to create a voice answer
     * with text to speech
     */
    public void setVoiceAnswer() {
        setValue(R.string.voiceAnswer, answer, true);
    }

    /**
     * This method is used to enable a notification on android wear for making a call to a
     * specified number
     *
     * @param name   Name of the number that should be called
     * @param number The number that should be dialed
     */
    public void setCall(String name, String number) {
        setValue(R.string.callName, answer, name);
        setValue(R.string.callNumber, answer, number);
    }

    @Override
    public JSONObject getJSONObject() {
        return answer;
    }
}
