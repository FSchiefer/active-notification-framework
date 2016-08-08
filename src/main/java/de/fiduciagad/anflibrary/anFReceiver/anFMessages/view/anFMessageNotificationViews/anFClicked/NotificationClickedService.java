package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.anFClicked;

import android.app.IntentService;
import android.content.Intent;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.MessageParts;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDB;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.ViewConstants;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDAO;
import de.fiduciagad.anflibrary.anFConnector.NoFConnector;
import de.fiduciagad.anflibrary.anFConnector.anFInterfaces.MessageBuilderInterface;

/**
 * Created by Felix Schiefer on 01.02.2016.
 */
public class NotificationClickedService extends IntentService {

    private static final String CLASS_NAME = NotificationClickedService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NotificationClickedService() {
        super(CLASS_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int id = intent.getExtras().getInt(ViewConstants.ID_EXTRA);

        MessageDB messageDB = new MessageDB(this);
        String rawMessage = messageDB.getRawMessage(id);
        MessageBuilderInterface messageBuilderInterface = NoFConnector.getMessageActivity();

        MessageDAO message = new MessageDAO(this);
        message.setId(id);
        message.messageRead();
        MessageParts messageParts = new MessageParts(this);
        messageParts.generateMessageParts(rawMessage);
        startActivity(messageBuilderInterface.getMessageActivity(id, messageParts, this).addFlags(intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
