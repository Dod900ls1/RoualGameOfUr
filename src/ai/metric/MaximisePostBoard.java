package ai.metric;

import states.GameState;

public class MaximisePostBoard extends Metric{


    @Override
    public double scoreForState(GameState gameState) {
        return gameState.getPiecesPostBoardForPlayer(gameState.getActivePlayer());
    }
}
