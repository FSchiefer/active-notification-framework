package de.fiduciagad.anflibrary;

import android.content.Context;
import android.test.ActivityTestCase;

import de.fiduciagad.anflibrary.anFConnector.NoFConfiguration;
import de.fiduciagad.anflibrary.anFMessageCreator.CreateAnFTextValues;
import de.fiduciagad.anflibrary.anFMessageCreator.CreateNoFMessage;
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
        NoFConfiguration noFConfiguration = new NoFConfiguration(context);
        JSONObject jsonObject = sendAldiMessage().getJSONObject();
        AnFMessage anFMessage = AnFMessage.getMessage(context, jsonObject);
        if (noFConfiguration.addService("Aldi")) {
            anFMessage = AnFMessage.getMessage(context, jsonObject);
            assertEquals(true, anFMessage.isValid());
        } else {
            assertEquals(true, anFMessage.isValid());
        }
    }

    public void testInvalidPositionMessageSender() {
        NoFConfiguration noFConfiguration = new NoFConfiguration(context);
        JSONObject jsonObject = sendInvalidAldiMessage().getJSONObject();
        AnFMessage anFMessage = AnFMessage.getMessage(context, jsonObject);
        if (noFConfiguration.addService("Aldi")) {
            anFMessage = AnFMessage.getMessage(context, jsonObject);
            assertEquals(null, anFMessage);
        } else {
            assertEquals(null, anFMessage);
        }
    }

    public void testNoFMessageSender() {
        NoFConfiguration noFConfiguration = new NoFConfiguration(context);
        JSONObject jsonObject = sendRealMessage().getJSONObject();
        AnFMessage anFMessage = AnFMessage.getMessage(context, jsonObject);
        if (noFConfiguration.addService("real")) {
            anFMessage = AnFMessage.getMessage(context, jsonObject);
            assertEquals(true, anFMessage.isValid());
        } else {
            assertEquals(true, anFMessage.isValid());
        }
    }

    public void testNoService() {

        CreateNoFMessage noFMessage = new CreateNoFMessage(context);
        noFMessage.setService("Imaginaer");
        noFMessage.setMofText(createMoFTextValues("imaginaer"));
        JSONObject jsonObject = noFMessage.getJSONObject();
        AnFMessage message = AnFMessage.getMessage(context, jsonObject);
        assertEquals(null, message);
    }

    public void testInvalidMessage() {

        NoFConfiguration noFConfiguration = new NoFConfiguration(context);
        CreateNoFMessage createNoFMessage = new CreateNoFMessage(context);
        createNoFMessage.setService("real");
        JSONObject jsonObject = createNoFMessage.getJSONObject();
        AnFMessage anFMessage = AnFMessage.getMessage(context, jsonObject);

        if (noFConfiguration.addService("real")) {
            anFMessage = AnFMessage.getMessage(context, jsonObject);
            assertEquals(null, anFMessage);
        } else {
            assertEquals(null, anFMessage);
        }
    }

    private CreateNoFMessage sendAldiMessage() {
        CreateNoFMessage noFMessage = new CreateNoFMessage(context);
        noFMessage.setService("Aldi");
        noFMessage.setMofText(createMoFTextValues("Aldi"));
        CreatePositionDependencyValues positionDependencyValues = new CreatePositionDependencyValues(context);
        positionDependencyValues.setTrigger("1000");
        positionDependencyValues.addPlace("Killisfeldstraße 46", "76227", "Aldi");
        positionDependencyValues.addPlace("Waldstraße 14-18", "76133", "Aldi");
        positionDependencyValues.addPlace("Tiengener Straße 2", "76227", "Aldi");
        positionDependencyValues.addPlace("Theodor-Rehbock-Straße 15", "76131 ", "Aldi");
        positionDependencyValues.addPlace("Freiburger Straße 15", "77652", "Aldi");
        positionDependencyValues.addPlace("Heinrich-Hertz-Straße 3", "77656 ", "Aldi");
        positionDependencyValues.addPlace("Carl Blos Straße 2", "77654", "Aldi");
        noFMessage.setLocation(positionDependencyValues);
        return noFMessage;
    }

    private CreateNoFMessage sendInvalidAldiMessage() {
        CreateNoFMessage noFMessage = new CreateNoFMessage(context);
        noFMessage.setService("Aldi");
        noFMessage.setMofText(createMoFTextValues("Aldi"));
        CreatePositionDependencyValues positionDependencyValues = new CreatePositionDependencyValues(context);
        positionDependencyValues.setTrigger("1000");
        positionDependencyValues.addPlace("Killisfeldstraße 46", "76227", "Aldi");
        positionDependencyValues.addPlace("Waldstraße 14-18", "76133", "Aldi");
        positionDependencyValues.addPlace("Tiengener Straße 2", "76227", "Aldi");
        positionDependencyValues.addPlace("Theodor-Rehbock-Straße 15", "76131 ", "Aldi");
        positionDependencyValues.addPlace("Freiburger Straße 15", "77652", "Aldi");
        positionDependencyValues.addPlace("Heinrich-Hertz-Stdfaasdasdaße 3", "76 ", "Aldi");
        positionDependencyValues.addPlace("Carl Blos Straße 2", "77654", "Aldi");
        noFMessage.setLocation(positionDependencyValues);
        return noFMessage;
    }

    private CreateNoFMessage sendRealMessage() {
        CreateNoFMessage noFMessage = new CreateNoFMessage(context);
        noFMessage.setService("real");
        noFMessage.setMofText(createMoFTextValues("real"));
        return noFMessage;
    }

    private CreateAnFTextValues createMoFTextValues(String shopName) {
        CreateAnFTextValues moFTextValues = new CreateAnFTextValues(context);
        moFTextValues.setConfidential(false);
        moFTextValues.setUrgency(false);
        moFTextValues.setTitle(shopName + " Angebote");
        moFTextValues.setShortMessage("Angebote von " + shopName + " abrufen");
        return moFTextValues;
    }
}
