package controller.action;

import controller.TileController;

import java.awt.event.ActionEvent;

public class TileSelected extends ActionEvent {

    public TileSelected(TileController source, int id){
        this(source, id, "Tile selected");
    }
    public TileSelected(TileController source, int id, String command) {
        super(source, id, command);
    }
}
