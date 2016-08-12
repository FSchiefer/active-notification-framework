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
 */
public class AnFMessageTest extends ActivityTestCase {

    private Context context;
    private ContextAnswer answer;

    public void setUp() throws Exception {
        super.setUp();

        context = getInstrumentation().getContext();

        assertNotNull(context);
    }

    public void testPositionMessageSender() {
        AnFConfiguration anFConfiguration = new AnFConfiguration(context);
        JSONObject jsonObject = sendAldiMessage().getJSONObject();
        AnFMessage anFMessage = AnFMessage.getMessage(context, jsonObject);
        if (anFConfiguration.addService("Aldi")) {
            anFMessage = AnFMessage.getMessage(context, jsonObject);
            assertEquals(true, anFMessage.isValid());
        } else {
            assertEquals(true, anFMessage.isValid());
        }
    }

    public void testInvalidPositionMessageSender() {
        AnFConfiguration anFConfiguration = new AnFConfiguration(context);
        JSONObject jsonObject = sendInvalidAldiMessage().getJSONObject();
        AnFMessage anFMessage = AnFMessage.getMessage(context, jsonObject);
        if (anFConfiguration.addService("Aldi")) {
            anFMessage = AnFMessage.getMessage(context, jsonObject);
            assertEquals(null, anFMessage);
        } else {
            assertEquals(null, anFMessage);
        }
    }

    public void testAnFMessageSender() {
        AnFConfiguration anFConfiguration = new AnFConfiguration(context);
        JSONObject jsonObject = sendRealMessage().getJSONObject();
        AnFMessage anFMessage = AnFMessage.getMessage(context, jsonObject);
        if (anFConfiguration.addService("real")) {
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
        createAnFMessage.setService("real");
        JSONObject jsonObject = createAnFMessage.getJSONObject();
        AnFMessage anFMessage = AnFMessage.getMessage(context, jsonObject);

        if (anFConfiguration.addService("real")) {
            anFMessage = AnFMessage.getMessage(context, jsonObject);
            assertEquals(null, anFMessage);
        } else {
            assertEquals(null, anFMessage);
        }
    }

    private CreateAnFMessage sendAldiMessage() {
        CreateAnFMessage anFMessage = new CreateAnFMessage(context);
        anFMessage.setService("Aldi");
        anFMessage.setMofText(createAnFTextValues("Aldi"));
        CreatePositionDependencyValues positionDependencyValues = new CreatePositionDependencyValues(context);
        positionDependencyValues.setTrigger("1000");
        positionDependencyValues.addPlace("Killisfeldstraße 46", "76227", "Aldi");
        positionDependencyValues.addPlace("Waldstraße 14-18", "76133", "Aldi");
        positionDependencyValues.addPlace("Tiengener Straße 2", "76227", "Aldi");
        positionDependencyValues.addPlace("Theodor-Rehbock-Straße 15", "76131 ", "Aldi");
        positionDependencyValues.addPlace("Freiburger Straße 15", "77652", "Aldi");
        positionDependencyValues.addPlace("Heinrich-Hertz-Straße 3", "77656 ", "Aldi");
        positionDependencyValues.addPlace("Carl Blos Straße 2", "77654", "Aldi");
        anFMessage.setLocation(positionDependencyValues);
        return anFMessage;
    }

    private CreateAnFMessage sendInvalidAldiMessage() {
        CreateAnFMessage anFMessage = new CreateAnFMessage(context);
        anFMessage.setService("Aldi");
        anFMessage.setMofText(createAnFTextValues("Aldi"));
        CreatePositionDependencyValues positionDependencyValues = new CreatePositionDependencyValues(context);
        positionDependencyValues.setTrigger("1000");
        positionDependencyValues.addPlace("Killisfeldstraße 46", "76227", "Aldi");
        positionDependencyValues.addPlace("Waldstraße 14-18", "76133", "Aldi");
        positionDependencyValues.addPlace("Tiengener Straße 2", "76227", "Aldi");
        positionDependencyValues.addPlace("Theodor-Rehbock-Straße 15", "76131 ", "Aldi");
        positionDependencyValues.addPlace("Freiburger Straße 15", "77652", "Aldi");
        positionDependencyValues.addPlace("Heinrich-Hertz-Stdfaasdasdaße 3", "76 ", "Aldi");
        positionDependencyValues.addPlace("Carl Blos Straße 2", "77654", "Aldi");
        anFMessage.setLocation(positionDependencyValues);
        return anFMessage;
    }

    private CreateAnFMessage sendRealMessage() {
        CreateAnFMessage anFMessage = new CreateAnFMessage(context);
        anFMessage.setService("real");
        anFMessage.setMofText(createAnFTextValues("real"));
        return anFMessage;
    }

    private CreateAnFTextValues createAnFTextValues(String shopName) {
        CreateAnFTextValues anFTextValues = new CreateAnFTextValues(context);
        anFTextValues.setConfidential(false);
        anFTextValues.setUrgency(false);
        anFTextValues.setTitle(shopName + " Angebote");
        anFTextValues.setShortMessage("Angebote von " + shopName + " abrufen");
        return anFTextValues;
    }
}
