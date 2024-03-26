package board;

/**
 * Models tile occupied by off-boarded pieces i.e. {@code Piece} instances that have completed path and are now off the board
 */
public class PostEndTile extends Tile{

    public PostEndTile(int tileNum, int tileType) {
        super(tileNum, tileType);
    }
}
