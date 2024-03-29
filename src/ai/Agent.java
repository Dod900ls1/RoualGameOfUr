package ai;

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
        RANDOM
    }

    /**
     * Constructs new {@code Agent} specialisation described by {@code agents} parameter for the given player and game context
     * @param playerAI  Reference to {@code Player} instance for whom to serve as an ai agent
     * @param agents Type of agent to be created
     * @param game Reference to current instance of {@code UrGame} to provide access to information that may be required by {@code Agent} in choosing next move
     * @return Specalisation of {@code Agent} for {@code playerAI} in context of {@code game}
     */
    public static Agent getNewAgent(PlayerAI playerAI, Agents agents, UrGame game) {
        switch (agents){
            case RANDOM -> {
                return new RandomAgent(playerAI, game);
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

    public abstract Tile determineNextMove(int roll);




}
