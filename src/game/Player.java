package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Player {

    private ArrayList<Tile> playerPath = new ArrayList<>();
    private HashMap<Integer, Tile> pieces = new HashMap<>();
    private Tile startPosition;
    private int pieceNum;

    public Player(ArrayList<Tile> playerPath, Tile startPosition) {
        this.pieceNum = 7;

    }

    public Tile getStartPosition() {
        return this.startPosition;
    }

    public ArrayList<Tile> getPlayerPath() {
        return this.playerPath;
    }

    public int getPieceNum() {
        return this.pieceNum;
    }

    public void removePiece() {
        this.pieceNum -= 1;
    }

    public void addPiece() {
        this.pieceNum += 1;
    }

    public int rollDice() {
        Random diceRoll = new Random();
        int randNum = diceRoll.nextInt(4);
        return randNum;
    }

    
}