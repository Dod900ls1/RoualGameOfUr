package controller.action.game;

import player.Piece;

import java.awt.event.ActionEvent;

/**
 * Fired when player has made their move with moved {@code Piece} as source
 */
public class MoveMade extends ActionEvent {

    public MoveMade(Piece movedPiece, int id){
        this(movedPiece, id, "Moved Made");
    }

    public MoveMade(Piece source, int id, String command) {
        super(source, id, command);
    }
}
