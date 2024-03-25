package main;

import controller.GameController;
import game.UrGame;
import player.PlayerOptions;

public class UrMain {

    public static void main(String[] args) {

        startGameFromSetup(new PlayerOptions[]{
                new PlayerOptions(1, true)
        });
    }

    public static void startGameFromSetup(PlayerOptions[] playerOptions){
        UrGame gameModel = new UrGame(playerOptions);
        GameController gameController = new GameController(gameModel);
    }


}
