package game;

import java.util.ArrayList;
import java.util.HashMap;

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

    
}