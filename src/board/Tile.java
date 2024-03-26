package board;

import player.Piece;
import player.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents tile on board
 */
public class Tile {
    public static final int LIGHT_INDICATOR=1;
    public static final int DARK_INDICATOR=2;
    public static final int SHARED_INDICATOR= LIGHT_INDICATOR+DARK_INDICATOR;
    private int tileNum;
    private int tileType;

    private Set<Piece> piecesOnTile;

    public Tile(int tileNum, int tileType){
        this.tileNum=tileNum;
        this.tileType = tileType;
        this.piecesOnTile=new HashSet<>();
    }

    public int getTileNum() {
        return this.tileNum;
    }



    public void removePiece(Piece piece) {
        piecesOnTile.remove(piece);
    }

    public void addPiece(Piece piece){
        piecesOnTile.add(piece);
    }

    /**
     * Returns if this tile is occupied by a piece belonging to the specified {@code Player}
     * @param player Player to check if pieces belong to
     * @return {@code true} if tile has piece belonging to {@code player}, otherwise {@code false}
     */
    public boolean hasPieceForPlayer(Player player) {
        return this.piecesOnTile.stream().anyMatch(piece -> piece.getPlayer().equals(player));
    }
}