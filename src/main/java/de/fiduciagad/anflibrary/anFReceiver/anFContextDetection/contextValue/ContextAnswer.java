package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.activityRecognition.ActivityEnum;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.locationAwareness.LocationAnswer;

/**
 * Created by Felix Schiefer on 03.01.2016.
 */
public class ContextAnswer {
    private boolean appointmentCurrentlyRunning;
    private ActivityEnum currentActivityValue;
    private Location currentPosition;
    private Context context;
    private boolean atWork = false;
    private boolean atHome = false;

    private boolean watchAvailable = false;
    private boolean displayIsRunning = false;

    public ContextAnswer(Context context) {
        this.context = context;
    }

    public boolean isAppointmentCurrentlyRunning() {
        return appointmentCurrentlyRunning;
    }

    public void setAppointmentCurrentlyRunning(boolean appointmentCurrentlyRunning) {
        this.appointmentCurrentlyRunning = appointmentCurrentlyRunning;
    }

    public ActivityEnum getCurrentActivityValue() {
        return currentActivityValue;
    }

    public void setCurrentActivityValue(int currentActivityValue) {
        this.currentActivityValue = ActivityEnum.getActivity(currentActivityValue);
    }

    public Location getCurrentPosition() {
        return currentPosition;
    }

    public void setDisplayIsRunning(boolean displayIsRunning) {
        this.displayIsRunning = displayIsRunning;
    }

    public void setCurrentPosition(LocationAnswer locAnswer) {
        atHome = locAnswer.isAtHome();
        atWork = locAnswer.isAtWork();
        Log.d("ContextAnswer", " At Home: " + atHome + " at Work " + atWork);
    }

    /**
     * @return Das Kontextlevel als Enum. S0 = Alle nachrichten ohne Signal
     * S1 = Volle Signale für dringliche, Vibration+LED für undringliche
     * S2 = Uhr Vibration für dringliche nur LED für undringliche
     * S3 = Nur LED und Visuell für dringliche undringliche müssen warten
     * S4 = Alle Nachrichten die in dieses Kontextlevel kommen müssen warten
     * S5 = Uhr Vibration, Auto ton für dringliche, undringliche müsse Warten
     */
    public ContextLevelEnum getContextLevel() {
        if (displayIsRunning) {
            return ContextLevelEnum.S0;
        }
        if (atWork) {
            return workContextLevel();
        } else if (atHome) {
            return homeContextLevel();
        }
        return otherContextLevel();
    }

    private ContextLevelEnum workContextLevel() {
        if (!appointmentCurrentlyRunning) {
            return ContextLevelEnum.S2;
        }
        return ContextLevelEnum.S3;
    }

    private ContextLevelEnum homeContextLevel() {
        if (!appointmentCurrentlyRunning) {
            return ContextLevelEnum.S1;
        }
        return ContextLevelEnum.S2;
    }

    /**
     * Diese Funktion liefert den Kontext-Level zurück den ein Nutzer hat
     * der nicht zuhause oder auf der Arbeit ist.
     *
     * @return Der Zustand an einem Ort der nicht zuhause oder auf der Arbeit ist.
     */
    private ContextLevelEnum otherContextLevel() {
        // TODO: Erkennung für einen Fahrer einbauen, aktuell wird davon ausgegangen, dass man kein Fahrer ist
        boolean driver = false;

        if (!appointmentCurrentlyRunning) {
            if (currentActivityValue == ActivityEnum.STILL) {
                return ContextLevelEnum.S1;
            } else if (currentActivityValue == ActivityEnum.WALKING) {
                return ContextLevelEnum.S2;
            } else if (currentActivityValue == ActivityEnum.DRIVING) {
                if (driver) {
                    //TODO: Zustand S4 einbauen wenn ein Nutzer Fahrer ist und kein passendes Gerät hat
                    return ContextLevelEnum.S5;
                }
            }
        }
        return ContextLevelEnum.S2;
    }

    public boolean isWatchAvailable() {
        return watchAvailable;
    }

    public void setWatchAvailable(boolean watchAvailable) {
        this.watchAvailable = watchAvailable;
    }
}
