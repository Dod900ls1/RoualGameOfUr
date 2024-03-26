package player;

import board.Tile;

/**
 * Models piece on board belonging to a particular {@code Player}
 */
public class Piece {
    /**
     * {@code Tile} occupied by this piece
     */
    private Tile tile;
    /**
     * Last {@code Tile} occupied by this piece
     */
    private Tile lastTile;

    /**
     * Constructor for new {@code Piece} instance.
     * Initialises {@code tile} field as {@link board.PreStartTile}
     * @param preStartTile Player's {@code PreStartTile} for this {@code Piece} to occupy before it is boarded
     */
    public Piece(Tile preStartTile){
        tile = preStartTile;
        lastTile = null;
    }

    /**
     * Returns the {@link Tile#getTileNum() Tile.tileNum} for {@code Tile} occupied by this piece
     * @return Value of {@code tile.getTileNum}
     */
    public int getTileNumber() {
        return tile.getTileNum();
    }

    /**
     * Return the {@code Tile} occupied by this piece
     * @return {@code Tile} instance in {@link #tile}
     */
    public Tile getTile(){return tile;}

    /**
     * Sets {@code lastTile} to {@code this.tile}. Sets this piece's current to {@code tile}.
     * @param tile
     */
    public void setTile(Tile tile) {
        this.lastTile = this.tile;
        this.tile = tile;
    }

    /**
     * Return the last {@code Tile} occupied by this piece
     * @return {@code Tile} instance in {@link #lastTile}
     */
    public Tile getLastTile() {
        return lastTile;
    }

    /**
     * Moves this piece from its current tile to {@code toMoveTo}
     * Removes self from {@code lastTile} and adds self to {@code tile=toMoveTo}
     * @param toMoveTo {@code Tile} instance to move piece to
     */
    public void move(Tile toMoveTo) {
        setTile(toMoveTo);
        lastTile.removePiece(this);
        tile.addPiece(this);
    }
}
