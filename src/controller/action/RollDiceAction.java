package controller.action;

import java.awt.event.ActionEvent;

public class RollDiceAction extends ActionEvent {

    public RollDiceAction(Object source, int id){
        this(source, id, "Roll Dice");
    }
    public RollDiceAction(Object source, int id, String command) {
        super(source, id, command);
    }

}
