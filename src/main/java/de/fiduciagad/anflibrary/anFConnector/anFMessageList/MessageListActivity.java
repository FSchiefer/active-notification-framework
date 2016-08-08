package de.fiduciagad.anflibrary.anFConnector.anFMessageList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.AnFText;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageNotificationViews.anFClicked.NotificationClickedActivity;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.AnFOpenHandler;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFServiceHandling.ServiceDB;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDB;
import de.fiduciagad.noflibrary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Felix Schiefer on 30.01.2016.
 * Activity wicht can be implemented in different ways to implement a message list
 */
public class MessageListActivity extends Activity {
    private static final String CLASS_NAME = MessageListActivity.class.getSimpleName();
    private CursorAdapter ca;
    private MessageDB dbHandler;
    private Spinner filterSpinner;
    private List<String> typeList;
    private CheckBox urgentBox;
    private String defaultValue;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultValue = "Alle Services";
        setContentView(R.layout.activity_message_list);
        filterSpinner = (Spinner) findViewById(R.id.FilterSpinner);
        list = (ListView) findViewById(R.id.listView);

        filterSpinner.setAdapter(getAdapter());
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                updateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        urgentBox = (CheckBox) findViewById(R.id.urgentCheckBox);
        urgentBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateList();
            }
        });

        ca = new NoFMessageAdapter(this);
        list.setAdapter(ca);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick(id);
            }
        });

        registerForContextMenu(list);
        dbHandler = new MessageDB(this);
        updateList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHandler.close();
    }

    private void onClick(long id) {

        Intent activityToOpen = NotificationClickedActivity.getMessageActivity(this, (int) id);

        startService(activityToOpen);
    }

    private void updateList() {

        Cursor cursor = null;
        if (filterSpinner.getSelectedItem().toString().equals(defaultValue)) {

            cursor = dbHandler.queryMessageCursor(urgentBox.isChecked());
        } else {

            cursor = dbHandler.queryMessageCursor(filterSpinner.getSelectedItem().toString(), urgentBox.isChecked());
            Log.i(CLASS_NAME, " Cursor " + cursor.getCount() + " Search Value " + filterSpinner.getSelectedItem().toString());
        }

        ca.changeCursor(cursor);
    }

    private ArrayAdapter<String> getAdapter() {
        ServiceDB serviceDB = new ServiceDB(this);
        typeList = serviceDB.getServiceList();
        typeList.add(0, defaultValue);

        String[] labels = typeList.toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, labels);

        return adapter;
    }

    private void getAllEntries() {
        Cursor c = dbHandler.query();
        int ciType = c.getColumnIndex(AnFOpenHandler.SERVICE);
        int ciID = c.getColumnIndex(AnFOpenHandler._ID);
        int ciMessage = c.getColumnIndex(AnFOpenHandler.MESSAGE);

        AnFText anFText = null;

        while (c.moveToNext()) {
            try {
                JSONObject payload = new JSONObject(c.getString(ciMessage));
                JSONObject mofTextJson = payload.getJSONObject("MOFTEXT");
                anFText = new AnFText(mofTextJson, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i(CLASS_NAME, " Cursor " + c.getPosition() + " Service Value " + c.getString(ciType) + " ID " + c.getInt(ciID) + " Moftext " + anFText.getTitle());
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.messagestoragemenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (R.id.openItem == item.getItemId()) {
            onClick(info.id);
            return true;
        } else if (R.id.deleteItem == item.getItemId()) {
            dbHandler.delete(info.id);
            updateList();
            return true;
        }

        return super.onContextItemSelected(item);
    }
}

