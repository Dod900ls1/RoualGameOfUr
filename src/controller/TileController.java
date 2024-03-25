package controller;

import board.Board;
import board.Tile;
import controller.action.TileSelected;
import player.Piece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller for {@code Tile} objects. Links {@code Tile} model entity to view
 */
public class TileController implements ActionListener {

    /**
     * {@code TileController} reports to {@link BoardController}.
     * If event needs to be responded to from whole board or greater scale, it is reported to {@code BoardController}
     */
    private final BoardController parentListener;

    /**
     * {@code Tile} model for this controller
     */
    private final Tile tile;

    /**
     * Constructor for new {@code TileController}
     * Has access to {@link Tile} instance and view of Tile - bridges communication between objects
     * @param tile {@code Tile} model entity
     * @param parentListener Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     */
    public TileController(Tile tile, BoardController parentListener){
        this.parentListener = parentListener;
        this.tile = tile;
    }

    /**
     * {@code ActionListener} override. Responds to events fired from tile's view.
     * May pass event up chain of command to {@link BoardController}
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        fireTileSelected();
    }

    /**
     * Fires event to {@code parentListener} to indicate this tile has been selected
     */
    private void fireTileSelected(){
        parentListener.actionPerformed(new TileSelected(this, tile.getTileNum()));
    }

    /**
     * Returns {@code Tile} model entity for this controller
     * @return
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Update tile view
     */
    public void updateTile(){
        //Todo Update tile view to reflect tile.piecesOnTile set
    }

}
