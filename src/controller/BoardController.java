package controller;

import board.Board;
import board.Tile;
import controller.action.game.MoveSelected;
import controller.action.game.TileSelected;
import player.Piece;
import player.Player;
import ui.BoardInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for board. Links board model to board view
 */
public class BoardController implements ActionListener {
    /**
     * {@code Board} model for this controller
     */
    final Board board;
    /**
     * {@link TileController Controllers} for {@code Tile} objects in {@link Board#getTiles() board tile set}
     */
    Set<TileController> tileControllers;

    /**
     * {@code BoardController} reports to {@link GameController}.
     * If event needs to be responded to from whole game scale, it is reported to {@code GameController}
     */
    final GameController parentListener;


    final BoardInterface boardInterface;

    /**
     * Constructor for new {@code BoardController}
     * Has access to {@link Board} instance and view of Board - bridges communication between objects
     * @param board {@code Board} model entity
     * @param parentListener Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     */
    public BoardController(Board board, GameController parentListener) {
        this.board=board;
        this.parentListener = parentListener;
        this.tileControllers = new HashSet<>();
        for (Tile tile : board.getTiles()) {
            tileControllers.add(new TileController(tile, this)); //this is listener for all tile events
        }


        this.boardInterface = new BoardInterface(this);
    }

    /**
     * {@code ActionListener} override. Responds to events fired from {@code tileController} controllers.
     * May pass event up chain of command to {@link GameController} e.g. if player needs to be notified of event
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e instanceof TileSelected){
            TileController tileController = (TileController) e.getSource();
            fireMoveSelected(tileController.getTile());
        }

    }

    /**
     * Fires event to {@code parentListener} to indicate tile has been selected
     * @param tileSelected Tile in board selected
     */
    private void fireMoveSelected(Tile tileSelected){
        this.parentListener.actionPerformed(new MoveSelected(tileSelected, tileSelected.getTileNum()));
    }

    /**
     * Updates the board tiles via {@link TileController#updateTile()} for the moved piece (update tile piece moved from and to)
     * Updates all {@code PreStartTile} and {@code PostEndTile} on board via {@link TileController#updateTile()}
     *
     * @param movedPiece Piece moved last turn
     * @return
     */
    public GameController.PieceMoveForStash updateBoard(Piece movedPiece) {
        TileController lastTileController = getControllerForTile(movedPiece.getLastTile());
        TileController newTileController = getControllerForTile(movedPiece.getTile());
        lastTileController.updateTile();
        newTileController.updateTile();
        List<Tile> prePostBoardTiles = board.getPrePostBoardTiles();
        for (Tile prePostBoardTile : prePostBoardTiles) {
            getControllerForTile(prePostBoardTile).updateTile();
        }

        GameController.PieceMoveForStash pieceMovedStash = new GameController.PieceMoveForStash(movedPiece.getPlayer().getPlayerColour(), lastTileController.getTileNumber(), newTileController.getTileNumber()); //adds moved piece to stash
            //not adding moves of captured pieces back to preboard as can be worked out when board is updated from remote stash



       return pieceMovedStash;
    }

    /**
     * Return {@link TileController} associated with provided {@code} Tile
     * @param tile Tile to get {@code TileController} for
     * @return {@code TileController} for {@code tile}
     */
    private TileController getControllerForTile(Tile tile){
        return tileControllers.stream().filter(tc -> tc.getTile().equals(tile)).findFirst().orElse(null);
    }

    private Collection<TileController> getControllersForTiles(Collection<Tile> tiles){
        return tileControllers.stream().filter(tc -> tiles.contains(tc.getTile())).collect(Collectors.toList());
    }


    public Collection<TileController> getTileControllers(){
        return this.tileControllers;
    }


    /**
     * Returns the number of rows in the board
     * @return {@link Board#getRows()}
     */
    public int getRows() {
        return board.getRows();
    }

    /**
     * Returns the number of columns in the board
     * @return {@link Board#getColumns()}
     */
    public int getColumns(){
        return board.getColumns();
    }

    /**
     * Accessor for {@code BoardInterface} field
     * @return {@link #boardInterface} instance
     */
    public BoardInterface getBoardInterface() {
        return this.boardInterface;
    }

    public void enableValidMoveTiles(List<Tile> validTilesForMove) {
        boardInterface.enableBoard();
        Collection<TileController> validTileControllers = getControllersForTiles(validTilesForMove);
        validTileControllers.stream().forEach(TileController :: enableTile);
    }

    public List<PlayerPieceOnTile> getPiecesForPlayersOnBoard() {
        List<PlayerPieceOnTile> playerPiecesOnTile = new ArrayList<>();
        for (TileController tileController : tileControllers) {
            Map<Player, Integer> piecesOnTile = tileController.getPiecesByPlayer(); //player, num of pieces
            for (Map.Entry<Player, Integer> playerPieceEntry : piecesOnTile.entrySet()) {
                PlayerPieceOnTile playerPieceOnTile = new PlayerPieceOnTile(playerPieceEntry.getKey().getPlayerColour(), tileController.getTileNumber());
                for (int i = 0; i < playerPieceEntry.getValue(); i++) {
                  playerPiecesOnTile.add(playerPieceOnTile);
                }

            }
        }

        return playerPiecesOnTile;

    }

    public Tile getTileFromNumber(int tileNumber) {
       return  board.getTileFromNumber(tileNumber);
    }

    /**
     * Determines if {@code Tile} in {@code tilesWithPieceForPlayer} has a different piece count for {@code player}
     * @return Collection of {@code Tile} for whom piece count for player has changed
     */
    public Collection<Tile> findAlteredPlayerPieceCountTiles(Player player, List<Integer> tilesWithPieceForPlayer) {
        List<Tile> alteredTiles = new ArrayList<>();
        Map<Integer, Long> tileNumberWithPieceCountForPlayer = tilesWithPieceForPlayer.stream().collect(Collectors.groupingBy(tileNumber -> tileNumber, Collectors.counting()));
        for (Map.Entry<Integer, Long> tilePieceCountEntry : tileNumberWithPieceCountForPlayer.entrySet()) {
            Tile tileToCheck = getTileFromNumber(tilePieceCountEntry.getKey());
            if (tileToCheck.getPieceCountForPlayer(player) == tilePieceCountEntry.getValue()) {
                return null;
            } else {
                alteredTiles.add(tileToCheck);
            }
        }
        return alteredTiles;
    }

    public record PlayerPieceOnTile(Integer playerNumber, Integer tileNumberWithPlayerPiece){}

}
