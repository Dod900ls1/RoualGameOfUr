package board;

import game.UrGame;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.PlayerOptions;

import java.util.List;


public class BoardTest {

    UrGame game;
    Board board;

    @Before
    public void setUp() throws Exception {
        UrGame game = new UrGame(new PlayerOptions[]{
                new PlayerOptions(Player.LIGHT_PLAYER, true),
                new PlayerOptions(Player.DARK_PLAYER, true)
        });

        board = game.getBoard();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetPlayerPath() {
        List<Tile> lightPath = board.getPlayerPath(Player.LIGHT_PLAYER);
        int[] lightPathNumbers = lightPath.stream().mapToInt(Tile::getTileNum).toArray();
        int[] expectedLightPathNumbers = board.getLayout().lightPath();
        Assert.assertArrayEquals(expectedLightPathNumbers, lightPathNumbers);
        Assert.assertTrue(lightPath.get(0) instanceof PreStartTile);
        Assert.assertTrue(lightPath.get(lightPath.size()-1) instanceof PostEndTile);

        List<Tile> darkPath = board.getPlayerPath(Player.DARK_PLAYER);
        int[] darkPathNumbers = darkPath.stream().mapToInt(Tile::getTileNum).toArray();
        int[] expectedDarkPathNumbers = board.getLayout().darkPath();
        Assert.assertArrayEquals(expectedDarkPathNumbers, darkPathNumbers);
        Assert.assertTrue(darkPath.get(0) instanceof PreStartTile);
        Assert.assertTrue(darkPath.get(darkPath.size()-1) instanceof PostEndTile);

    }
}