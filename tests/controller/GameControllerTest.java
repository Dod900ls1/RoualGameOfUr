package controller;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.PlayerOptions;
import states.TileState;

public class GameControllerTest {


    MainController mainController;
    GameController gameController;
    @Before
    public void setUp() throws Exception {
        mainController = new MainController();
        gameController = new GameController(mainController);
        gameController.createGame(new PlayerOptions[]{
                new PlayerOptions(Player.LIGHT_PLAYER, false, null, null),
                new PlayerOptions(Player.DARK_PLAYER, false, null,null)
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