package server;

/**
 * Utility class to provide functions for sending/receiving messages across network
 */
public abstract class MessageUtilities {

    /**
     * Parses JSON text from {@link NetworkActionListener#din} stream to {@link Message} record to be processed by local
     * @param messageString JSON text received over data input stream
     * @return {@code Message} instance containing parsed {@code messageString}
     */
    public static Message parseMessageJSON(String messageString){
        //Message.data is type Object so we can use find Message.type from messageString - match this to a record maybe telling us what fields expect JSON string received to have so can parse string. Create a new instance of this record and add to Message as Message.data
        return new Message(null, null);
    }

    /**
     * Generates JSON string from {@code Message} record created locally to be sent to {@link NetworkActionListener} on remote through local {@link NetworkActionListener#dout} data stream
     * @param messageObj Message to convert to JSON
     * @return Generated JSON containing {@code messageObj} data
     */
    public static String writeMessageJSON(Message messageObj){
        return "";
    }

}
