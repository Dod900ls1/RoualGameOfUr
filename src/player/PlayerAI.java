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

    public Tile determineNextTile(int roll) {
        //test with random choice
        List<Tile> moveTiles = findPotentialMoves(roll);
        int moves = moveTiles.size();
        if (moves==0){ //player does not always have a valid move
            return null;
        }
        else{
            return moveTiles.get(0);
        }


    }








}
