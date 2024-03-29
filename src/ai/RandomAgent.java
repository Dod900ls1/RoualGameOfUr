package ai;

import game.UrGame;
import player.PlayerAI;

/**
 * AI agent who determines next tile by random selection
 */
public class RandomAgent extends Agent{

    /**
     * Constructor for new {@code RandomAgent}
     * @param player Reference to {@code Player} instance for whom to serve as an ai agent
     * @param game Reference to current instance of {@code UrGame} to provide access to information that may be required by {@code Agent} in choosing next move
     */
    public RandomAgent(PlayerAI player, UrGame game) {
        super(player, game);
    }
}
