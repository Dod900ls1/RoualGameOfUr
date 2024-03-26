package board;

import game.UrGame;
import junit.framework.TestCase;
import org.junit.Assert;
import player.Player;
import player.PlayerOptions;

import java.util.List;


public class BoardTest extends TestCase {

    UrGame game;
    Board board;

    public void setUp() throws Exception {
        super.setUp();
        UrGame game = new UrGame(new PlayerOptions[]{
                new PlayerOptions(Player.LIGHT_PLAYER, true),
                new PlayerOptions(Player.DARK_PLAYER, true)
        });

        board = game.getBoard();

    }

    public void tearDown() throws Exception {
    }

    public void testGetPlayerPath() {
        List<Tile> lightPath = board.getPlayerPath(Player.LIGHT_PLAYER);
        int[] lightPathNumbers = lightPath.stream().mapToInt(Tile::getTileNum).toArray();
        int[] expectedLightPathNumbers = board.getLayout().lightPath();
        Assert.assertArrayEquals(expectedLightPathNumbers, lightPathNumbers);
        assertTrue(lightPath.get(0) instanceof PreStartTile);
        assertTrue(lightPath.get(lightPath.size()-1) instanceof PostEndTile);

        List<Tile> darkPath = board.getPlayerPath(Player.DARK_PLAYER);
        int[] darkPathNumbers = darkPath.stream().mapToInt(Tile::getTileNum).toArray();
        int[] expectedDarkPathNumbers = board.getLayout().darkPath();
        Assert.assertArrayEquals(expectedDarkPathNumbers, darkPathNumbers);
        assertTrue(darkPath.get(0) instanceof PreStartTile);
        assertTrue(darkPath.get(darkPath.size()-1) instanceof PostEndTile);

    }
}