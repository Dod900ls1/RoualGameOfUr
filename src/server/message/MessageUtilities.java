package server.message;

import controller.GameController;
import server.NetworkActionListener;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonGenerator;
import java.util.List;
import java.util.Map;

/**
 * Utility class to provide functions for sending/receiving messages across network
 */
public abstract class MessageUtilities {

    /**
     * Parses JSON text from {@link NetworkActionListener#din} stream to {@link Message} record to be processed by local
     * @param messageJSON JSON text received over data input stream
     * @return {@code Message} instance containing parsed {@code messageJSON}
     */
    public static Message parseMessageJSON(JsonObject messageJSON){
        //Message.data is type Object so we can use find Message.type from messageJSON - match this to a record maybe telling us what fields expect JSON string received to have so can parse string. Create a new instance of this record and add to Message as Message.data
        Message parsedMessage  = null;
        MessageType messageType = MessageType.valueOf(messageJSON.get("messageType").toString());
        switch (messageType){
            case GAME_STATE -> parsedMessage = readGameStashJSON(messageJSON, MessageType.GAME_STATE);
            case ASSIGN_COLOR -> parsedMessage=readAssignColourJSON(messageJSON);
            case READY_TO_START -> parsedMessage=readReadyToStartJSON(messageJSON);
            case GAME_OVER -> parsedMessage = readGameStashJSON(messageJSON, MessageType.GAME_OVER);
        }

        return parsedMessage;

    }




    /**
     * Generates JSON string from {@code Message} record created locally to be sent to {@link NetworkActionListener} on remote through local {@link NetworkActionListener#dout} data stream
     * @param messageObj Message to convert to JSON
     * @return Generated JSON containing {@code messageObj} data
     */
    public static void writeMessageJSON(Message messageObj, JsonGenerator generator){
        if (messageObj.type().equals(MessageType.GAME_STATE)){
            writeGameStashJSON(messageObj, generator);
        } else if (messageObj.type().equals(MessageType.ASSIGN_COLOR)) {
            writeAssignColourJSON(messageObj, generator);
        }else if (messageObj.type().equals(MessageType.READY_TO_START)){
            writeReadyToStartJSON(messageObj, generator);
        } else if (messageObj.type().equals(MessageType.GAME_OVER)) {
            writeGameStashJSON(messageObj, generator);
        } else{
            return;
        }
    }


    private static void writeReadyToStartJSON(Message readyToStartMessage, JsonGenerator generator) {
        generator.writeStartObject();
        generator.write("messageType", readyToStartMessage.type().toString());
        generator.writeEnd();
        generator.flush();
    }
    private static Message readReadyToStartJSON(JsonObject messageJSON) {
        return new Message(
                MessageType.READY_TO_START,
                null
        );
    }
    public static void writeAssignColourJSON(Message assignColourMessage, JsonGenerator generator){
        generator.writeStartObject();
        generator.write("messageType", assignColourMessage.type().toString());
        generator.write("colour", (int)assignColourMessage.data());
        generator.writeEnd();
        generator.flush();
    }

    private static Message readAssignColourJSON(JsonObject messageJSON){
        return new Message(
                MessageType.ASSIGN_COLOR,
                messageJSON.getInt("colour")
        );
    }



    private static void writeGameStashJSON(Message gameStashMessage, JsonGenerator generator) {
        GameStash stash = (GameStash) gameStashMessage.data();
        generator.writeStartObject()
            .write("messageType", gameStashMessage.type().toString())
            .write("lastRoll", stash.lastRoll());
    
        if (stash.lastRoll() != 0 && stash.pieceMoved() != null) {
            generator.writeStartObject("pieceMoved")
                .write("player", stash.pieceMoved().player())
                .write("fromTileNumber", stash.pieceMoved().fromTileNumber())
                .write("toTileNumber", stash.pieceMoved().toTileNumber())
                .writeEnd();
        }
    
        generator.writeEnd();
        generator.flush();
    }
    
    private static Message readGameStashJSON(JsonObject messageJSON, MessageType type) {
        int lastRoll = messageJSON.getInt("lastRoll");
    
        if (lastRoll == 0 || !messageJSON.containsKey("pieceMoved")) {
            return new Message(
                    type,
                    new GameStash(lastRoll, null)
            );
        } else {
            JsonObject pieceMoved = messageJSON.getJsonObject("pieceMoved");
            return new Message(
                    type,
                    new GameStash(
                            lastRoll,
                            new GameController.PieceMoveForStash(
                                    pieceMoved.getInt("player"),
                                    pieceMoved.getInt("fromTileNumber"),
                                    pieceMoved.getInt("toTileNumber")
                            )
                    )
            );
        }
    }

    private JsonObject mapToJson(Map<Integer, List<Integer>> map) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            Integer key = entry.getKey();
            List<Integer> value = entry.getValue();
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (Integer intValue : value) {
                jsonArrayBuilder.add(intValue);
            }
            jsonObjectBuilder.add(key.toString(), jsonArrayBuilder.build());
        }

        return jsonObjectBuilder.build();
    }

}
