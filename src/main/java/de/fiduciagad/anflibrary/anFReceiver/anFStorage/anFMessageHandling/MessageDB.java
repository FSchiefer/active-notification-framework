package de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.MessageParts;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.AnFMessage;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.AnFOpenHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix Schiefer on 04.01.2016.
 */
public class MessageDB extends AnFOpenHandler {

    private static final String CLASS_NAME = MessageDB.class.getSimpleName();

    private Context context;
    private List<MessageDAO> messages;
    private List<MessageDAO> urgentMessages;

    public MessageDB(Context context) {
        super(context);
        this.context = context;
    }

    public AnFMessage getMessage(long id) {

        Cursor c = this.queryMessageWithId(id, false);
        int ciMessage = c.getColumnIndex(AnFOpenHandler.MESSAGE);
        AnFMessage message = null;
        while (c.moveToNext()) {
            message = AnFMessage.getMessage(context, c.getString(ciMessage));
        }
        c.close();
        this.close();
        return message;
    }

    public String getRawMessage(long id) {
        String rawMessage = null;

        Cursor c = this.queryMessageWithId(id, false);

        int ciMessage = c.getColumnIndex(AnFOpenHandler.MESSAGE);

        while (c.moveToNext()) {
            rawMessage = c.getString(ciMessage);
        }
        c.close();
        this.close();
        return rawMessage;
    }

    public List<MessageDAO> getMessages() {
        messages = new ArrayList<>();
        Cursor c = this.queryUnsentMessages(false);
        getMessagesFromCursor(c, messages);
        c.close();
        this.close();
        Log.d(CLASS_NAME, " Messages found " + messages.size());
        return messages;
    }

    public List<MessageDAO> getUnreadMessages(String service) {
        messages = new ArrayList<>();
        Cursor c = this.queryUnreadMessages(service);
        getMessagesFromCursor(c, messages);
        c.close();
        this.close();
        Log.d(CLASS_NAME, " Messages found " + messages.size());
        return messages;
    }

    public List<MessageDAO> getUrgentMessages() {
        urgentMessages = new ArrayList<>();

        Cursor c = this.queryUnsentMessages(true);
        getMessagesFromCursor(c, urgentMessages);
        c.close();
        this.close();
        Log.d(CLASS_NAME, " Messages found " + urgentMessages.size());
        return urgentMessages;
    }

    public List<MessageDAO> getMessagesById(ArrayList<String> messageId) {
        List<MessageDAO> requestedMessages = new ArrayList<>();
        Log.d(CLASS_NAME, " Messages found in messageIdList " + messageId.size());
        for (String id : messageId) {
            Cursor c = this.queryMessageWithId(Integer.parseInt(id), false);
            getMessagesFromCursor(c, requestedMessages);
            Log.d(CLASS_NAME, " Messages found in Cursor " + c.getCount());
            c.close();
        }
        this.close();
        return requestedMessages;
    }

    public List<MessageDAO> getUrgentMessagesById(ArrayList<String> messageId) {
        List<MessageDAO> requestedMessages = new ArrayList<>();
        for (String id : messageId) {
            Cursor c = this.queryMessageWithId(Integer.parseInt(id), true);
            getMessagesFromCursor(c, requestedMessages);
            c.close();
        }
        this.close();
        return requestedMessages;
    }

    public List<MessageDAO> getPositionDependentMessages() {
        List<MessageDAO> requestedMessages = new ArrayList<>();

        Cursor c = this.queryPositionTriggeredMessages();
        getMessagesFromCursor(c, requestedMessages);
        c.close();

        this.close();
        return requestedMessages;
    }

    public Cursor queryMessageCursor(long id, boolean urgent) {
        return queryMessageWithId(id, urgent);
    }

    public Cursor queryMessageCursor(boolean urgent) {
        return querySentMessages(urgent);
    }

    public Cursor queryMessageCursor(String service, boolean urgent) {
        return querySentMessages(service, urgent);
    }

    public void delete(long id) {
        deleteMessage(id);
    }

    public Cursor query() {
        return queryNoFMessages();
    }

    private void getMessagesFromCursor(Cursor c, List<MessageDAO> messageList) {
        int ciID = c.getColumnIndex(AnFOpenHandler._ID);
        int ciMessage = c.getColumnIndex(AnFOpenHandler.MESSAGE);

        Log.d(CLASS_NAME, "ciID " + ciID + " Cursor Count " + c.getCount());

        long starttime = System.currentTimeMillis();

        while (c.moveToNext()) {
            MessageDAO message = new MessageDAO(context);
            MessageParts m = new MessageParts(context);
            m.generateMessageParts(c.getString(ciMessage));
            message.setId(c.getInt(ciID));
            message.setNoFMessageParts(m);
            messageList.add(message);
            Log.d(CLASS_NAME, " Messages found " + messageList.size());
        }

        Log.d(CLASS_NAME, "Duration: " + (System.currentTimeMillis() - starttime));
    }
}
