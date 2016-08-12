package de.fiduciagad.anflibrary.anFReceiver.anFContextDetection;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Felix on 27.12.2015.
 */
public class Constants {

    public static final String PACKAGE_NAME = "de.fiduciagad.anflibrary";

    public static final String ACTIVITY_PACKAGE_NAME = PACKAGE_NAME + ".ActivityRecognition";

    public static final String BROADCAST_ACTION = ACTIVITY_PACKAGE_NAME + ".BROADCAST_ACTION";

    public static final String ACTIVITY_EXTRA = ACTIVITY_PACKAGE_NAME + ".ACTIVITY_EXTRA";

    public static final String LOCATIONPACKAGE_NAME = PACKAGE_NAME + ".LocationAwareness";
    public static final String RECEIVER = LOCATIONPACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = LOCATIONPACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = LOCATIONPACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static String getName(int detected_activity_type) {
        switch (detected_activity_type) {
            case DetectedActivity.IN_VEHICLE:
                return "in vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on bike";
            case DetectedActivity.ON_FOOT:
                return "on foot";
            case DetectedActivity.WALKING:
                return "walking";
            case DetectedActivity.RUNNING:
                return "running";
            case DetectedActivity.TILTING:
                return "tilting";
            case DetectedActivity.STILL:
                return "still";
            default:
                return "unknown";
        }
    }
}
