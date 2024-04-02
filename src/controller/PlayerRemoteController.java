package controller;

import board.Tile;
import player.PlayerAI;
import player.PlayerRemote;
import server.ClientActionListener;
import server.Message;
import server.MessageType;
import server.ServerActionListener;

public class PlayerRemoteController extends PlayerAIController {

    ServerActionListener serverActionListener;
    ClientActionListener clientActionListener;

    private PlayerRemote playerRemote;

    /**
     * Constructor for new {@code PlayerAIController}
     * Has access to {@link Player} instance - bridges communication between Player view and model
     *
     * @param player               {@code Player} model entity
     * @param parentListener       Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     * @param serverActionListener
     * @param clientActionListener
     */
    public PlayerRemoteController(PlayerAI player, GameController parentListener, ServerActionListener serverActionListener, ClientActionListener clientActionListener) {
        super(player, parentListener);
        this.playerRemote = (PlayerRemote) player;
        this.serverActionListener = serverActionListener;
        this.clientActionListener = clientActionListener;
        intialiseRemote();
    }

    /**
     * Update client on what has happened in the game. Turn then handled on client, will receive information from client about their turn in {@link #endTurn(Object)}
     */
    @Override
    public void startTurn() {
        //send message to remote client saying turn started what happened in last turn.
        Object lastTurnData = parentListener.getLastTurnInformation();
        serverActionListener.sendMessageToClient(new Message(MessageType.GAME_STATE, lastTurnData));

    }

    /**
     * Send initial message to client with some information needed to construct game
     */
    private void intialiseRemote(){
        //send game setup info so client can create gameCOntroller
        serverActionListener.setPlayerRemoteController(this);
        //create message about game
        Message initMessage = new Message(MessageType.READY_TO_START, parentListener.getRemoteInitMessage());
        serverActionListener.sendMessageToClient(initMessage);

    }


    /**
     * Called from {@link ServerActionListener} when move data received from client. Use this data to update this game instance.
     * @param data
     * @return
     */
    @Override
    public boolean endTurn(Object data) {
        //get back state of game and turn made by remote and update

        parentListener.updateFromStash();

        Tile toMoveTo; //figure out which tile from data - need tile number
        makeMove(toMoveTo);
    }



}
