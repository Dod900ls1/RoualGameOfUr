package controller.action.game;

import java.awt.event.ActionEvent;

public class NoMovePossible extends ActionEvent {
    public NoMovePossible(){
        super(null, ActionEvent.ACTION_PERFORMED, "No move possible");
    }



}
