package controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.PlayerOptions;

import static org.junit.Assert.*;

public class GameControllerTest {


    MainController mainController;
    GameController gameController;
    @Before
    public void setUp() throws Exception {
        mainController = new MainController();
        gameController = new GameController(mainController);
        gameController.createGame(new PlayerOptions[]{
                new PlayerOptions(Player.LIGHT_PLAYER, false),
                new PlayerOptions(Player.DARK_PLAYER, false)
        });

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void beginGame() {
        System.out.println("Turns taken:" +gameController.beginGame());
    }
}