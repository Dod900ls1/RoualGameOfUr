package controller.action.game;

import controller.TileController;

import java.awt.event.ActionEvent;

/**
 * Fired when a {@code Tile} is selected
 */
public class TileSelected extends ActionEvent {

    public TileSelected(TileController source, int id){
        this(source, id, "Tile selected");
    }
    public TileSelected(TileController source, int id, String command) {
        super(source, id, command);
    }
}
