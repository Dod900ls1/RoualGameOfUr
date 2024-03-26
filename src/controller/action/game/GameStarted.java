package controller.action.game;

import controller.MenuController;
import player.PlayerOptions;

import java.awt.event.ActionEvent;

/**
 * Fired to {@code GameController} when game is ready to be started
 */
public class GameStarted extends ActionEvent {

    /**
     * Passed with {@code GameStarted} event as {@code source}
     * @param playerOptions Player setup options to be passed to {@link game.UrGame UrGame} constructor
    */
    public record GameStartedEventSource(PlayerOptions[] playerOptions){}

    public GameStarted(GameStartedEventSource source){
        super(source, ActionEvent.ACTION_PERFORMED, "Game started");
    }

    public GameStarted(Object source, int id, String command) {
        super(source, id, command);
    }
}
