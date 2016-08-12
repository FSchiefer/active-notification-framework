package de.fiduciagad.anflibrary.anFReceiver.anFStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import de.fiduciagad.anflibrary.anFConnector.AnFConnector;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.AnFMessage;
import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFMessageHandling.MessageDAO;

/**
 * Created by Felix Schiefer on 10.11.2015.
 * Datenbankcontroller für das Speichern von empfangenen AnF Nachrichten
 */
public class AnFOpenHandler extends SQLiteOpenHelper {

    private static final String TAG = AnFOpenHandler.class.getSimpleName();

    public static final String _ID = "_id";
    public static final String SERVICE = "service";
    public static final String MESSAGE = "message";
    /*    private static final String RECEIVEDDATE = "messageReceivedDate";*/
    public static final String SETDATE = "messageSetDate";
    public static final String SENT = "sent";
    /*    private static final String RECEIVEDPOSTION = "receivedPosition";*/
    public static final String URGENT = "urgent";
    public static final String READ = "read";
    public static final String POSITIONTRIGGERED = "positionTriggered";

    private static final String DATABASE_NAME = "anfmessage.db";
    private static final int DATABASE_VERSION = 1;

    protected static final String TABLE_NAME_ANFMESSAGE = "anfmessages";
    private static final String TABLE_ANFMESSAGE_CREATE = "CREATE TABLE " + TABLE_NAME_ANFMESSAGE + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SERVICE + " STRING, " + MESSAGE + " STRING, " + SETDATE + " INTEGER, " + SENT + " INTEGER, " + POSITIONTRIGGERED + " INTEGER, " + READ + " INTEGER, " + URGENT + " INTEGER);";
    private static final String TABLE_ANFMESSAGE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME_ANFMESSAGE;

    protected static final String TABLE_NAME_ANFSERVICE = "anfservices";
    private static final String TABLE_ANFSERVICE_CREATE = "CREATE TABLE " + TABLE_NAME_ANFSERVICE + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SERVICE + " STRING unique);";
    private static final String TABLE_ANFSERVICE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME_ANFSERVICE;

    protected Context context;

