package controller;

import board.Tile;
import controller.action.MoveMade;
import controller.action.MoveSelected;
import controller.action.RollDiceAction;
import exceptions.IllegalMoveException;
import player.Piece;
import player.Player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerController implements ActionListener {

    final Player player;
    int lastRoll;

    final GameController parentListener;

    public PlayerController(Player player, GameController parentListener) {
        this.player = player;
        this.parentListener = parentListener;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e instanceof MoveSelected){
            makeMove((Tile)e.getSource());
        } else if (e instanceof RollDiceAction) {
            rollDice();
        }
    }
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
    private void rollDice(){
        Thread makeMoveThread = new Thread(() -> lastRoll = player.rollDice());
        makeMoveThread.start();
    }

    private void fireMoveMade(Piece movedPiece){
        this.parentListener.actionPerformed(new MoveMade(movedPiece, movedPiece.getTileNumber()));
    }


    public void endTurn() {
    }

    public void startTurn() {
    }
}
