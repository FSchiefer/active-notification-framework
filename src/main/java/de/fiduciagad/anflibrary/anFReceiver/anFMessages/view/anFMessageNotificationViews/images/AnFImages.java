package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import de.fiduciagad.anflibrary.anFConnector.anFInterfaces.AnFImagesInterface;

/**
 * Created by Felix Schiefer on 10.11.2015.
 */
public class AnFImages implements AnFImagesInterface {
    private Context context;

    public AnFImages(Context context) {
        this.context = context;
    }

    @Override
    public int getSmallIcon(String service, de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.NotificationType notificationType) {

        if (NotificationType.LOCATION.equals(notificationType)) {
            return android.R.drawable.presence_away;
        }

        return android.R.drawable.presence_online;
    }

    @Override
    public Bitmap getBigIcon(String service, de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.NotificationType notificationType) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.presence_online);

        return largeIcon;
    }
}
