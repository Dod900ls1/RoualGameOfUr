package player;

import ai.Agent;
import board.Tile;
import game.UrGame;

import java.util.List;

public class PlayerAI extends Player{

    Agent agent;

    public PlayerAI(int colour, List<Tile> playerPath, UrGame game) {
        super(colour, playerPath);
        agent = Agent.getNewAgent(this, Agent.Agents.RANDOM, game);
    }

    /**
     * Determines and returns end {@code Tile} for next move via {@link #agent} for given value of {@code roll}
     * @param roll Value of roll for turn
     * @return {@code Tile} instance a {@code Piece} is to be moved to in current turn
     */
    public Tile determineNextTile(int roll) {
        return agent.determineNextMove(roll);
    }








}
