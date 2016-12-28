package com.example.anftest.notificationtestproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.fiduciagad.anflibrary.anFConnector.AnFConfiguration;
import de.fiduciagad.anflibrary.anFConnector.AnFController;
import de.fiduciagad.anflibrary.anFMessageCreator.CreateAnFMessage;
import de.fiduciagad.anflibrary.anFMessageCreator.CreateAnFTextValues;
import de.fiduciagad.anflibrary.anFMessageCreator.CreatePositionDependencyValues;
import de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts.Place;
import de.fiduciagad.anflibrary.anFReceiver.anFPermissions.PermissionQuickCheck;

public class MainActivity extends AppCompatActivity {
    private String defaultService = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // This object is used to provide an Interface for setting framework configurations to the
        // developer. The only mandatory step for a developer is enabling the services he
        // want's to provide to a user
        AnFConfiguration anFConfiguration = new AnFConfiguration(this);
        anFConfiguration.addService(defaultService);



        // This Object checks if all needed Permissions for the usage of the user are granted.
        // If you don't want to use the context detection on a device with
        // android version 6 or higher, use this object.
        PermissionQuickCheck permissionQuickCheck = new PermissionQuickCheck(this);
        permissionQuickCheck.providePermissionRequestDialogIfNeeded(this);

        // Not relevant for the usage of the AnF Framework
        uiSetup();
    }



    // In the following methods everything is provided, that is needed to send an message to the
    // framework. Every message that gets provided to the user check's the current status of a User.

    /**
     * This methods provides an simple message to the framework with all needed basic content
     */
    public void sendAnFMessage(){
        AnFController controller = new AnFController(this);
        controller.handleMessage(createAnFMessage());
    }


    /**
     * This method provides an message with a place to the framework.
     * The transmissioned place can be used to display more content.
     */
    public void sendLocationAnFMessage(){
        AnFController controller = new AnFController(this);
        CreateAnFMessage anFMessage = createAnFMessage();
        anFMessage.setLocation(createPositionDependencyValues());
        controller.handleMessage(anFMessage);
    }

    /**
     * This method provides an location triggerd AnFMessage to the framework.
     * The functions: createPositionDependencyValues and CreateAnFMessage are used to
     * create the basic content
     */
    public void sendLocationTriggeredAnFMessage(){
        AnFController controller = new AnFController(this);
        CreateAnFMessage anFMessage = createAnFMessage();
        CreatePositionDependencyValues positionDependencyValues = createPositionDependencyValues();
        // This line says, that the message gets triggered if a user enters an area 200 Meters around
        // any place.
        positionDependencyValues.setTrigger("200");
        anFMessage.setLocation(positionDependencyValues);
        controller.handleMessage(anFMessage);
    }

    /**
     *  This method creates an Object with one or many places. More places can be added with the add
     *  Place function.
     * @return An object with the information for one or many places.
     */
    private CreatePositionDependencyValues createPositionDependencyValues(){
        CreatePositionDependencyValues positionDependencyValues = new CreatePositionDependencyValues(this);
        positionDependencyValues.addPlace("Prinzessenstr. 1", "76227", "Address to be transimitted in the message");
        return positionDependencyValues;
    }

    /**
     * Create an AnFMessage Object with all basic message Elements like Title, assigned service and Shortmessage.
     * The provided information is all that is needed to display an anf message.
     * @return An created basic AnFMessage Object
     */
    private CreateAnFMessage createAnFMessage(){
        CreateAnFMessage anFMessage = new CreateAnFMessage(this);
        anFMessage.setService(defaultService);

        CreateAnFTextValues anFTextValues = new CreateAnFTextValues(this);
        anFTextValues.setTitle("Hey tester");
        anFTextValues.setShortMessage("This is your simple Message");

        anFMessage.setAnFText(anFTextValues);
        return anFMessage;
    }



    /**
     * This method is only used to connect buttons with the functions to send AnFMessages
     */
    private void uiSetup() {
        Button defaultMessageButton = (Button) findViewById(R.id.defaultMessage);
        defaultMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAnFMessage();
            }
        });

        Button positionTriggeredMessageButton = (Button) findViewById(R.id.positionMessage);
        positionTriggeredMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLocationTriggeredAnFMessage();
            }
        });

        Button locationMessageButton = (Button) findViewById(R.id.positionMessageButton);
        locationMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLocationAnFMessage();
            }
        });
    }
}
