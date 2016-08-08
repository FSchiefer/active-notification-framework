package de.fiduciagad.anflibrary;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextLevelEnum;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.locationAwareness.LocationAnswer;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Felix Schiefer on 28.02.2016.
 */
public class ContextTest extends AndroidTestCase {

    private Context context;
    private ContextAnswer answer;

    public void setUp() throws Exception {
        super.setUp();

        context = new MockContext();

        setContext(context);

        assertNotNull(context);

    }



    public void testContextOtherAppointment()  {
        answer = new ContextAnswer(context);
        answer.setWatchAvailable(true);
        answer.setAppointmentCurrentlyRunning(true);
        answer.setCurrentActivityValue(DetectedActivity.ON_FOOT);
        answer.setDisplayIsRunning(false);
        LocationAnswer locationAnswer = new LocationAnswer(context);
        answer.setCurrentPosition(locationAnswer);

        assertEquals(ContextLevelEnum.S2, answer.getContextLevel());
    }
    public void testContextOtherWalking()  {
        answer = new ContextAnswer(context);
        answer.setWatchAvailable(true);
        answer.setAppointmentCurrentlyRunning(false);
        answer.setCurrentActivityValue(DetectedActivity.ON_FOOT);
        answer.setDisplayIsRunning(false);
        LocationAnswer locationAnswer = new LocationAnswer(context);
        answer.setCurrentPosition(locationAnswer);

        assertEquals(ContextLevelEnum.S2, answer.getContextLevel());
    }
    public void testContextOtherStill()  {
        answer = new ContextAnswer(context);
        answer.setWatchAvailable(true);
        answer.setAppointmentCurrentlyRunning(false);
        answer.setCurrentActivityValue(DetectedActivity.STILL);
        answer.setDisplayIsRunning(false);
        LocationAnswer locationAnswer = new LocationAnswer(context);
        answer.setCurrentPosition(locationAnswer);

        assertEquals(ContextLevelEnum.S1, answer.getContextLevel());
    }
    public void testContextOtherDriving(){
        answer = new ContextAnswer(context);
        answer.setWatchAvailable(true);
        answer.setAppointmentCurrentlyRunning(false);
        answer.setCurrentActivityValue(DetectedActivity.IN_VEHICLE);
        answer.setDisplayIsRunning(false);
        LocationAnswer locationAnswer = new LocationAnswer(context);
        answer.setCurrentPosition(locationAnswer);

        assertEquals(ContextLevelEnum.S2, answer.getContextLevel());
    }

    public void testContextDisplayRunning(){
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


