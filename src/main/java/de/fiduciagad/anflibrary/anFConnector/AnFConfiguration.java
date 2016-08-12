package de.fiduciagad.anflibrary.anFConnector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import de.fiduciagad.anflibrary.anFReceiver.anFStorage.anFServiceHandling.ServiceDB;
import de.fiduciagad.anflibrary.R;

/**
 * This class is used to configure the Framework.
 * Here the default settings can be saved for home and workplace of a user and the services are added to the database
 */
public class AnFConfiguration {
    private Context context;
    private ServiceDB serviceDB;
    private Resources resources;
    private SharedPreferences preferences;

    public AnFConfiguration(Context context) {
        this.context = context;
        resources = context.getResources();
        serviceDB = new ServiceDB(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * With this function the home address can be set in the settings
     *
     * @param address Home address of the user as String
     */
    public void setUserHomeAddress(String address) {
        setAddress(R.string.home_key, address);
    }

    /**
     * With this function the workplace address can be set in the settings
     *
     * @param address Workplace address of the user as String
     */
    public void setUserWorkAddress(String address) {
        setAddress(R.string.work_key, address);
    }

    private void setAddress(int keyId, String address) {
        if (preferences.getString(resources.getString(keyId), "").equals("")) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(resources.getString(keyId), address);
            editor.commit();
        }
    }

    /**
     * With this function a service that is used to identify messages that are valid for the framework can be registered.
     *
     * @param service Service which is is stored in the service db for further use in the framework
     */
    public boolean addService(String service) {
        return serviceDB.insert(service);
    }
}
