package ai;

import board.Tile;
import game.UrGame;
import player.PlayerAI;
import states.GameState;

public class ExpectiminimaxAgent extends Agent{

    /**
     *  Constructor for new {@code ExpectiminimaxAgent}
     * @param player Reference to {@code Player} instance for whom to serve as an ai agent
     * @param game   Reference to current instance of {@code UrGame} to provide access to information that may be required by {@code Agent} in choosing next move
     */
    public ExpectiminimaxAgent(PlayerAI player, UrGame game) {
        super(player, game);
    }

    /**
     * Determines and returns end {@code Tile} for next move via expectiminimax algorithm for given value of {@code roll}
     * @param roll Value of roll for turn
     * @return {@code Tile} instance a {@code Piece} is to be moved to in current turn
     */
    @Override
    public Tile determineNextMove(int roll) {
        GameState currentGameState = game.bundle();






        return null;
        //TODO
    }







}
