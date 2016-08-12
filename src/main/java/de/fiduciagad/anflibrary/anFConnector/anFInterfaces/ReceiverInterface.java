package de.fiduciagad.anflibrary.anFConnector.anFInterfaces;

/**
 * This interface needs to be implemented to receive answers for messages from the framework
 */
public interface ReceiverInterface {

    /**
     * The method is used to receive textanswers directly from notifications with short answers
     *
     * @param message Message given as String
     * @param ID      The ID of the message in the database
     */
    void receiveMessage(String message, int ID);
}
