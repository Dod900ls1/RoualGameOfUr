package controller.action.game;

import board.Tile;

import java.awt.event.ActionEvent;

/**
 * Fired when {@code Tile} selected as move location. Abstraction over {@link TileSelected} to set source as {@code Tile}
 */
public class MoveSelected extends ActionEvent {

    public MoveSelected(Tile source, int id){
        this(source, id, "Move Selected");
    }

    public MoveSelected(Tile source, int id, String command) {
        super(source, id, command);
    }
}
