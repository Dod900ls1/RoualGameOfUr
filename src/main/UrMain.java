package main;

import controller.GameController;
import game.UrGame;
import player.PlayerOptions;

public class UrMain {

    public static void main(String[] args) {

    }

    public void startGameFromSetup(PlayerOptions[] playerOptions){
        UrGame gameModel = new UrGame(playerOptions);
        GameController gameController = new GameController(gameModel);
    }


}
