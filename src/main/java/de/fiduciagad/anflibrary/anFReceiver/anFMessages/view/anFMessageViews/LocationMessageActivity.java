package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageViews;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.MessageParts;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.ViewConstants;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageViews.messageFragments.LocationDependencyFragment;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.PositionDependency;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageViews.messageFragments.AnFTextFragment;
import de.fiduciagad.anflibrary.R;
import de.fiduciagad.anflibrary.anFReceiver.anFPermissions.CheckPositionPermissions;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to Display a default position Activity with a Google Maps fragment with all
 * sent addresses
 */
public class LocationMessageActivity extends FragmentActivity implements LocationDependencyFragment.OnFragmentInteractionListener, AnFTextFragment.OnFragmentInteractionListener, OnMapReadyCallback {

    private PositionDependency positionDependency;
    private static final String CLASS_NAME = LocationMessageActivity.class.getSimpleName();
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_message);
        Bundle bundle = getIntent().getExtras();
        Resources res = this.getResources();
        String rawMessage = bundle.getString(ViewConstants.MESSAGE_EXTRA);
        JSONObject jsonMessage = null;

        long starttime = System.currentTimeMillis();
        /*AnFMessage anFMessage = AnFMessage.getMessage(this, rawMessage);*/
        Log.i(CLASS_NAME, "Duration: " + (System.currentTimeMillis() - starttime));

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        try {
            jsonMessage = new JSONObject(rawMessage);
            MessageParts messageParts = new MessageParts(this);
            messageParts.generateMessageParts(jsonMessage);

            if (jsonMessage != null) {
                String extra;
                if (messageParts.getAnFText() != null) {
                    extra = messageParts.getAnFText().getAnFJSONObject().toString();
                    AnFTextFragment textFragment = AnFTextFragment.newInstance(extra);
                    fragmentTransaction.add(R.id.mofTextFragment, textFragment);
                }

                if (messageParts.getPositionDependency() != null) {
                    positionDependency = messageParts.getPositionDependency();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                    if (mapFragment != null)
                        mapFragment.getMapAsync(this);
                }

                fragmentTransaction.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            // For showing a move to my location button permission is needed
            CheckPositionPermissions positionPermissions = new CheckPositionPermissions();
            if (positionPermissions.permissionAllowed(this)) {
                try {
                    googleMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    //TODO: Add Exception handling
                }
            } else {
                map = googleMap;
                positionPermissions.providePermissionRequestDialog(this);
            }
            for (Address address : positionDependency.getAddresses()) {

                if (address != null) {
                    // TODO: think about extending the place message part with a value for the snippet
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title(positionDependency.getPlaceName()).snippet("sent by the message"));
                }
            }
        }
    }

    // This method get's called if a permission request get's answered
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        CheckPositionPermissions positionPermissions = new CheckPositionPermissions();
        if (positionPermissions.permissionAllowed(this)) {
            if (map != null)
                try {
                    map.setMyLocationEnabled(true);
                } catch (SecurityException e) {

                }
        }
    }
}
