package de.fiduciagad.anflibrary;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import com.google.android.gms.location.DetectedActivity;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextLevelEnum;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.locationAwareness.LocationAnswer;

/**
 * Created by Felix Schiefer on 28.02.2016.
 * This class is used to check if the evaluation for context states with a given context is working correct
 */
public class ContextEvaluationTest extends AndroidTestCase {

    private Context context;
    private ContextAnswer answer;

    public void setUp() throws Exception {
        super.setUp();

        context = new MockContext();

        setContext(context);

        assertNotNull(context);
    }

    /**
     * This method is used to check if the context level of a person with a smartwatch which is
     * currently not at home or work and is in an appointment is correct
     */
    public void testContextOtherAppointment() {
        answer = new ContextAnswer(context);
        answer.setWatchAvailable(true);
        answer.setAppointmentCurrentlyRunning(true);
        answer.setCurrentActivityValue(DetectedActivity.ON_FOOT);
        answer.setDisplayIsRunning(false);
        LocationAnswer locationAnswer = new LocationAnswer(context);
        answer.setCurrentPosition(locationAnswer);

        assertEquals(ContextLevelEnum.S2, answer.getContextLevel());
    }

    /**
     * This method is used to check if the context level of a person with a smartwatch which is
     * currently walking not at home or work and is not in an appointment is correct
     */
    public void testContextOtherWalking() {
        answer = new ContextAnswer(context);
        answer.setWatchAvailable(true);
        answer.setAppointmentCurrentlyRunning(false);
        answer.setCurrentActivityValue(DetectedActivity.ON_FOOT);
        answer.setDisplayIsRunning(false);
        LocationAnswer locationAnswer = new LocationAnswer(context);
        answer.setCurrentPosition(locationAnswer);

        assertEquals(ContextLevelEnum.S2, answer.getContextLevel());
    }


    /**
     * This method is used to check if the context level of a person with a smartwatch which is
     * currently not at home or work and is not in an appointment or walking is correct
     */
    public void testContextOtherStill() {
        answer = new ContextAnswer(context);
        answer.setWatchAvailable(true);
        answer.setAppointmentCurrentlyRunning(false);
        answer.setCurrentActivityValue(DetectedActivity.STILL);
        answer.setDisplayIsRunning(false);
        LocationAnswer locationAnswer = new LocationAnswer(context);
        answer.setCurrentPosition(locationAnswer);

        assertEquals(ContextLevelEnum.S1, answer.getContextLevel());
    }

    /**
     * This method is used to check if the context level of a person with a smartwatch which is
     * currently driving not at home or work and is not in an appointment is correct
     */
    public void testContextOtherDriving() {
        answer = new ContextAnswer(context);
        answer.setWatchAvailable(true);
        answer.setAppointmentCurrentlyRunning(false);
        answer.setCurrentActivityValue(DetectedActivity.IN_VEHICLE);
        answer.setDisplayIsRunning(false);
        LocationAnswer locationAnswer = new LocationAnswer(context);
        answer.setCurrentPosition(locationAnswer);

        assertEquals(ContextLevelEnum.S2, answer.getContextLevel());
    }

    /**
     * This method is used to check if the context level of a person with a smartwatch
     * and a phone with a running display is correct
     */
    public void testContextDisplayRunning() {
        answer = new ContextAnswer(context);
        answer.setWatchAvailable(true);
        answer.setAppointmentCurrentlyRunning(true);
        answer.setCurrentActivityValue(DetectedActivity.ON_FOOT);
        answer.setDisplayIsRunning(true);
        LocationAnswer locationAnswer = new LocationAnswer(context);
        answer.setCurrentPosition(locationAnswer);

        assertEquals(ContextLevelEnum.S0, answer.getContextLevel());
    }
}


