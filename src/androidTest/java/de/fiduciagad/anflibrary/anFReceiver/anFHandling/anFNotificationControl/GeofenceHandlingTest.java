package de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationControl;

import android.content.Context;
import android.location.Address;
import android.support.annotation.NonNull;
import android.test.ActivityTestCase;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import android.util.Log;


import com.google.android.gms.location.Geofence;

import de.fiduciagad.anflibrary.anFMessageCreator.CreateAnFMessage;
import de.fiduciagad.anflibrary.anFMessageCreator.CreateAnFTextValues;
import de.fiduciagad.anflibrary.anFMessageCreator.CreatePositionDependencyValues;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.MessageParts;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.PositionDependency;


/**
 * Created by Felix on 26.11.2016.
 */
public class GeofenceHandlingTest extends ActivityTestCase {


    private Context context;
    private GeofenceHandling geofenceHandling;
    private String testServiceName;

    public void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        testServiceName = "testService";
        assertNotNull(context);

    }


    public void testGeofenceOnlyAdded() throws Exception {
        geofenceHandling=       new GeofenceHandling(context);
        MessageParts messageParts = generateMessageParts("Value");
        geofenceHandling.addMessageToGeofenceList(messageParts,1);
        assertEquals(messageParts.getPositionDependency().getAddresses().size(), geofenceHandling.mGeofenceList.size());


    }


    public void testGeofenceOnlyAddedOnce() throws Exception {

        geofenceHandling=       new GeofenceHandling(context);
        MessageParts messageParts = generateMessageParts("Value");
        geofenceHandling.addMessageToGeofenceList(messageParts,1);
        assertEquals(messageParts.getPositionDependency().getAddresses().size(), geofenceHandling.mGeofenceList.size());
        geofenceHandling.addMessageToGeofenceList(messageParts,2);
        assertEquals(messageParts.getPositionDependency().getAddresses().size(), geofenceHandling.mGeofenceList.size());

    }

    private MessageParts generateMessageParts(String ownValue){
        MessageParts messageParts = new MessageParts(context);
        messageParts.generateMessageParts(createMessageWithPositions(ownValue).getJSONObject());
        return messageParts;
    }




    /**
     * This method is used to build the message for a message with position dependencies
     * @return correct AnFMessage object with postion dependencies
     */
    private CreateAnFMessage createMessageWithPositions(String value) {
        CreateAnFMessage anFMessage = new CreateAnFMessage(context);
        anFMessage.setService(testServiceName);
        anFMessage.setAnFText(createAnFTextValues(value));
        CreatePositionDependencyValues positionDependencyValues = getCreatePositionDependencyValues();
        anFMessage.setLocation(positionDependencyValues);
        return anFMessage;
    }

    @NonNull
    private CreatePositionDependencyValues getCreatePositionDependencyValues() {
        CreatePositionDependencyValues positionDependencyValues = new CreatePositionDependencyValues(context);
        positionDependencyValues.setTrigger("1000");
        positionDependencyValues.addPlace("Killisfeldstraße 46", "76227", testServiceName);
        positionDependencyValues.addPlace("Waldstraße 14-18", "76133", testServiceName);
        positionDependencyValues.addPlace("Tiengener Straße 2", "76227", testServiceName);
        positionDependencyValues.addPlace("Theodor-Rehbock-Straße 15", "76131 ", testServiceName);
        positionDependencyValues.addPlace("Freiburger Straße 15", "77652", testServiceName);
        positionDependencyValues.addPlace("Heinrich-Hertz-Straße 3", "77656 ", testServiceName);
        positionDependencyValues.addPlace("Carl Blos Straße 2", "77654", testServiceName);
        return positionDependencyValues;
    }


    /**
     * This method is used to create the basic values of an AnFMessage
     * @param shopName Name that is used for Message and title
     * @return AnFTextValues as basic message elements
     */
    private CreateAnFTextValues createAnFTextValues(String shopName) {
        CreateAnFTextValues anFTextValues = new CreateAnFTextValues(context);
        anFTextValues.setConfidential(false);
        anFTextValues.setUrgency(false);
        anFTextValues.setTitle(shopName + " offers");
        anFTextValues.setShortMessage("call offers from " + shopName);
        return anFTextValues;
    }

}