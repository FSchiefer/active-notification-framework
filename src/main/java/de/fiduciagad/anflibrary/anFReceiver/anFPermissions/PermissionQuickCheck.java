package de.fiduciagad.anflibrary.anFReceiver.anFPermissions;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Felix on 19.09.2016.
 * This Class is used to provide a way to make
 * a quickcheck to make sure that all needed permissions are granted. And to provide
 * a dialog for the needed permissions.
 * If you want to get the result for only one Permission use the Classes: CheckCalendarPermissions
 * or CheckCalendarPermissions
 */
public class PermissionQuickCheck {

    private static String TAG = PermissionQuickCheck.class.getSimpleName().toString();

    private Context context;
    private CheckPositionPermissions positionPermissions;
    private CheckCalendarPermissions calendarPermissions;
    private int permission_request_code;


    public PermissionQuickCheck(Context context) {
        this.context = context;
        positionPermissions = new CheckPositionPermissions();
        calendarPermissions = new CheckCalendarPermissions();
        permission_request_code = 434;
    }

    /**
     * This Function provides a quickcheck with no proviced dialog
     *
     * @return The information if all wanted permissions are granted
     */
    public boolean allNeededPermissionsGranted() {
        return (positionPermissions.permissionAllowed(context) && calendarPermissions.permissionAllowed(context));
    }

    /**
     * This Function is used to provide a permissionRequestDialog for all needed Permissions
     *
     * @param activity The activity where the result of the request should be returned
     * @return The used PermissionRequestCode
     */
    public int providePermissionRequestDialogIfNeeded(Activity activity) {
        boolean positionpermissionGranted = positionPermissions.permissionAllowed(context);
        boolean calendarPermissionGranted = calendarPermissions.permissionAllowed(context);

        Log.e(TAG,"Position: " + positionpermissionGranted + " Calendar: " + calendarPermissionGranted);
        if (!positionpermissionGranted && !calendarPermissionGranted) {
            ActivityCompat.requestPermissions(activity, new String[]{positionPermissions.getPermission(), calendarPermissions.getPermission()}, permission_request_code);
            return permission_request_code;
        } else if (!positionpermissionGranted && calendarPermissionGranted) {
            positionPermissions.providePermissionRequestDialog(activity);
            return positionPermissions.getPermission_request_code();
        } else if (positionpermissionGranted && !calendarPermissionGranted) {
            calendarPermissions.providePermissionRequestDialog(activity);
            return calendarPermissions.getPermission_request_code();
        }

        return -1;
    }
}
