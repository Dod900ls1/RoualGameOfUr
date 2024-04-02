package ai.agent;

import ai.metric.Metric;
import board.Tile;
import game.UrGame;
import player.PlayerAI;

/**
 * Greedy agent who prioritises metric at next turn.
 * Greedy agent is a specialisation of {@link ExpectiminimaxAgent} with {@code DEPTH} of 0
 */
public class GreedyAgent extends ExpectiminimaxAgent{

    /**
     * Constructor for new {@code GreedyAgent}.
     * @param player Reference to {@code Player} instance for whom to serve as an ai agent
     * @param game   Reference to current instance of {@code UrGame} to provide access to information that may be required by {@code Agent} in choosing next move
     */
    public GreedyAgent(PlayerAI player, UrGame game, Metric metric) {
        super(player, game, metric, 0); //expectiminimax with depth 0 is greedy
    }

    /**
     * Determines and returns end {@code Tile} for next move via greedy algorithm for given value of {@code roll}
     * @param roll Value of roll for turn
     * @return {@code Tile} instance a {@code Piece} is to be moved to in current turn
     */
    @Override
    public Tile determineNextMove(int roll) {
        return super.determineNextMove(roll);
    }
}
