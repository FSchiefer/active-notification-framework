package de.fiduciagad.anflibrary.anFConnector.anFMessageList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

/**
 * With this button the MessageListActivity can be implemented
 */
public class MessageListButton extends Button {

    private static final String CLASS_NAME = MessageListButton.class.getSimpleName();

    Context context;

    public MessageListButton(Context context) {
        super(context);
        this.setText("AnfMessageList");
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
