package controller;

import board.Board;
import board.Tile;
import controller.action.game.MoveSelected;
import controller.action.game.TileSelected;
import player.Piece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * @param movedPiece Piece moved last turn
     */
    public void updateBoard(Piece movedPiece) {
        TileController lastTileController = getControllerForTile(movedPiece.getLastTile());
        TileController newTileController = getControllerForTile(movedPiece.getTile());
        lastTileController.updateTile();
        newTileController.updateTile();
        List<Tile> prePostBoardTiles = board.getPrePostBoardTiles();
        for (Tile prePostBoardTile : prePostBoardTiles) {
            getControllerForTile(prePostBoardTile).updateTile();
        }
    }

    /**
     * Return {@link TileController} associated with provided {@code} Tile
     * @param tile Tile to get {@code TileController} for
     * @return {@code TileController} for {@code tile}
     */
    private TileController getControllerForTile(Tile tile){
        return tileControllers.stream().filter(tc -> tc.getTile().equals(tile)).findFirst().orElse(null);
    }
}
