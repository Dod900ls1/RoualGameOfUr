package board;

/**
 * Models tile occupied by pre-boarded pieces i.e. {@code Piece} instances that are not yet on the board
 */
public class PreStartTile extends Tile{

    public PreStartTile(Board board, int tileNum, int tileType) {
        super(board, tileNum, tileType);
        canContainMultiplePieces=true;
        isNonWalkable=true;
    }
}
