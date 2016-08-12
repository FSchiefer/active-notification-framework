package de.fiduciagad.anflibrary.anFConnector.anFInterfaces;

import android.content.Context;
import android.content.Intent;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.MessageParts;

/**
 * Created by Felix Schiefer on 30.01.2016.
 * This Interface needs to be implemented if an own Activity is used to display the Notification
 */
public interface  MessageBuilderInterface {

    /**
     * This method is used for a developer to set own Activities for clicked Notifications
     *
     * @param id           The ID of the sent message
     * @param messageParts All Parts of the Message in a usable style
     * @param context      The application context
     * @return An Intent for an Actitivity
     */
    public Intent getMessageActivity(int id, MessageParts messageParts, Context context);
}
