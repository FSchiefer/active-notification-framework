package de.fiduciagad.anflibrary.anFReceiver.anFPermissions;

import android.Manifest;

/**
 * Created by Felix Schiefer on 15.08.2016.
 * This class is used to encapsulate the permission requests for the permission READ_CALENDAR
 */
public class CheckCalendarPermissions extends PermissionRequest {
    private static final String CLASS_NAME = CheckCalendarPermissions.class.getSimpleName();

    public CheckCalendarPermissions() {
        super(Manifest.permission.READ_CALENDAR, 234);
    }
}
