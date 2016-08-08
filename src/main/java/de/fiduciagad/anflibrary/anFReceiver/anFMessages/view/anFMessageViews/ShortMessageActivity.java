package de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageViews;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.MessageParts;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.ViewConstants;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.view.anFMessageViews.messageFragments.AnFTextFragment;
import de.fiduciagad.noflibrary.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Die durch das Framework bereitgestellte standard Activity um Nachrichten ohne Ortsinformation darzustellen
 */
public class ShortMessageActivity extends Activity implements AnFTextFragment.OnFragmentInteractionListener {
    private LinearLayout verticalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_message);

        Bundle bundle = getIntent().getExtras();

        Resources res = this.getResources();
        String rawMessage = bundle.getString(ViewConstants.MESSAGE_EXTRA);
        JSONObject jsonMessage = null;

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        try {
            jsonMessage = new JSONObject(rawMessage);
            MessageParts messageParts = new MessageParts(this);
            messageParts.generateMessageParts(jsonMessage);

            if (jsonMessage != null) {
                String extra;
                if (messageParts.getAnFText() != null) {
                    extra = messageParts.getAnFText().getNofJSONObject().toString();
                    AnFTextFragment textFragment = AnFTextFragment.newInstance(extra);
                    fragmentTransaction.add(R.id.listFragment, textFragment);
                }

                fragmentTransaction.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        verticalLayout = (LinearLayout) findViewById(R.id.VerticalLayout);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
