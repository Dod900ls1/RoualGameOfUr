package ai.agent;

import board.Tile;
import game.UrGame;
import player.PlayerAI;

import java.util.List;
import java.util.Random;

/**
 * AI agent who determines next tile by random selection
 */
public class RandomAgent extends Agent {
    private Random random;

    /**
     * Constructor for new {@code RandomAgent}
     * @param player Reference to {@code Player} instance for whom to serve as an ai agent
     * @param game Reference to current instance of {@code UrGame} to provide access to information that may be required by {@code Agent} in choosing next move
     */
    public RandomAgent(PlayerAI player, UrGame game) {
        super(player, game);
        random = new Random();
    }

    /**
     * Determines and returns end {@code Tile} for next move via random selection from valid move set for given value of {@code roll}
     * @param roll Value of roll for turn
     * @return {@code Tile} instance a {@code Piece} is to be moved to in current turn
     */
    @Override
    public Tile determineNextMove(int roll) {
        List<Tile> moveTiles = player.findPotentialMoves(roll);
        int moves = moveTiles.size();

        //sleep thread for a bit
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        if (moves==0){ //player does not always have a valid move
            return null;
        }
        else{
            return moveTiles.get(random.nextInt(moveTiles.size()));
        }
    }
}
