package controller.action;

import board.Tile;
import player.Piece;

import java.awt.event.ActionEvent;

public class MoveMade extends ActionEvent {

    public MoveMade(Piece movedPiece, int id){
        this(movedPiece, id, "Moved Made");
    }

    public MoveMade(Piece source, int id, String command) {
        super(source, id, command);
    }
}
