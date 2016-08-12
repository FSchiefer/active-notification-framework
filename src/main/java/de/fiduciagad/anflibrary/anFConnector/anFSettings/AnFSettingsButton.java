package de.fiduciagad.anflibrary.anFConnector.anFSettings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

/**
 * Created by Felix Schiefer on 02.11.2015.
 * Button to start the settings activity
 */
public class AnFSettingsButton extends Button {

    private static final String CLASS_NAME = AnFSettingsButton.class.getSimpleName();

    Context context;

    public AnFSettingsButton(Context context) {
        super(context);
        this.setText("AnFSettings");
        this.context = context;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreferences(v);
            }
        });
    }

    public void showPreferences(View view) {
        Intent intent = new Intent(context, SettingsActivity.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        context.startActivity(intent);
    }
}
