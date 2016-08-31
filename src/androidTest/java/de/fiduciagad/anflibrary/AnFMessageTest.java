package de.fiduciagad.anflibrary;

import android.content.Context;
import android.test.ActivityTestCase;

import org.json.JSONObject;

import de.fiduciagad.anflibrary.anFConnector.AnFConfiguration;
import de.fiduciagad.anflibrary.anFMessageCreator.CreateAnFMessage;
import de.fiduciagad.anflibrary.anFMessageCreator.CreateAnFTextValues;
import de.fiduciagad.anflibrary.anFMessageCreator.CreatePositionDependencyValues;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.AnFMessage;

/**
 * Created by Felix Schiefer on 28.02.2016.
 * This class is used to check if the message handling and validation of the framework works correct
 */
public class AnFMessageTest extends ActivityTestCase {

    private Context context;
    private ContextAnswer answer;
    private String testServiceName;
    private String testUnregisteredServiceName;

    public void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        testServiceName = "testService";
        testUnregisteredServiceName = "unregisteredTestService";
        assertNotNull(context);
    }

    /**
     * This method is used to test if a message with position dependencies is validated correctly
     * by the framework
     */
    public void testPositionMessageSender() {
        AnFConfiguration anFConfiguration = new AnFConfiguration(context);
        JSONObject jsonObject = createMessageWithPositions().getJSONObject();
        AnFMessage anFMessage = AnFMessage.getMessage(context, jsonObject);
        if (anFConfiguration.addService(testServiceName)) {
            anFMessage = AnFMessage.getMessage(context, jsonObject);
            assertEquals(true, anFMessage.isValid());
        } else {
            assertEquals(true, anFMessage.isValid());
        }
    }

    /**
     * This method is used to test if a message with position dependencies and false values is
     * validated correctly as false by the framework
     */
    public void testInvalidPositionMessageSender() {
        AnFConfiguration anFConfiguration = new AnFConfiguration(context);
        JSONObject jsonObject = createInvalidMessageWithPositions().getJSONObject();
        AnFMessage anFMessage = AnFMessage.getMessage(context, jsonObject);
        if (anFConfiguration.addService(testServiceName)) {
            anFMessage = AnFMessage.getMessage(context, jsonObject);
            assertEquals(null, anFMessage);
        } else {
            assertEquals(null, anFMessage);
        }
    }

    /**
     * This message is used to check if a message with no position dependencies is validated
     * correctly by the framework
     */
    public void testAnFMessageSender() {
        AnFConfiguration anFConfiguration = new AnFConfiguration(context);
        JSONObject jsonObject = createMessageWithNoPosition().getJSONObject();
        AnFMessage anFMessage = AnFMessage.getMessage(context, jsonObject);
        if (anFConfiguration.addService(testServiceName)) {
            anFMessage = AnFMessage.getMessage(context, jsonObject);
            assertEquals(true, anFMessage.isValid());
        } else {
            assertEquals(true, anFMessage.isValid());
        }
    }

    /**
     * This message is used to check if a message with no valid service is rejected correctly
     * by the framework
     */
    public void testNoService() {

        CreateAnFMessage anFMessage = new CreateAnFMessage(context);
        anFMessage.setService("Imaginaer");
        anFMessage.setAnFText(createAnFTextValues("imaginaer"));
        JSONObject jsonObject = anFMessage.getJSONObject();
        AnFMessage message = AnFMessage.getMessage(context, jsonObject);
        assertEquals(null, message);
    }

    /**
     * This method is used to check if a message with false content get's rejected correctly
     * by the framework
     */
    public void testInvalidMessage() {

        AnFConfiguration anFConfiguration = new AnFConfiguration(context);
        CreateAnFMessage createAnFMessage = new CreateAnFMessage(context);
        createAnFMessage.setService(testUnregisteredServiceName);
        JSONObject jsonObject = createAnFMessage.getJSONObject();
        AnFMessage anFMessage = AnFMessage.getMessage(context, jsonObject);

        if (anFConfiguration.addService(testUnregisteredServiceName)) {
            anFMessage = AnFMessage.getMessage(context, jsonObject);
            assertEquals(null, anFMessage);
        } else {
            assertEquals(null, anFMessage);
        }
    }

    /**
     * This method is used to build the message for a message with position dependencies
     * @return correct AnFMessage object with postion dependencies
     */
    private CreateAnFMessage createMessageWithPositions() {
        CreateAnFMessage anFMessage = new CreateAnFMessage(context);
        anFMessage.setService(testServiceName);
        anFMessage.setAnFText(createAnFTextValues(testServiceName));
        CreatePositionDependencyValues positionDependencyValues = new CreatePositionDependencyValues(context);
        positionDependencyValues.setTrigger("1000");
        positionDependencyValues.addPlace("Killisfeldstraße 46", "76227", testServiceName);
        positionDependencyValues.addPlace("Waldstraße 14-18", "76133", testServiceName);
        positionDependencyValues.addPlace("Tiengener Straße 2", "76227", testServiceName);
        positionDependencyValues.addPlace("Theodor-Rehbock-Straße 15", "76131 ", testServiceName);
        positionDependencyValues.addPlace("Freiburger Straße 15", "77652", testServiceName);
        positionDependencyValues.addPlace("Heinrich-Hertz-Straße 3", "77656 ", testServiceName);
        positionDependencyValues.addPlace("Carl Blos Straße 2", "77654", testServiceName);
        anFMessage.setLocation(positionDependencyValues);
        return anFMessage;
    }

    /**
     * This method is used to create an invalid message object with positions.
     * This message is invalid because the Entry with Heinrich-Hertz can't be matched from google
     * @return Invalid AnFMessage object
     */
    private CreateAnFMessage createInvalidMessageWithPositions() {
        CreateAnFMessage anFMessage = new CreateAnFMessage(context);
        anFMessage.setService(testServiceName);
        anFMessage.setAnFText(createAnFTextValues(testServiceName));
        CreatePositionDependencyValues positionDependencyValues = new CreatePositionDependencyValues(context);
        positionDependencyValues.setTrigger("1000");
        positionDependencyValues.addPlace("Killisfeldstraße 46", "76227", testServiceName);
        positionDependencyValues.addPlace("Waldstraße 14-18", "76133", testServiceName);
        positionDependencyValues.addPlace("Tiengener Straße 2", "76227", testServiceName);
        positionDependencyValues.addPlace("Theodor-Rehbock-Straße 15", "76131 ", testServiceName);
        positionDependencyValues.addPlace("Freiburger Straße 15", "77652", testServiceName);
        positionDependencyValues.addPlace("Heinrich-Hertz-Stdfaasdasdaße 3", "76 ", testServiceName);
        positionDependencyValues.addPlace("Carl Blos Straße 2", "77654", testServiceName);
        anFMessage.setLocation(positionDependencyValues);
        return anFMessage;
    }

    /**
     * This method is used to build small message with no position dependencies
     * @return Correct AnFMessage with no position dependencies
     */
    private CreateAnFMessage createMessageWithNoPosition() {
        CreateAnFMessage anFMessage = new CreateAnFMessage(context);
        anFMessage.setService(testServiceName);
        anFMessage.setAnFText(createAnFTextValues(testServiceName));
        return anFMessage;
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
