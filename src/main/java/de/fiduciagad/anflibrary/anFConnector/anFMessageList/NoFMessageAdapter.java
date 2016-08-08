package de.fiduciagad.anflibrary.anFConnector.anFMessageList;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.NotificationType;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.AnFText;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.PositionDependency;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.AnFOpenHandler;
import de.fiduciagad.noflibrary.R;
import de.fiduciagad.anflibrary.anFConnector.NoFConnector;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Felix Schiefer on 30.01.2016.
 * Adapter to display messages in the message list
 */
 class NoFMessageAdapter extends CursorAdapter {

    private static final String CLASS_NAME = NoFMessageAdapter.class.getSimpleName();

    private int iD;

    private final Date date;

    private static final DateFormat DF_DATE = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);

    private static final DateFormat DF_TIME = SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM);

    private LayoutInflater inflator;

    private Context context;

    public NoFMessageAdapter(Context context) {
        super(context, null, 0);
        this.context = context;
        date = new Date();
        inflator = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflator.inflate(R.layout.message_list_element, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int ciType = cursor.getColumnIndex(AnFOpenHandler.SERVICE);
        int ciMessage = cursor.getColumnIndex(AnFOpenHandler.MESSAGE);
        int ciTime = cursor.getColumnIndex(AnFOpenHandler.SETDATE);
        int ciSent = cursor.getColumnIndex(AnFOpenHandler.SENT);
        int ciID = cursor.getColumnIndex(AnFOpenHandler._ID);
        Resources res = context.getResources();

        AnFText anFText = null;
        PositionDependency positionDependency = null;
        JSONObject payload = null;
        String service = null;
        try {
            payload = new JSONObject(cursor.getString(ciMessage));
            JSONObject mofTextJson = payload.getJSONObject(res.getString(R.string.anfText));
            anFText = new AnFText(mofTextJson, context);
            service = payload.getString(res.getString(R.string.serviceType));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            JSONObject posDependency = payload.getJSONObject(res.getString(R.string.positionDependency));
            positionDependency = new PositionDependency(posDependency, context);
        } catch (JSONException e) {
       /*     e.printStackTrace();*/
        }

        iD = cursor.getInt(ciID);
        Log.i(CLASS_NAME, " Cursor " + cursor.getPosition() + " Service Value " + cursor.getString(ciType) + " ID " + cursor.getInt(ciID) + " Moftext " + anFText.getTitle() + "Send " + cursor.getInt(ciSent));

        if (anFText != null && cursor.getInt(ciSent) == 1) {
            ImageView image = (ImageView) view.findViewById(R.id.icon);
            image.setImageResource(NoFConnector.getNoFImages(context).getSmallIcon(service, NotificationType.SHORT));

            if (positionDependency != null) {
                image.setImageResource(NoFConnector.getNoFImages(context).getSmallIcon(service, NotificationType.LOCATION));
            }

            TextView textView1 = (TextView) view.findViewById(R.id.text1);
            TextView textView2 = (TextView) view.findViewById(R.id.text2);
            TextView textView3 = (TextView) view.findViewById(R.id.text3);
            TextView textView4 = (TextView) view.findViewById(R.id.text4);

            long timeMillis = cursor.getLong(ciTime);
            date.setTime(timeMillis);
            textView1.setText(anFText.getTitle());
            textView2.setText(anFText.getShortMessage());
            textView3.setText(DF_DATE.format(date));
            textView4.setText(DF_TIME.format(date));
        }
    }

    public int getiD() {
        return iD;
    }
}
