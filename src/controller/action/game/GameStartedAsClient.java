package controller.action.game;

import player.PlayerOptions;
import server.ClientActionListener;

import java.awt.event.ActionEvent;

public class GameStartedAsClient extends ActionEvent {

    /**
     * Passed with {@code GameStarted} event as {@code source}
     *
     * @param playerOptions        Player setup options to be passed to {@link game.UrGame UrGame} constructor
     * @param clientActionListener
     */
    public record GameStartedAsClientEventSource(PlayerOptions[] playerOptions, ClientActionListener clientActionListener){}

    public GameStartedAsClient(GameStartedAsClient.GameStartedAsClientEventSource source){
        super(source, ActionEvent.ACTION_PERFORMED, "Game started as client");
    }
}
