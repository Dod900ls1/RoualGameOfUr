package controller.action;

import board.Tile;

import java.awt.event.ActionEvent;

public class MoveSelected extends ActionEvent {

    public MoveSelected(Tile source, int id){
        this(source, id, "Move Selected");
    }

    public MoveSelected(Tile source, int id, String command) {
        super(source, id, command);
    }
}
