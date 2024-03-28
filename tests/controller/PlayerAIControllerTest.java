package controller;

import junit.framework.TestCase;
import player.Player;
import player.PlayerOptions;

public class PlayerAIControllerTest extends TestCase {

    MainController mainController;
    GameController gameController;

    public void setUp() throws Exception {
        super.setUp();
         mainController = new MainController();
         gameController = new GameController(mainController);
        gameController.createGame(new PlayerOptions[]{
                new PlayerOptions(Player.LIGHT_PLAYER, false),
                new PlayerOptions(Player.DARK_PLAYER, false)
        });

    }
    public void testStartTurn() {
        gameController.beginGame();

    }
}