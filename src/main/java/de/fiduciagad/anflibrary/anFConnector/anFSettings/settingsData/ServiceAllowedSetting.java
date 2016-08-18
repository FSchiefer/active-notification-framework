package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

import android.content.Context;
import android.content.res.Resources;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;

import de.fiduciagad.anflibrary.R;


/**
 * This class is used to provide a Setting to allow or deny the receiving of messages of a Service
 */
public class ServiceAllowedSetting extends CheckBoxPreference {

 public ServiceAllowedSetting(Context context, AttributeSet attrs,
   int defStyleAttr) {
  super(context, attrs, defStyleAttr);
 }

 /**
  * This constructor can be used to create the default values for the service receiving message
  * device
  *
  * @param context The application context
  * @param service The name of the service for that a user want's to make the setting
  */
 public ServiceAllowedSetting(Context context, String service) {
  super(context);
  setValues(service);
 }

 /**
  * This method is made to set the values of the service allowed setting
  *
  * @param service The name of the service for that a user want's to make the setting
  */
 private void setValues(String service) {
  Resources res = getContext().getResources();

  this.setKey(service + res.getString(R.string.service_key));
  this.setChecked(true);
  this.setDefaultValue(true);
  this.setSummary(res.getString(R.string.useService));
  this.setTitle(res.getString(R.string.service));
 }

}
