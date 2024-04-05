package board;

import exceptions.IllegalMoveException;
import player.Piece;
import player.Player;

/**
 * Extension of {@code Tile} to model tile with rosette.
 * Features of Rosette:
 * - Pieces safe from capture on rosette
 */
public class Rosette extends Tile{

    public Rosette(Board board, int tileNum, int tileType) {
        super(board, tileNum, tileType);
        isRosette=true;
    }

    /**
     * Attempts to add {@code piece} to this tile if legal.
     * Legality determined by {@link #canAddPieceForPlayer(Player) canAddPieceForPlayer} conditions
     * Pieces safe from capture on rosette so does not send opponents pieces back to start if other player lands on the rosette
     * @param piece Piece to add to tile
     * @throws IllegalMoveException If {@code piece} cannot legally be added to tile
     */
    @Override
    public void addPiece(Piece piece) throws IllegalMoveException {
        if (canAddPieceForPlayer(piece.getPlayer())){
            piecesOnTile.add(piece);
            System.out.println("Added piece to rosette for "+piece.getPlayer().getPlayerColour());

        }else{
            throw new IllegalMoveException();
        }
    }
}
