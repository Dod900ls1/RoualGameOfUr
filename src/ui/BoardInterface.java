package ui;

import controller.BoardController;
import controller.TileController;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Board interface - presents view of board
 */
public class BoardInterface extends JPanel {

    /**
     * {@code BoardController} for this interface linking view to {@link board.Board Board} model
     */
    final BoardController controller;


    /**
     * Constructor for new board interface
     * @param controller {@code BoardController} for this interface linking view to {@link board.Board Board} model
     */
    public BoardInterface(BoardController controller) {
        this.controller = controller;
        createTileInterfaces(controller.getTileControllers());
        setLayout(new GridLayout(controller.getRows(),controller.getColumns()));
    }

    /**
     * Creates {@code TileInterface} objects with {@code TileController} attached as action listener
     * @param tileControllers Controllers to make interfaces for
     */
    private void createTileInterfaces(Collection<TileController> tileControllers){
        List<TileController> sortedControllers = tileControllers.stream().sorted(new Comparator<TileController>() {
            @Override
            public int compare(TileController t1, TileController t2) {
                return Integer.compare(t1.getTileNumber(), t2.getTileNumber());
            }
        }).collect(Collectors.toList());
        for (TileController tileController : sortedControllers) {
            this.add(tileController.getTileInterface());
        }
    }



    public void disableBoard() {
        setEnabled(false);
    }

    public void enableBoard(){
        setEnabled(true);
    }

    public void resetForNewTurn() {
        disableBoard();
        for (Component component : getComponents()) {
            component.setEnabled(false);
        }
    }
}
