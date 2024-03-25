package controller;

import board.Board;
import board.Tile;
import controller.action.MoveMade;
import controller.action.MoveSelected;
import controller.action.RollDiceAction;
import exceptions.IllegalMoveException;
import player.Piece;
import player.Player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller for {@code Player} objects. Links {@code Player} model entity to view
 */
public class PlayerController implements ActionListener {

    /**
     * {@code Player} model for this controller
     */
    final Player player;

    /**
     * Value of the player's last roll.
     * Stored here as dice rolls separate from piece movement in turn sequence
     * May move to inside {@link Player}?
     */
    int lastRoll;

    /**
     * {@code PlayerController} reports to {@link GameController}.
     * If event needs to be responded to from whole game scale, it is reported to {@code GameController}
     */
    final GameController parentListener;


    /**
     * Constructor for new {@code PlayerController}
     * Has access to {@link Player} instance - bridges communication between Player view and model
     * @param player {@code Player} model entity
     * @param parentListener Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     */
    public PlayerController(Player player, GameController parentListener) {
        this.player = player;
        this.parentListener = parentListener;
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
        } else if (e instanceof RollDiceAction) {
            rollDice();
        }
    }

    /**
     * Starts a new thread to execute move in {@code Player} model.
     * Receives {@code Tile} instance selected to move to.
     * Calls {@link Player#makeMove(int, Tile) Player.makeMove} to execute move (checks move is valid and if so, moves the appropriate {@code Piece})
     * When move made, calls {@code fireMoveMade} to inform {@code GameController} that move has been executed
     * @param toMoveTo {@code Tile} to move {@code Piece} to
     */
    private void makeMove(Tile toMoveTo){
        Thread makeMoveThread = new Thread(() -> {
            try {
                Piece movedPiece = player.makeMove(lastRoll, toMoveTo);
                fireMoveMade(movedPiece);
            } catch (IllegalMoveException e) {
                e.printStackTrace(); //Shouldn't happen if we disable buttons not in valid move set for this turn so cannot fire event!
            }
        });
        makeMoveThread.start();

    }

    /**
     * Starts a new thread to roll dice using {@link Player#rollDice()}.
     * Sets the dice value as {@code lastRoll}
     * May need to fire new event to {@link GameController} to provide roll value to view??
     */
    private void rollDice(){
        Thread rollDiceThread = new Thread(() -> lastRoll = player.rollDice());
        rollDiceThread.start();
    }

    /**
     * Fires {@code MoveMade} event with source as {@code movedPiece} to {@code parentListener} who is {@link GameController}
     * @param movedPiece {@code Piece} object moved
     */
    private void fireMoveMade(Piece movedPiece){
        this.parentListener.actionPerformed(new MoveMade(movedPiece, movedPiece.getTileNumber()));
    }


    public void endTurn() {
    }

    public void startTurn() {
    }
}
