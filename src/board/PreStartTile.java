package board;

/**
 * Models tile occupied by pre-boarded pieces i.e. {@code Piece} instances that are not yet on the board
 */
public class PreStartTile extends Tile{

    public PreStartTile(int tileNum, int tileType) {
        super(tileNum, tileType);
    }
}
