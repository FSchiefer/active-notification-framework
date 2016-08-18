package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

import android.content.Context;
import android.content.res.Resources;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;

import de.fiduciagad.anflibrary.R;

/**
 * With this class you can create a preference checkbox for a user to allow the delivery
 * of confidential messages to this device
 */
public class ConfidentialSetting extends CheckBoxPreference {

 public ConfidentialSetting(Context context, AttributeSet attrs,
   int defStyleAttr) {
  super(context, attrs, defStyleAttr);
 }

 /**
  * This constructor can be used to create the default values for the confidential settings on this
  * device
  *
  * @param context The application context
  * @param service The name of the service for that a user want's to make the setting
  */
 public ConfidentialSetting(Context context, String service) {
  super(context);
  setValues(service);
 }
 /**
  * This method is made to set the values of the confidential setting
  * @param service The name of the service for that a user want's to make the setting
  */
 private void setValues(String service) {
  Resources res = getContext().getResources();

  this.setKey(service + res.getString(R.string.confidential_key));
  this.setChecked(true);
  this.setDefaultValue(true);
  this.setSummary(res.getString(R.string.useConfidential));
  this.setTitle(res.getString(R.string.confidential));
 }

}
