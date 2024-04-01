package ai.metric;

import player.Player;
import states.BoardState;
import states.GameState;
import states.TileState;

import java.util.List;

public class MaximisePostBoard extends Metric{


    @Override
    public double scoreForState(GameState gameState) {
        BoardState boardState = gameState.getBoardState();
        List<TileState> lightPath = boardState.getPathForPlayer(Player.LIGHT_PLAYER);
        List<TileState> darkPath = boardState.getPathForPlayer(Player.DARK_PLAYER);
        int postBoardForLight = lightPath.get(lightPath.size()-1).getPiecesForPlayer(Player.LIGHT_PLAYER);
        int postBoardForDark = darkPath.get(darkPath.size()-1).getPiecesForPlayer(Player.DARK_PLAYER);
        return postBoardForLight-postBoardForDark;
    }
}
