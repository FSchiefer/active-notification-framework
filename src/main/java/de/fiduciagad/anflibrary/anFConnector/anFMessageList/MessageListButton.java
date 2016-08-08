package de.fiduciagad.anflibrary.anFConnector.anFMessageList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Felix Schiefer on 30.01.2016.
 * Mit diesem Button wird die Message List Acitivity eingebunden.
 */
public class MessageListButton extends Button {

    private static final String CLASS_NAME = MessageListButton.class.getSimpleName();

    Context context;

    public MessageListButton(Context context) {
        super(context);
        this.setText("NofMessageList");
        this.context = context;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageList(v);
            }
        });
    }

    public void showMessageList(View view) {
        Intent intent = new Intent(context, MessageListActivity.class);

        context.startActivity(intent);
    }
}
