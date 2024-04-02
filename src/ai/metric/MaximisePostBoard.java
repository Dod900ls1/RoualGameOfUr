package ai.metric;

import game.UrGame;
import player.Player;
import states.GameState;

/**
 * Metric asses how many of player's pieces are post-board
 */
public class MaximisePostBoard extends Metric{

    public MaximisePostBoard(UrGame game) {
        super( Player.PIECE_START_COUNT, -Player.PIECE_START_COUNT);
    }

    /**
     * Evaluates the {@code gameState} and gives it a score based on the criteria of this metric.
     * Metric asses how many of player's pieces are post-board
     * Scores reflect how 'good' game state is for both players.
     * Game states with <b>HIGHER</b> scores are better for <b>LIGHT</b> player
     * Game states with <b>LOWER</b> scores are better for <b>DARK</b> player
     *
     * @param gameState State to score
     * @return Score for {@code gameState} according to this metric's criteria
     */
    @Override
    public double scoreForState(GameState gameState) {
        int postBoardForLight = gameState.getPiecesPostBoardForPlayer(Player.LIGHT_PLAYER);
        int postBoardForDark = gameState.getPiecesPostBoardForPlayer(Player.DARK_PLAYER);
        return postBoardForLight-postBoardForDark;
    }
}
