package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.anFClicked;

import android.app.IntentService;
import android.content.Intent;

import de.fiduciagad.anflibrary.anFConnector.AnFConnector;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDAO;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDB;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFServiceHandling.ServiceDB;

/**
 * Created by Felix Schiefer on 01.02.2016.
 */
public class SummaryClickedService extends IntentService {

    private static final String CLASS_NAME = SummaryClickedService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SummaryClickedService() {
        super(CLASS_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent summaryAcitivity = AnFConnector.getSummaryActivity(this);

        setAllRead();
        startActivity(summaryAcitivity.addFlags(intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void setAllRead() {
        MessageDB lists = new MessageDB(this);
        ServiceDB serviceDB = new ServiceDB(this);
        if (!AnFConnector.isSeperateByService()) {
            for (String service : serviceDB.getServiceList()) {
                for (MessageDAO messageMessageDAO : lists.getUnreadMessages(service)) {
                    messageMessageDAO.messageRead();
                }
            }
        } else {

        }
    }
}
