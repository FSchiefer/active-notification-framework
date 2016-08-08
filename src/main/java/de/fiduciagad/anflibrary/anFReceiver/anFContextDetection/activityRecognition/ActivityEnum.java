package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection.activityRecognition;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Felix Schiefer on 26.02.2016.
 */
public enum ActivityEnum {
    DRIVING, WALKING, STILL;

    public static ActivityEnum getActivity(int detected_activity_type) {

        switch (detected_activity_type) {
            case DetectedActivity.IN_VEHICLE:
                return ActivityEnum.DRIVING;
            case DetectedActivity.ON_BICYCLE:
                return ActivityEnum.DRIVING;
            case DetectedActivity.ON_FOOT:
                return ActivityEnum.WALKING;
            case DetectedActivity.WALKING:
                return ActivityEnum.WALKING;
            case DetectedActivity.RUNNING:
                return ActivityEnum.WALKING;
            case DetectedActivity.TILTING:
                return ActivityEnum.STILL;
            case DetectedActivity.STILL:
                return ActivityEnum.STILL;
            default:
                return ActivityEnum.STILL;
        }
    }
}
