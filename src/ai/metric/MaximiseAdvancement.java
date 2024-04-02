package ai.metric;

import game.UrGame;
import player.Player;
import states.BoardState;
import states.GameState;
import states.TileState;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Metric asses how far down player's path player's most advanced piece is.
 */
public class MaximiseAdvancement extends Metric{

    public MaximiseAdvancement(UrGame game) {
        super(
         game.getBoard().getPlayerPath(Player.LIGHT_PLAYER).size(),
        -game.getBoard().getPlayerPath(Player.DARK_PLAYER).size()
        );
    }

    /**
     * Evaluates the {@code gameState} and gives it a score based on the criteria of this metric.
     * Metric asses how far down player's path player's most advanced piece is.
     * Scores reflect how 'good' game state is for both players.
     * Game states with <b>HIGHER</b> scores are better for <b>LIGHT</b> player
     * Game states with <b>LOWER</b> scores are better for <b>DARK</b> player
     *
     * @param gameState State to score
     * @return Score for {@code gameState} according to this metric's criteria
     */
    @Override
    public double scoreForState(GameState gameState) {
        BoardState boardState = gameState.getBoardState();
        List<TileState> lightPath = boardState.getPathForPlayer(Player.LIGHT_PLAYER);
        List<TileState> darkPath = boardState.getPathForPlayer(Player.DARK_PLAYER);
        int lastIndexForLight = IntStream.range(1, lightPath.size()-2).filter(index -> lightPath.get(index).hasPieceForPlayer(Player.LIGHT_PLAYER)).max().orElse(lightPath.size());
        int lastIndexForDark = IntStream.range(1, darkPath.size()-2).filter(index -> darkPath.get(index).hasPieceForPlayer(Player.DARK_PLAYER)).max().orElse(darkPath.size());
        return lastIndexForLight-lastIndexForDark;
    }

}
