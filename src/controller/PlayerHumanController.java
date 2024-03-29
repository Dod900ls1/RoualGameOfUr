package controller;

import board.Tile;
import controller.action.game.MoveSelected;
import controller.action.game.RollDice;
import player.Player;
import player.PlayerHuman;

import java.awt.event.ActionEvent;

/**
 * Specialisation of {@link PlayerController} for use with {@link PlayerHuman} players
 */
public class PlayerHumanController extends PlayerController{

    PlayerHuman player;

    /**
     * Constructor for new {@code PlayerHumanController}
     * Has access to {@link Player} instance - bridges communication between Player view and model
     *
     * @param player         {@code Player} model entity
     * @param parentListener Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     */
    public PlayerHumanController(PlayerHuman player, GameController parentListener) {
        super(player, parentListener);
    }

    /**
     * {@code ActionListener} override.
     * May receive events from parent {@code GameController}
     * May pass event up chain of command to {@link GameController} e.g. if player needs to be notified of event
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e instanceof MoveSelected){
            makeMove((Tile)e.getSource());
        } else if (e instanceof RollDice) {
            rollDice();
        }
    }

}
