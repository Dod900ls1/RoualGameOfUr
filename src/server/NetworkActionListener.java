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
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class NetworkActionListener implements ActionListener {
    protected PlayerRemoteController playerRemoteController;

    protected Socket remoteSocket;

    protected DataInputStream din;
    protected DataOutputStream dout;

    public void setPlayerRemoteController(PlayerRemoteController playerRemoteController){
        this.playerRemoteController = playerRemoteController;
    }

    //todo how does this get called?
    void receiveMessageFromRemote(){
        //parse JSON to Message
        //todo listening thread?
        String messageString = null;
        try (JsonReader inReader = Json.createReader(getDin())){
            JsonObject messageJSON = inReader.readObject();
            Message fromRemote = MessageUtilities.parseMessageJSON(messageJSON);
            processMessageFromRemote(fromRemote);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    public void sendMessageToRemote(Message message){
        //generate JSON message

        JsonObject messageJSON = MessageUtilities.writeMessageJSON(message);

        try (JsonWriter outWriter = Json.createWriter(getDout())){
            outWriter.writeObject(messageJSON);
//            dout.writeUTF(jsonText);
//            dout.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
