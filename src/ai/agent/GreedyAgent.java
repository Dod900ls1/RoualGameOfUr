package ai.agent;

import ai.metric.Metric;
import board.Tile;
import game.UrGame;
import player.PlayerAI;

/**
 * Greedy agent who prioritises metric at next turn
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

    @Override
    public Tile determineNextMove(int roll) {
        return super.determineNextMove(roll);
    }
}
