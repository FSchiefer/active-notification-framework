package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageViews.MessageActivityBuilder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.MessageParts;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.ViewConstants;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageViews.LocationMessageActivity;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageViews.ShortMessageActivity;
import de.fiduciagad.anflibrary.anFConnector.anFInterfaces.MessageBuilderInterface;

import org.json.JSONObject;

/**
 * Created by Felix Schiefer on 30.01.2016.
 */
public class MessageBuilder implements MessageBuilderInterface {

    @Override
    public Intent getMessageActivity(int id, MessageParts messageParts, Context context) {

        Resources res = context.getResources();
        JSONObject jsonMessage = null;
        Intent activityToOpen = new Intent(context, ShortMessageActivity.class);

        if (messageParts.getPositionDependency() != null) {
            activityToOpen = new Intent(context, LocationMessageActivity.class);
        }

        activityToOpen.putExtra(ViewConstants.MESSAGE_EXTRA, messageParts.getNoFMessage().toString());

        return activityToOpen;
    }
}
