package board;

import game.UrGame;
import junit.framework.TestCase;
import org.junit.Assert;
import player.Player;
import player.PlayerOptions;



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
        int[] lightPathNumbers = board.getPlayerPath(Player.LIGHT_PLAYER).stream().mapToInt(Tile::getTileNum).toArray();
        int[] expectedLightPathNumbers = board.getLayout().lightPath();
        Assert.assertArrayEquals(expectedLightPathNumbers, lightPathNumbers);

        int[] darkPathNumbers = board.getPlayerPath(Player.DARK_PLAYER).stream().mapToInt(Tile::getTileNum).toArray();
        int[] expectedDarkPathNumbers = board.getLayout().darkPath();
        Assert.assertArrayEquals(expectedDarkPathNumbers, darkPathNumbers);
    }
}