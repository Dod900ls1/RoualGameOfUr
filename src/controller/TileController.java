package controller;

import board.Tile;
import controller.action.TileSelected;
import player.Piece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TileController implements ActionListener {

    private final BoardController parentListener;

    private final Tile tile;

    public TileController(Tile tile, BoardController parentListener){
        this.parentListener = parentListener;
        this.tile = tile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireTileSelected();
    }
    private void fireTileSelected(){
        parentListener.actionPerformed(new TileSelected(this, tile.getTileNum()));
    }

    public Tile getTile() {
        return tile;
    }

    public void updateTile(){
        //Todo Update tile view to reflect tile.piecesOnTile set
    }

}
