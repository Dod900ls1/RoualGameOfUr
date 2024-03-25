package player;

import board.Tile;

public class Piece {
    private Tile tile;
    private Tile lastTile;

    public Piece(){
        tile = null;
    }

    public int getTileNumber() {
        return tile.getTileNum();
    }

    public Tile getTile(){return tile;}

    public void setTile(Tile tile) {
        this.lastTile = this.tile;
        this.tile = tile;
    }

    public Tile getLastTile() {
        return lastTile;
    }

    public void move(Tile toMoveTo) {
        this.lastTile = this.tile;
        this.tile = toMoveTo;
        lastTile.removePiece(this);
        tile.addPiece(this);
    }
}
