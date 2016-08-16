package de.fiduciagad.anflibrary.anFReceiver.anFPermissions;

import android.Manifest;

/**
 * Created by Felix Schiefer on 15.08.2016.
 * This class is used to encapsulate the permission requests for the permission ACCESS_FINE_LOCATION
 */
public class CheckPositionPermissions extends PermissionRequest {

    private static final String CLASS_NAME = CheckPositionPermissions.class.getSimpleName();

    public CheckPositionPermissions() {
        super(Manifest.permission.ACCESS_FINE_LOCATION, 123);
    }
}
