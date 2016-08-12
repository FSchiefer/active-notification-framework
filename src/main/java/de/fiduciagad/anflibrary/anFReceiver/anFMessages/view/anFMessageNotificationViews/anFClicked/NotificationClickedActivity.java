package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.anFClicked;

import android.content.Context;
import android.content.Intent;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.ViewConstants;

/**
 * Created by Felix Schiefer on 16.01.2016.
 */
public class NotificationClickedActivity {
    private static final String CLASS_NAME = NotificationClickedActivity.class.getSimpleName();

    /**
     * @param context
     * @param id
     * @return Das Intent wird an die Notification als Reply gesetzt. Sobald ein klick auf das Notification
     * Ribbon durchgeführt wird. Über den AnFConnector wird die passende Instanz des MessageBuilderInterface
     * genutzt.
     */
    public static Intent getMessageActivity(Context context, int id) {

        Intent clickedService = new Intent(context, NotificationClickedService.class);
        clickedService.putExtra(ViewConstants.ID_EXTRA, id);

        return clickedService;
    }
}
