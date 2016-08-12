package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageViews.messageFragments;

import android.app.Activity;
import android.app.Fragment;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.PositionDependency;
import de.fiduciagad.anflibrary.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationDependencyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationDependencyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationDependencyFragment extends MapFragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POS_DEPENDENCY = "param1";

    // TODO: Rename and change types of parameters
    private PositionDependency positionDependency;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment LocationDependencyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationDependencyFragment newInstance(String param1) {
        LocationDependencyFragment fragment = new LocationDependencyFragment();
        Bundle args = new Bundle();
        args.putString(POS_DEPENDENCY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public LocationDependencyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LocationDependency", "OnCreate");
        if (getArguments() != null) {
            if (getArguments() != null) {
                getPositionDependency(getArguments().getString(POS_DEPENDENCY));
            }
        }
    }

    private void getPositionDependency(String posDepString) {
        JSONObject posDependencyJSON = null;
        try {
            posDependencyJSON = new JSONObject(posDepString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        positionDependency = new PositionDependency(posDependencyJSON, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_location_message, container, false);

        Log.d("LocationDependency", "OnCreateView");

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            // For showing a move to my loction button
            googleMap.setMyLocationEnabled(true);

            // For dropping a marker at a point on the Map
            for (Address address : positionDependency.getAddresses()) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title(positionDependency.getPlaceName()).snippet("Gesendet von der Nachricht"));
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument service and name
        public void onFragmentInteraction(Uri uri);
    }
}