    protected AnFOpenHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_ANFMESSAGE_CREATE);
        db.execSQL(TABLE_ANFSERVICE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrade der Datenbank von Version " + oldVersion + " zu " + newVersion + "; alle Daten werden gelöscht");
        db.execSQL(TABLE_ANFMESSAGE_DROP);
        db.execSQL(TABLE_ANFSERVICE_DROP);
        onCreate(db);
    }

    protected Cursor querySentMessages(boolean urgent) {
        SQLiteDatabase db = getWritableDatabase();
        if (urgent) {
            return db.query(TABLE_NAME_ANFMESSAGE, null, SENT + " = 1" + " AND " + POSITIONTRIGGERED + " = 0 AND " + URGENT + " =" + 1, null, null, null, SETDATE + " DESC");
        }
        return db.query(TABLE_NAME_ANFMESSAGE, null, SENT + " = 1 AND " + POSITIONTRIGGERED + " = 0", null, null, null, SETDATE + " DESC");
    }

    protected Cursor queryAnFMessages() {
        SQLiteDatabase db = getWritableDatabase();
        return db.query(TABLE_NAME_ANFMESSAGE, null, null, null, null, null, _ID + " DESC");
    }

    protected Cursor querySentMessages(String service, boolean urgent) {
        SQLiteDatabase db = getWritableDatabase();
        if (urgent) {
            return db.query(TABLE_NAME_ANFMESSAGE, null, SERVICE + " = " + "\"" + service + "\"  AND " + POSITIONTRIGGERED + " = 0 AND " + SENT + " = 1" + " AND " + URGENT + " =" + 1, null, null, null, SETDATE + " DESC");
        }
        return db.query(TABLE_NAME_ANFMESSAGE, null, SERVICE + " = " + "\"" + service + "\"  AND " + POSITIONTRIGGERED + " = 0 AND " + SENT + " = 1", null, null, null, SETDATE + " DESC");
    }

    protected Cursor queryUnsentMessages(boolean urgent) {
        SQLiteDatabase db = getWritableDatabase();
        if (urgent) {
            return db.query(TABLE_NAME_ANFMESSAGE, null, SENT + " = 0 AND " + POSITIONTRIGGERED + " = 0 AND " + URGENT + " = 1", null, null, null, _ID + " DESC");
        }
        return db.query(TABLE_NAME_ANFMESSAGE, null, POSITIONTRIGGERED + " = 0 AND " + SENT + " = 0", null, null, null, _ID + " DESC");
    }

    protected Cursor queryMessageWithId(long id, boolean urgent) {
        SQLiteDatabase db = getWritableDatabase();
        if (urgent) {
            return db.query(TABLE_NAME_ANFMESSAGE, null, _ID + " = " + id + " AND " + URGENT + " = 1", null, null, null, _ID + " DESC");
        }
        return db.query(TABLE_NAME_ANFMESSAGE, null, _ID + " = " + id, null, null, null, _ID + " DESC");
    }

    /**
     * @param service
     * @return Methode um alle noch ungelesenen Notifications des selben Service zu erhalten
     */
    protected Cursor queryUnreadMessages(String service) {
        SQLiteDatabase db = getWritableDatabase();
        if (AnFConnector.isSeperateByService()) {
            return db.query(TABLE_NAME_ANFMESSAGE, null, SERVICE + " = " + "\"" + service + "\"  AND " + READ + " = " + 0 + " AND " + SENT + " = 1", null, null, null, _ID + " DESC");
        }
        return db.query(TABLE_NAME_ANFMESSAGE, null, READ + " = " + 0 + " AND " + SENT + " = 1", null, null, null, _ID + " DESC");
    }

    protected Cursor queryPositionTriggeredMessages() {
        SQLiteDatabase db = getWritableDatabase();

        return db.query(TABLE_NAME_ANFMESSAGE, null, POSITIONTRIGGERED + " = 1", null, null, null, _ID + " DESC");
    }

    protected long insertAnFMessage(MessageDAO messageDAO) {
        long rowId = -1;
        AnFMessage anFMessage = messageDAO.getAnFMessage();

        try (SQLiteDatabase db = getWritableDatabase()) {
            Log.d(TAG, "Pfad: " + db.getPath());
            ContentValues values = new ContentValues();
            values.put(SERVICE, anFMessage.getService());
            values.put(MESSAGE, anFMessage.getAnfPayload().toString());
            values.put(SETDATE, System.currentTimeMillis());
            values.put(SENT, 0);
            values.put(READ, 0);
            if (anFMessage.getAnFText().getUrgency()) {
                values.put(URGENT, 1);
            } else {
                values.put(URGENT, 0);
            }
            if (anFMessage.getPositiondependency() != null) {
                if (anFMessage.isTriggerNeeded()) {
                    Log.d(TAG, "Message is positionTriggered");
                    values.put(POSITIONTRIGGERED, 1);
                } else {
                    values.put(POSITIONTRIGGERED, 0);
                }
            } else {
                values.put(POSITIONTRIGGERED, 0);
            }

            rowId = db.insert(TABLE_NAME_ANFMESSAGE, null, values);
        } catch (SQLiteException e) {
            Log.e(TAG, "insertAnFMessage()", e);
        } finally {
            Log.d(TAG, "insertAnFMessage(): rowId=" + rowId + " Type: " + anFMessage.getService());
        }

        messageDAO.setId(rowId);
        return rowId;
    }

    protected void updateSentStatus(long id) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            Log.d(TAG, "Pfad: " + db.getPath());
            ContentValues values = new ContentValues();
            values.put(SENT, 1);
            values.put(SETDATE, System.currentTimeMillis());
            db.update(TABLE_NAME_ANFMESSAGE, values, _ID + "= ?", new String[]{Long.toString(id)});
        } catch (SQLiteException e) {
            Log.e(TAG, "update()", e);
        } finally {
            Log.d(TAG, "update(): rowId=" + id);
        }
    }

    protected void updateReadStatus(long id) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            Log.d(TAG, "Pfad: " + db.getPath());
            ContentValues values = new ContentValues();
            values.put(READ, 1);
            db.update(TABLE_NAME_ANFMESSAGE, values, _ID + "= ?", new String[]{Long.toString(id)});
        } catch (SQLiteException e) {
            Log.e(TAG, "update()", e);
        } finally {
            Log.d(TAG, "update(): rowId=" + id);
        }
    }

    protected void deleteMessage(long id) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            int numDeleted = db.delete(TABLE_NAME_ANFMESSAGE, _ID + " = ?", new String[]{Long.toString(id)});
            Log.d(TAG, "delete(): id=" + id + " -> " + numDeleted);
        } catch (SQLiteException e) {
            Log.e(TAG, "update()", e);
        }
    }

    protected Cursor queryServices() {
        SQLiteDatabase db = getWritableDatabase();
        return db.query(TABLE_NAME_ANFSERVICE, null, null, null, null, null, _ID + " DESC");
    }

    protected long insertServices(String service) {
        long rowId = -1;

        try (SQLiteDatabase db = getWritableDatabase()) {

            Log.d(TAG, "Pfad: " + db.getPath());
            ContentValues values = new ContentValues();
            values.put(SERVICE, service);
            rowId = db.insert(TABLE_NAME_ANFSERVICE, null, values);

            db.close();
        } catch (SQLiteException e) {
            Log.e(TAG, "insertAnFMessage()", e);
        } finally {
            Log.d(TAG, "insertAnFMessage(): rowId=" + rowId);
        }

        return rowId;
    }

    protected Cursor queryService(String service) {
        SQLiteDatabase db = getWritableDatabase();
        return db.query(TABLE_NAME_ANFSERVICE, null, SERVICE + " = " + "\"" + service + "\" ", null, null, null, _ID + " DESC");
    }
}

