package server;

import controller.GameController;
import controller.PlayerRemoteController;
import player.Player;
import server.message.GameStash;
import server.message.Message;
import server.message.MessageUtilities;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public abstract class NetworkActionListener implements ActionListener {
    protected PlayerRemoteController playerRemoteController;

    protected Socket remoteSocket;

    protected DataInputStream din;
    protected DataOutputStream dout;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private StringWriter jsonStringWriter;
    private StringReader jsonStringReader;

    Thread listeningThread;
    private JsonGenerator jsonGenerator;
    //private JsonParser jsonParser;

    public void listen() throws IOException {
            while (true){
                if (getDin().available()>0){
                    String message = getDin().readUTF();
                    System.out.println(message);
                    jsonStringReader = new StringReader(message);
                    jsonReader = Json.createReaderFactory(null).createReader(jsonStringReader);
                    receiveMessageFromRemote();
                }
            }
    }


    public void startListening(){
        listeningThread = new Thread(){
            @Override
            public void run() {
                try {
                    //jsonWriter = Json.createWriterFactory(null).createWriter(getDout());
                    jsonStringWriter = new StringWriter();
                    jsonStringReader = new StringReader("");
                    jsonGenerator = Json.createGenerator(jsonStringWriter);
                    jsonReader = Json.createReaderFactory(null).createReader(jsonStringReader);
                    //jsonParser = Json.createParser(jsonStringReader);
                    listen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        listeningThread.setDaemon(true);
        listeningThread.start();
    }

    public void setPlayerRemoteController(PlayerRemoteController playerRemoteController){
        this.playerRemoteController = playerRemoteController;
    }

    //todo how does this get called?
    void receiveMessageFromRemote(){
        //parse JSON to Message

        JsonObject messageJSON = jsonReader.readObject();
        Message fromRemote = MessageUtilities.parseMessageJSON(messageJSON);
        processMessageFromRemote(fromRemote);

    }

    public void sendMessageToRemote(Message message){
        //generate JSON message
        jsonStringWriter = new StringWriter();
        jsonGenerator = Json.createGenerator(jsonStringWriter);
        MessageUtilities.writeMessageJSON(message, jsonGenerator);
        System.out.println(jsonStringWriter.toString());
        try {
            getDout().writeUTF(jsonStringWriter.toString());
            getDout().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //jsonWriter.writeObject(messageJSON);

//            dout.writeUTF(jsonText);
//        try {
//            getDout().flush();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

    public int getRemoteColourFromLocalColour(int localColour){
        int remoteColour;
        if (localColour== Player.LIGHT_PLAYER){
            remoteColour = Player.DARK_PLAYER;
        }
        else{
            remoteColour = Player.LIGHT_PLAYER;
        }
        return remoteColour;
    }

    abstract void processMessageFromRemote(Message message);

    /**
     * Receives game state from remote: calls {@link controller.PlayerRemoteController#endTurnFromRemote(Object)} to update game via {@link GameController#updateFromStash(String)},
     * ends move for remote player and begins turn for local player
     * @param remoteStash
     */
    protected void receiveGameState(Message remoteStashMessage) {
        //todo
        //receives game state - updates game, then starts turn for client as PlayerHuman
        GameStash remoteStash = (GameStash) remoteStashMessage.data();
        playerRemoteController.endTurnFromRemote(remoteStash);
    }


    protected DataOutputStream getDout() throws IOException {
        return new DataOutputStream(remoteSocket.getOutputStream());
    }

    protected DataInputStream getDin() throws IOException {
        return new DataInputStream(remoteSocket.getInputStream());
    }



}
