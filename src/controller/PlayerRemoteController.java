package controller;

import board.Tile;
import player.Player;
import player.PlayerAI;
import player.PlayerRemote;
import server.*;
import server.message.GameStash;
import server.message.Message;
import server.message.MessageType;

public class PlayerRemoteController extends PlayerAIController {

    NetworkActionListener networkActionListener;

    private PlayerRemote playerRemote;

    /**
     * Constructor for new {@code PlayerAIController}
     * Has access to {@link Player} instance - bridges communication between Player view and model
     *
     * @param player               {@code Player} model entity
     * @param parentListener       Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     */
    public PlayerRemoteController(PlayerAI player, GameController parentListener, NetworkActionListener networkActionListener) {
        super(player, parentListener);
        this.playerRemote = (PlayerRemote) player;
        this.networkActionListener = networkActionListener;
        //intialiseRemote();
    }

    /**
     * Update client on what has happened in the game. Turn then handled on client, will receive information from client about their turn in {@link #endTurnFromRemote(Object)}
     */
    @Override
    public void startTurn() {
        //send message to remote client saying turn started what happened in last turn.
        Object gameStash = parentListener.getStash();
        networkActionListener.sendMessageToRemote(new Message(MessageType.GAME_STATE, gameStash));
    }

    /**
     * Send initial message to client with some information needed to construct game
     */
    public void initialiseRemote(){
        //send game setup info so client can create gameCOntroller
        networkActionListener.setPlayerRemoteController(this);
        //create message about game
        Message initMessage = new Message(MessageType.READY_TO_START, parentListener.getRemoteInitMessage());
        networkActionListener.sendMessageToRemote(initMessage);

    }


    /**
     * Called from {@link NetworkActionListener} when move data received from remote. Use this data to update this game instance.
     * @param remoteStash Game stash received from {@link GameController#getStash()} called on remote machine and passed over network to local
     * @return
     */
    public void endTurnFromRemote(GameStash remoteStash) {
        //get back state of game and turn made by remote and update
        //TODO

        Tile fromTile = parentListener.getTileFromNumber(remoteStash.pieceMoved().fromTileNumber());
        Tile toTile = parentListener.getTileFromNumber(remoteStash.pieceMoved().toTileNumber());
        lastRoll = remoteStash.lastRoll();
        makeMove(toTile);

        //Tile toMoveTo = parentListener.updateFromStash(remoteStash);

    }



}
