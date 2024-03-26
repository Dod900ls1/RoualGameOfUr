package player;

import board.Board;
import board.Tile;
import exceptions.IllegalMoveException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public abstract class Player {

    public static final int LIGHT_PLAYER=1;
    public static final int DARK_PLAYER=2;

    public static final int PIECE_START_COUNT = 7;

    /**
     * Player's path around board as {@code Tile} sequence
     */
    private List<Tile> playerPath;

    /**
     * Player's pieces
     */
    Collection<Piece> pieces;

    private Tile startPosition;
    private int pieceNum;

    /**
     * Creates a {@code Player} instance from the {@code PlayerOptions} parameters.
     * @param playerOption Configuration of new {@code Player}
     * @param playerPath Path for new {@code Player} retrieved from {@link Board#getPlayerPath(int) Board.getPlayerPath}
     * @return new {@code Player} instance
     */
    public static Player createPlayerFromSetup(PlayerOptions playerOption, List<Tile> playerPath) {
        if (playerOption.isHuman()){
            return new PlayerHuman(playerPath);
        }
        else{
            return new PlayerAI(playerPath);
        }
    }


    /**
     * Super constructor for {@code Player} subclasses
     * @param playerPath Player's path around board as {@code Tile} sequence
     */
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


    /**
     * Simulates dice roll
     * @return Random integer in interval [0, 4)
     */
    public int rollDice() {
        Random diceRoll = new Random();
        int randNum = diceRoll.nextInt(4);
        return randNum;
    }


    /**
     * Determines which of player's @code Piece} instances can perform the selected move legally
     * @param indexFrom Index of {@code Tile} in {@code playerPath} {@code Piece} should be in to start
     * @return {@code Piece} to move; {@code null} if no piece found
     */
    private Piece getPieceToMove(int indexFrom) {
        if (indexFrom>=0){
            Tile tileFrom = playerPath.get(indexFrom);
            Piece pieceToMove = pieces.stream().filter(p -> p.getTile().equals(tileFrom)).findFirst().orElse(null);
            return pieceToMove;

        }
        return null;
    }

    /**
     * Executes move for player.
     * Moves {@code Piece} that achieves a legal move for the given {@code roll} and {@code toMoveTo}
     * @param roll Number of tiles in path to moe through
     * @param toMoveTo {@code Tile} instance to move appropriate {@code Piece} onto
     * @return {@code Piece} instance moved
     * @throws IllegalMoveException If cannot find a {@code Piece} for which move is valid
     */
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