package de.fiduciagad.anflibrary.anFConnector.anFInterfaces;

/**
 * This interface needs to be implemented to receive answers for messages from the framework
 */
public interface ReceiverInterface {

    /**
     * @param message Message given as String
     * @param ID      The ID of the message in the database
     */
    public void receiveMessage(String message, int ID);
}
