package controller.action.game;

import player.PlayerOptions;
import server.ServerActionListener;

import java.awt.event.ActionEvent;

public class GameStartedWithServer extends ActionEvent {

    /**
     * Passed with {@code GameStarted} event as {@code source}
     * @param playerOptions Player setup options to be passed to {@link game.UrGame UrGame} constructor
     */
    public record GameStartedWithServerEventSource(PlayerOptions[] playerOptions, ServerActionListener serverListener){}

    public GameStartedWithServer(GameStartedWithServer.GameStartedWithServerEventSource source){
        super(source, ActionEvent.ACTION_PERFORMED, "Game started with server");
    }
}
