package controller.action.game;

import java.awt.event.ActionEvent;

/**
 * Fired when a dice is to be rolled in {@code PlayerController}
 */
public class RollDice extends ActionEvent {

    public RollDice(Object source, int id){
        this(source, id, "Roll Dice");
    }
    public RollDice(Object source, int id, String command) {
        super(source, id, command);
    }

}
