package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller for system. Presides over {@code GameController}, {@code MenuController} for system.
 * Allows for communication between menus and game
 */
public class MainController implements ActionListener {

    /**
     * Menu controller instance to allow system to show menus and relay menu input to game where necessary
     */
    MenuController menuController;

    /**
     * Game controller to allow system to start and control game
     */
    GameController gameController;

    /**
     * Constructor for new {@code MainController}
     * Root of controller tree. Relays between {@code MenuController} and {@code GameController} instances
     */
    public MainController(){
        this.menuController = new MenuController(this);
        this.gameController = new GameController( this);
    }

    /**
     * Starts main controller system
     */
    public void start(){
        showStart();
    }

    /**
     * Shows start menu via {@code MenuController}
     */
    private void showStart(){
        this.menuController.showStartMenu();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
