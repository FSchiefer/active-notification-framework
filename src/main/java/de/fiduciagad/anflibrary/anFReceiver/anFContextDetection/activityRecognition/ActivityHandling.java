package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.activityRecognition;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.contextResolver.ContextResolverInterfaces.ActivityInterface;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.Constants;
import de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.googleApiHandling.GoogleApiHandling;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

/**
 * This class is used to control the handle the google API for activity detection of a user
 */
public class ActivityHandling extends GoogleApiHandling implements ResultCallback<Status> {

    private ActivityInterface activityInterface;

    public ActivityHandling(Context context, GoogleApiClient gClient, ActivityInterface activityInterface) {
        super(context, gClient);
        this.activityInterface = activityInterface;
        mReceiver = new ActivityDetectionBroadcastReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
    }

    @Override
    public void connectHandling() {
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(gClient, 20000, getActivityDetectionPendingIntent()).setResultCallback(this);
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(mContext, DetectedActivitiesIntentService.class);

        return PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void setDetectedActivity(DetectedActivity detectedActivity) {

        boolean resolverB = (activityInterface != null);
        boolean ActivityB = (detectedActivity != null);

        Log.i(TAG, "Activity is set activityInterface is " + resolverB + " detectedActivity is " + ActivityB);
        if (activityInterface != null && detectedActivity != null)
            activityInterface.setActivity(detectedActivity);
    }

    @Override
    public void onResult(Status status) {

    }

    public void disconnect() {
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(gClient, getActivityDetectionPendingIntent()).setResultCallback(this);
    }

    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver {
        protected static final String TAG = "activity-detection-response-receiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            DetectedActivity updatedActivity = intent.getParcelableExtra(Constants.ACTIVITY_EXTRA);

            setDetectedActivity(updatedActivity);
        }
    }
}
