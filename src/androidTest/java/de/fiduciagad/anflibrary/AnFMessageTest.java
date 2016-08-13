package de.fiduciagad.anflibrary;

import android.content.Context;
import android.test.ActivityTestCase;

import de.fiduciagad.anflibrary.anFConnector.AnFConfiguration;
import de.fiduciagad.anflibrary.anFMessageCreator.CreateAnFMessage;
import de.fiduciagad.anflibrary.anFMessageCreator.CreateAnFTextValues;
import de.fiduciagad.anflibrary.anFMessageCreator.CreatePositionDependencyValues;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.AnFMessage;

import org.json.JSONObject;

/**
 * Created by Felix Schiefer on 28.02.2016.
 * Th
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

    public void testNoService() {

        CreateAnFMessage anFMessage = new CreateAnFMessage(context);
        anFMessage.setService("Imaginaer");
        anFMessage.setMofText(createAnFTextValues("imaginaer"));
        JSONObject jsonObject = anFMessage.getJSONObject();
        AnFMessage message = AnFMessage.getMessage(context, jsonObject);
        assertEquals(null, message);
    }

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

    private CreateAnFMessage createMessageWithPositions() {
        CreateAnFMessage anFMessage = new CreateAnFMessage(context);
        anFMessage.setService(testServiceName);
        anFMessage.setMofText(createAnFTextValues(testServiceName));
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

    private CreateAnFMessage createInvalidMessageWithPositions() {
        CreateAnFMessage anFMessage = new CreateAnFMessage(context);
        anFMessage.setService(testServiceName);
        anFMessage.setMofText(createAnFTextValues(testServiceName));
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

    private CreateAnFMessage createMessageWithNoPosition() {
        CreateAnFMessage anFMessage = new CreateAnFMessage(context);
        anFMessage.setService(testServiceName);
        anFMessage.setMofText(createAnFTextValues(testServiceName));
        return anFMessage;
    }

    private CreateAnFTextValues createAnFTextValues(String shopName) {
        CreateAnFTextValues anFTextValues = new CreateAnFTextValues(context);
        anFTextValues.setConfidential(false);
        anFTextValues.setUrgency(false);
        anFTextValues.setTitle(shopName + " offers");
        anFTextValues.setShortMessage("call offers from " + shopName );
        return anFTextValues;
    }
}
