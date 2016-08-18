package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

import android.content.Context;
import android.content.res.Resources;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;

import de.fiduciagad.anflibrary.R;

//TODO FS add javadoc
public class UrgencySetting extends CheckBoxPreference {

 public UrgencySetting(Context context, AttributeSet attrs, int defStyleAttr) {
  super(context, attrs, defStyleAttr);
 }

 public UrgencySetting(Context context, String service) {
  super(context);
  setValues(service);
 }

 private void setValues(String service) {
  Resources res = getContext().getResources();

  this.setKey(service + res.getString(R.string.urgency_key));
  this.setChecked(true);
  this.setDefaultValue(true);
  this.setSummary(res.getString(R.string.useUrgency));
  this.setTitle(res.getString(R.string.urgency));
 }

}
