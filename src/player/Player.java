package player;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import board.Board.PlayerBoard;
import board.Tile;
import exceptions.IllegalMoveException;

public abstract class Player {

    public static final int LIGHT_PLAYER=1;
    public static final int DARK_PLAYER=2;

    public static final int PIECE_START_COUNT = 7;

    private List<Tile> playerPath;

    Collection<Piece> pieces;

    private Tile startPosition;
    private int pieceNum;

    public static Player createPlayerFromSetup(PlayerOptions playerOption, List<Tile> playerPath) {
        if (playerOption.isHuman()){
            return new PlayerHuman(playerPath);
        }
        else{
            return new PlayerAI(playerPath);
        }
    }


    public Player(List<Tile> playerPath) {
        this.playerPath=playerPath;
        this.startPosition=playerPath.get(0);
        this.pieces = new ArrayList<>();
        for (int i = 0; i < PIECE_START_COUNT; i++) {
            pieces.add(new Piece());
        }
    }



    public Tile getStartPosition() {
        return this.startPosition;
    }

    public List<Tile> getPlayerPath() {
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

    /**
     * Simulates dice roll
     * @return Random integer in interval [0, 4)
     */
    public int rollDice() {
        Random diceRoll = new Random();
        int randNum = diceRoll.nextInt(4);
        return randNum;
    }


    private Piece getPieceToMove(int indexFrom) {
        if (indexFrom>=0){
            Tile tileFrom = playerPath.get(indexFrom);
            Piece pieceToMove = pieces.stream().filter(p -> p.getTile().equals(tileFrom)).findFirst().orElse(null);
            return pieceToMove;

        }
        return null;
    }


    public Piece makeMove(int roll, Tile toMoveTo) throws IllegalMoveException {
        int tileToMoveToPathIndex = playerPath.indexOf(toMoveTo);
        int indexFrom = tileToMoveToPathIndex-roll;
        Piece movePiece = getPieceToMove(indexFrom);
        if (movePiece==null){
            throw new IllegalMoveException();
        }
        else{
            movePiece.move(toMoveTo);
        }
        return movePiece;
    }
}