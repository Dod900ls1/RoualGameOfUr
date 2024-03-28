package player;

import board.Tile;

import java.util.List;

public class PlayerAI extends Player{

    public PlayerAI(int colour, List<Tile> playerPath) {
        super(colour, playerPath);
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
