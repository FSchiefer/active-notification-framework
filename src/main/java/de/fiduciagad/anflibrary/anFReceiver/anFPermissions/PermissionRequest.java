package de.fiduciagad.anflibrary.anFReceiver.anFPermissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Felix Schiefer on 16.08.2016.
 * This class is used to encapsulate the android 6 permission request system
 */
public class PermissionRequest {
    private String permission;
    private int permission_request_code;

    /**
     * This constructor is used to register permission and permission_request_code
     *
     * @param permission              The name of the permission that should be used
     * @param permission_request_code The number of the permission request, for handling
     */
    public PermissionRequest(String permission, int permission_request_code) {
        this.permission = permission;
        this.permission_request_code = permission_request_code;
    }

    /**
     * This method is used to check if permission is granted
     *
     * @param context Application context
     * @return True if permission is granted and false if not
     */
    public boolean permissionAllowed(Context context) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This method is used to create a request dialog for the permission if needed
     *
     * @param mActivity Activity context for access on UI-Thread
     */
    public void providePermissionRequestDialog(Activity mActivity) {
        if (!permissionAllowed(mActivity)) {
            ActivityCompat.requestPermissions(mActivity, new String[]{permission}, permission_request_code);
        }
    }

    /**
     * This method is used to create a request dialog for the permission if needed and return an information the application whether it was needed
     *
     * @param mActivity Activity context for access on UI-Thread
     * @return True if request was needed and false if no request was needed.
     */
    public boolean permissionRequestProvided(Activity mActivity) {
        if (!permissionAllowed(mActivity)) {
            ActivityCompat.requestPermissions(mActivity, new String[]{permission}, permission_request_code);
            return true;
        }
        return false;
    }

    public int getPermission_request_code() {
        return permission_request_code;
    }
}
