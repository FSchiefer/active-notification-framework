package de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling;

import android.content.Context;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.AnFMessage;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.MessageParts;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.AnFOpenHandler;

/**
 * Created by Felix Schiefer on 04.01.2016.
 */
public class MessageDAO extends AnFOpenHandler {

    private MessageParts anFMessageParts;
    private long id;
    private AnFMessage anFMessage;

    public MessageDAO(Context context) {
        super(context);
    }

    public void messageSent() {
        this.updateSentStatus(id);
    }

    public void messageRead() {
        this.updateReadStatus(id);
    }

    public MessageParts getAnFMessageParts() {
        return anFMessageParts;
    }

    public AnFMessage getAnFMessage() {

        if (anFMessage == null) {
            anFMessage = AnFMessage.getMessage(context, anFMessageParts);
        }
        return anFMessage;
    }

    public void insert(AnFMessage anFMessage) {
        this.anFMessage = anFMessage;
        this.insertAnFMessage(this);
        this.setAnFMessageParts(anFMessage.getMessageParts());
        this.close();
    }

    public void deleteAnFMessage(){
        this.deleteMessage(id);
        this.close();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAnFMessageParts(MessageParts messageParts) {
        this.anFMessageParts = messageParts;
    }
}
