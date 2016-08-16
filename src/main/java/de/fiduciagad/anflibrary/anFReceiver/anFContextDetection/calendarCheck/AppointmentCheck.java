package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.calendarCheck;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.util.Log;

/**
 * Created by Felix on 30.12.2015.
 */
public class AppointmentCheck {

    private static final String TAG = "AppointmentCheck";
    Context context;

    public AppointmentCheck(Context context) {

        this.context = context;
    }

    /**
     * @return Liefert ein true zurück wenn ein Termin läuft und ein false wenn nicht.
     */
    public boolean checkCurrentAppointments() {
        Cursor c;

        try {
            c = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
        } catch(SecurityException securityException){
            Log.e(TAG,"No permission for calendar given. No appointment set");
            return false;
        }
        if (c != null) {
            int indexID = c.getColumnIndex(CalendarContract.Events._ID);
            int indexTitle = c.getColumnIndex(CalendarContract.Events.TITLE);
            int startTime = c.getColumnIndex(CalendarContract.Events.DTSTART);
            int endTime = c.getColumnIndex(CalendarContract.Events.DTEND);
            int allDay = c.getColumnIndex(CalendarContract.Events.ALL_DAY);

            while (c.moveToNext()) {
                if (c.getString(allDay).equals("0")) {
                    long time = System.currentTimeMillis();
                    if (Long.parseLong(c.getString(startTime)) < time && time < Long.parseLong(c.getString(endTime))) {
                        Log.d(TAG, "_ID: " + c.getString(indexID));
                        Log.d(TAG, "TITLE: " + c.getString(indexTitle));
                        Log.d(TAG, "Time: " + time);
                        Log.d(TAG, "Start: " + c.getString(startTime));
                        Log.d(TAG, "End: " + c.getString(endTime));

                        c.close();
                        return true;
                    }
                }
            }

            c.close();
        }
        Log.d(TAG, "No current Appointment");

        return false;
    }
}

