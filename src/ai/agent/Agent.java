package ai.agent;

import ai.metric.Metric;
import board.Tile;
import game.UrGame;
import player.Player;
import player.PlayerAI;

/**
 * Abstract AI agent
 */
public abstract class Agent {

    /**
     * Reference to {@code Player} instance for whom to serve as an ai agent
     */
    final Player player;
    /**
     * Reference to current instance of {@code UrGame} to provide access to information that may be required by {@code Agent} in choosing next move
     */
    final UrGame game;

    /**
     * Implemented agent types based on choosing behaviour
     */
    public enum Agents{
        RANDOM,
        EXPECTIMINIMAX_EASY,
        EXPECTIMINIMAX_HARD,
        GREEDY
    }

    /**
     * Constructs new {@code Agent} specialisation described by {@code agents} parameter for the given player and game context
     * @param playerAI  Reference to {@code Player} instance for whom to serve as an ai agent
     * @param agent Type of agent to be created
     * @param game Reference to current instance of {@code UrGame} to provide access to information that may be required by {@code Agent} in choosing next move
     * @return Specalisation of {@code Agent} for {@code playerAI} in context of {@code game}
     */
    public static Agent getNewAgent(PlayerAI playerAI, Agents agent, UrGame game, Metric metric) {
        switch (agent){
            case RANDOM -> {
                return new RandomAgent(playerAI, game);
            }
            case EXPECTIMINIMAX_EASY -> {return ExpectiminimaxAgent.createAgent(playerAI, game, metric, ExpectiminimaxAgent.Level.EASY); }
            case EXPECTIMINIMAX_HARD -> {
                return ExpectiminimaxAgent.createAgent(playerAI, game , metric, ExpectiminimaxAgent.Level.HARD);
            }
            case GREEDY -> {
                return new GreedyAgent(playerAI, game, metric);
            }
        }
        return null;
    }

    /**
     * Super constructor for new {@code Agent}. Called by specialisations
     * @param player Reference to {@code Player} instance for whom to serve as an ai agent
     * @param game Reference to current instance of {@code UrGame} to provide access to information that may be required by {@code Agent} in choosing next move
     */
    public Agent(PlayerAI player, UrGame game) {
        this.player = player;
        this.game = game;
    }

    /**
     * Determines and returns end {@code Tile} for next move via agent's selection method for given value of {@code roll}
     * @param roll Value of roll for turn
     * @return {@code Tile} instance a {@code Piece} is to be moved to in current turn
     */
    public abstract Tile determineNextMove(int roll);




}
