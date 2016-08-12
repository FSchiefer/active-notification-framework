package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.activityRecognition.ActivityApiHandling;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.calendarCheck.AppointmentCheck;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver.ContextResolverInterfaces.ActivityInterface;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver.ContextResolverInterfaces.PositionInterface;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver.ContextResolverInterfaces.WatchDetectionInterface;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextAnswer;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextValue.ContextInterface;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.deviceCheck.BatteryRequestCheck;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.deviceCheck.DeviceState;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.deviceCheck.WatchDetection;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.locationAwareness.LocationAnswer;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.locationAwareness.PositionApiHandling;
import de.fiduciagad.anflibrary.R;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Felix on 29.12.2015.
 */
public class ContextResolver implements ActivityInterface, WatchDetectionInterface, PositionInterface {
    int daValue;
    AppointmentCheck appointmentCheck;

    private LocationAnswer locAnswer;
    protected static final String TAG = "Resolver";
    private PositionApiHandling posHandler;
    private ActivityApiHandling actHandler;
    private WatchDetection watchDetection;
    private ContextInterface answer;
    private Context mContext;

    private BatteryRequestCheck check;

    private DeviceState deviceState;
    boolean currentAppointment;
    boolean currentAppointmentInstantiated;

    private boolean smartWatchAvailable;
    private boolean smartWatchAvailableInstantiated;

    private static ContextResolver instance;

    public static synchronized ContextResolver getInstance(Context context) {
        if (ContextResolver.instance == null) {
            ContextResolver.instance = new ContextResolver(context);
        }
        return ContextResolver.instance;
    }

    private ContextResolver(Context context) {
        this.mContext = context;
        appointmentCheck = new AppointmentCheck(context);
        daValue = -1;
        deviceState = new DeviceState(context);
        posHandler = new PositionApiHandling(context, this);
        actHandler = new ActivityApiHandling(context, this);
        watchDetection = new WatchDetection(context, this);
        check = new BatteryRequestCheck(context);
    }

    public void getContext(ContextInterface answer) {
        this.answer = answer;

        watchDetection.run();

        if (!deviceState.isDisplayRunning()) {
            setContextValues();
        } else if (deviceState.isDisplayRunning()) {
            sendAnswerDisplayRunning();
        }
    }

    private void setContextValues() {
        Resources res = mContext.getResources();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (prefs.getBoolean(res.getString(R.string.useCalendarKey), true)) {
            Log.i(TAG, "Calendar Allowed");
            currentAppointment = appointmentCheck.checkCurrentAppointments();
            currentAppointmentInstantiated = true;
        } else {
            currentAppointment = false;
            currentAppointmentInstantiated = true;
            sendAnswer();
        }

        if (prefs.getBoolean(res.getString(R.string.usePositionKey), true)) {
            Log.i(TAG, "PositionsAllowed");
            posHandler.connect();
        } else {
            locAnswer = new LocationAnswer(mContext);
            sendAnswer();
        }

        if (prefs.getBoolean(res.getString(R.string.useActivityRecognitionKey), true)) {
            Log.i(TAG, "ActivityRecognitionAllowed");
            actHandler.connect();
        } else {
            daValue = DetectedActivity.STILL;
            sendAnswer();
        }
    }

    @Override
    public void setActivity(DetectedActivity da) {

        this.daValue = da.getType();
        String activityDetectedString = "Detected in Resolver " + da.getType() + " " + da.getConfidence();
        Log.i(TAG, activityDetectedString);

        actHandler.disconnect();
        Log.i(TAG, "Disconnected");
        sendAnswer();
    }

    @Override
    public void setPosition(LocationAnswer answer) {
        Log.i(TAG, "Loc Answer is set");
        this.locAnswer = answer;
        posHandler.disconnect();
        sendAnswer();
    }

    @Override
    public void setWatchAvailable(Boolean watchAvailable) {
        Log.i(TAG, "WatchAvailable set " + watchAvailable);
        this.smartWatchAvailable = watchAvailable;
        smartWatchAvailableInstantiated = true;
        sendAnswer();
    }

    private void sendAnswer() {
        if (locAnswer != null && daValue != -1 && currentAppointmentInstantiated && smartWatchAvailableInstantiated) {
            ContextAnswer cA = new ContextAnswer(mContext);
            cA.setAppointmentCurrentlyRunning(currentAppointment);
            cA.setCurrentActivityValue(daValue);
            cA.setCurrentPosition(locAnswer);
            cA.setWatchAvailable(smartWatchAvailable);
            resetObjects();
            answer.setContext(cA);
        }
    }

    private void sendAnswerDisplayRunning() {
        ContextAnswer cA = new ContextAnswer(mContext);
        cA.setDisplayIsRunning(deviceState.isDisplayRunning());
        resetObjects();
        answer.setContext(cA);
    }

    private void resetObjects() {
        daValue = -1;
        locAnswer = null;
        currentAppointmentInstantiated = false;
        currentAppointment = false;
        smartWatchAvailableInstantiated = false;
        smartWatchAvailable = false;
    }

    // TODO: Schwellwert in Settings verfügbar machen oder über den Battery Manager beziehen
    public boolean batteryStatusOk() {
        return (check.getBatteryCapacity() > 30 || check.getChargingBoolean());
    }
}
