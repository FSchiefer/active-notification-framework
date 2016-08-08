package de.fiduciagad.anflibrary.anFReceiver.anFHandling.anFNotificationTrigger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextInterface;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.deviceCheck.BatteryRequestCheck;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver.ContextResolver;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDAO;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDB;

import java.util.List;

/**
 * Created by Felix Schiefer on 17.12.2015.
 * Klasse für die Darstellung der Nachrichten.
 */
public class InstantMessageTriggerService extends IntentService implements ContextInterface {

    private static final String CLASS_NAME = InstantMessageTriggerService.class.getSimpleName();

    private Context mActivity;
    private MessageDB messageDB;

    private BatteryRequestCheck check;

    private ContextResolver contextResolver;

    public InstantMessageTriggerService() {
        super(CLASS_NAME);
    }

    // Anhand von einem Broadcast ändernde Geschwindigkeit der Aktualisierungen um auch Ortsaabhängigkeiten im Auto finden zu können
    // Der Thread läuft ständig solange nachrichten noch nicht gesendet sind und schaut ob die Nachricht getriggert ist/ gesendet werden kann.
    @Override
    public void setContext(ContextAnswer answer) {
        Log.d(CLASS_NAME, "Context Set");

        List<MessageDAO> messages = null;
        TriggerHelper helper = new TriggerHelper(contextResolver, answer);

        if (helper.onlyUrgentMessages()) {
            messages = messageDB.getUrgentMessages();
        } else {
            messages = messageDB.getMessages();
        }

        helper.sendMessages(messages, this);

        messages = messageDB.getMessages();
        if (messages.size() != 0) {
            Log.d(CLASS_NAME, "Service waits");
            waitForContextLevel();
        }
    }

    private void waitForContextLevel() {
        ContextAwaitRunnable awaitRunnable = new ContextAwaitRunnable(this);
        Thread t = new Thread(awaitRunnable);
        t.start();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        setIntentRedelivery(true);
        mActivity = getApplicationContext();
        messageDB = new MessageDB(mActivity);
        check = new BatteryRequestCheck(mActivity);

        Log.d(CLASS_NAME, "Unsentmessages: " + messageDB.getMessages().size() + " UnsentUrgentMessages: " + messageDB.getUrgentMessages().size());

        if (contextResolver == null) {
            contextResolver = ContextResolver.getInstance(mActivity);
        }

        int messageCount = contextResolver.batteryStatusOk() ? messageDB.getMessages().size() : messageDB.getUrgentMessages().size();

        if (messageCount > 0) {

            Log.d(CLASS_NAME, "Service Started");

            contextResolver.getContext(this);
        } else {
            Log.e(CLASS_NAME, "No service needed");
        }
    }
}
