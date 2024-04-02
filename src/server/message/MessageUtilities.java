package server.message;

import controller.GameController;
import server.NetworkActionListener;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
            case GAME_STATE -> parsedMessage = readGameStateJSON(messageJSON);
        }

        return parsedMessage;

    }

    private static Message readGameStateJSON(JsonObject messageJSON) {

        JsonObject pieceMoved = messageJSON.getJsonObject("pieceMoved");

        return new Message(
            MessageType.GAME_STATE,
            new GameStash(
                    messageJSON.getInt("lastRoll"),
                    new GameController.PieceMoveForStash(
                            pieceMoved.getInt("player"),
                            pieceMoved.getInt("fromTileNumber"),
                            pieceMoved.getInt("toTileNumber")
                    )
            )
        );

    }

    /**
     * Generates JSON string from {@code Message} record created locally to be sent to {@link NetworkActionListener} on remote through local {@link NetworkActionListener#dout} data stream
     * @param messageObj Message to convert to JSON
     * @return Generated JSON containing {@code messageObj} data
     */
    public static JsonObject writeMessageJSON(Message messageObj){
        JsonObject messageJSON = null;
        if (messageObj.data() instanceof GameStash){
            messageJSON = writeGameStashJSON(messageObj.type(), (GameStash)messageObj.data());
        }

        return messageJSON;
    }

    private static JsonObject writeGameStashJSON(MessageType type, GameStash data) {
        JsonObjectBuilder stashBuilder = Json.createObjectBuilder();
        stashBuilder.add("messageType", type.toString());
        stashBuilder.add("lastRoll", data.lastRoll());

        JsonObjectBuilder pieceMoveBuilder = Json.createObjectBuilder();
        pieceMoveBuilder.add("player", data.pieceMoved().player());
        pieceMoveBuilder.add("fromTileNumber", data.pieceMoved().fromTileNumber());
        pieceMoveBuilder.add("toTileNumber", data.pieceMoved().toTileNumber());

        stashBuilder.add("pieceMoved", pieceMoveBuilder.build());
        return stashBuilder.build();
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
