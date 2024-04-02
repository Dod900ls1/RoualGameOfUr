package server;

import controller.GameController;
import controller.PlayerRemoteController;
import player.Player;

import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class NetworkActionListener implements ActionListener {
    protected PlayerRemoteController playerRemoteController;

    protected DataInputStream din;
    protected DataOutputStream dout;

    public void setPlayerRemoteController(PlayerRemoteController playerRemoteController){
        this.playerRemoteController = playerRemoteController;
    }

    //todo how does this get called?
    void receiveMessageFromRemote(){
        //parse JSON to Message
        //todo listening thread?
        String messageString;
        Message fromRemote = MessageUtilities.parseMessageJSON(messageString);
        processMessageFromRemote(fromRemote);
    }

    public void sendMessageToRemote(Message message){
        //generate JSON message
        String jsonText = MessageUtilities.writeMessageJSON(message);
        try {
            dout.writeUTF(jsonText);
            dout.flush();

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
    protected void receiveGameState(Message remoteStash) {
        //todo
        //receives game state - updates game, then starts turn for client as PlayerHuman
        playerRemoteController.endTurnFromRemote(remoteStash);
    }

}
