package controller.action.game;

import player.Player;

import java.awt.event.ActionEvent;

public class NoMovePossible extends ActionEvent {
    public NoMovePossible(Player player){
        super(player, ActionEvent.ACTION_PERFORMED, "No move possible");
    }



}
