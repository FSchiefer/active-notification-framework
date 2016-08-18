package de.fiduciagad.anflibrary.anFConnector.anFInterfaces;

import android.graphics.Bitmap;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.NotificationType;

/**
 * An Interface to define wich images should be used for notifications or the
 * message list.
 */
public interface AnFImagesInterface {

 /**
  * This method is used to set an icon for notifications.
  *
  * @param service
  *            The Service where the image should be used
  * @param notificationType
  *            The type of the notification: ShortMessage oder Message with
  *            position dependency
  * @return Small image ID for notification or message list icons
  */
 int getSmallIcon(String service, NotificationType notificationType);

 /**
  * This method is used to set icons for stacked notifications.
  *
  * @param service
  *            The Service where the image should be used
  * @param notificationType
  *            The type of the notification: ShortMessage oder Message with
  *            position dependency
  * @return Small image ID for notification or message list icons
  */
 Bitmap getBigIcon(String service, NotificationType notificationType);

}
