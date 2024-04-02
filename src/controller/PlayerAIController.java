package controller;

import player.Player;
import player.PlayerAI;

import java.awt.event.ActionEvent;
/**
 * Specialisation of {@link PlayerController} for use with {@link PlayerAI} players
 */
public class PlayerAIController extends PlayerController{

    private final PlayerAI player;

    /**
     * Constructor for new {@code PlayerAIController}
     * Has access to {@link Player} instance - bridges communication between Player view and model
     *
     * @param player         {@code Player} model entity
     * @param parentListener Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     */
    public PlayerAIController(PlayerAI player, GameController parentListener) {
        super(player, parentListener);
        this.player = player;
        requiresUserInput = false;

    }

    /**
     * Override of parent.
     * Automatically rolls dice by {@code rollDice}.
     * Calls {@code makeMove} with result of {@link PlayerAI#determineNextTile(int) PlayerAI.determineNextTile(roll)}
     */
    @Override
    public void startTurn(){
        rollDice();
        makeMove(player.determineNextTile(lastRoll));
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public abstract boolean endTurn(Object data);
}
