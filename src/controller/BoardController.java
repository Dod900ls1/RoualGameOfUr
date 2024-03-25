package controller;

import board.Board;
import board.Tile;
import controller.action.MoveSelected;
import controller.action.TileSelected;
import player.Piece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class BoardController implements ActionListener {
    final Board board;
    Set<TileController> tileControllers;

    final GameController parentListener;

    public BoardController(Board board, GameController parentListener) {
        this.board=board;
        this.parentListener = parentListener;
        this.tileControllers = new HashSet<>();
        for (Tile tile : board.getTiles()) {
            tileControllers.add(new TileController(tile, this)); //this is listener for all tile events
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e instanceof TileSelected){
            TileController tileController = (TileController) e.getSource();
            fireMoveSelected(tileController.getTile());
        }

    }

    private void fireMoveSelected(Tile tileSelected){
        this.parentListener.actionPerformed(new MoveSelected(tileSelected, tileSelected.getTileNum()));
    }

    public void updateBoard(Piece movedPiece) {
        TileController lastTileController = getControllerForTile(movedPiece.getLastTile());
        TileController newTileController = getControllerForTile(movedPiece.getTile());
        lastTileController.updateTile();
        newTileController.updateTile();
    }

    private TileController getControllerForTile(Tile tile){
        return tileControllers.stream().filter(tc -> tc.getTile().equals(tile)).findFirst().orElse(null);
    }
}
