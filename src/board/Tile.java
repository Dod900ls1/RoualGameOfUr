package board;

import exceptions.IllegalMoveException;
import player.Piece;
import player.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents tile on board
 */
public class Tile {
    public static final int LIGHT_INDICATOR=1;
    public static final int DARK_INDICATOR=2;
    public static final int SHARED_INDICATOR= LIGHT_INDICATOR+DARK_INDICATOR;
    /**
     * {@code Board} instance containing this tile
     */
    private final Board board;
    private int tileNum;
    private int tileType;

    /**
     * Indicates if this tile can contain multiple pieces at once
     */
    protected boolean canContainMultiplePieces;

    private Set<Piece> piecesOnTile;

    public Tile(Board board, int tileNum, int tileType){
        this.board=board;
        this.tileNum=tileNum;
        this.tileType = tileType;
        this.piecesOnTile=new HashSet<>();
        canContainMultiplePieces = false;
    }

    public int getTileNum() {
        return this.tileNum;
    }


    /**
     * Removes {@code piece} from this tile's {@code piecesOnTile} set
     * @param piece Piece to be removed
     */
    public void removePiece(Piece piece) {
        piecesOnTile.remove(piece);
    }

    /**
     * Attempts to add {@code piece} to this tile if legal.
     * Legality determined by {@link #canAddPieceForPlayer(Player) canAddPieceForPlayer} conditions
     * Calls {@link Board#sendPiecesToPreBoard(Set) Board.sendPiecesToPreBoard} to remove pre-board pieces not belonging to player owner of {@code piece}
     * @param piece Piece to add to tile
     * @throws IllegalMoveException If {@code piece} cannot legally be added to tile
     */
    public void addPiece(Piece piece) throws IllegalMoveException {
        if (canAddPieceForPlayer(piece.getPlayer())){
            addPiece(piece);
        }else{
            throw new IllegalMoveException();
        }

        Set<Piece> toStay = getAllPiecesForPlayer(piece.getPlayer());
        Set<Piece> toRemove = piecesOnTile.stream().filter(p->!toStay.contains(p)).collect(Collectors.toSet());
        board.sendPiecesToPreBoard(toRemove);//pieces will remove themselves from this tile when Piece.move called here

    }

    /**
     * Returns all {@code Piece} objects occupying this tle belonging to the specified {@code Player}
     * @param player Player to retrieve {@code Piece} instances for
     * @return Subset of {@link #piecesOnTile} where {@link Piece#getPlayer()} is {@code player}
     */
    public Set<Piece> getAllPiecesForPlayer(Player player){
        return piecesOnTile.stream().filter(p -> p.getPlayer().equals(player)).collect(Collectors.toSet());
    }

    /**
     * Returns if the specified {@code Player} can legally add another {@code Piece} to this tile.
     * Legal if tile can contain multiple pieces for same player or if {@code player} does not have a piece on this tile
     * @param player Player to check for
     * @return {@code true} if legal to add piece, otherwise {@code false}
     */
    public boolean canAddPieceForPlayer(Player player){
        return canContainMultiplePieces||(!hasPieceForPlayer(player));
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