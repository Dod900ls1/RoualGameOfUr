package controller;

import controller.action.game.GameStarted;
import controller.action.game.GameStarted.GameStartedEventSource;
import controller.action.game.GameStartedWithServer;

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



    /**
     * {@code ActionListener} override. Responds to events fired from {@link #gameController} and {@link #menuController} instances that need to be relayed between them
     * Root of action event tree
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e instanceof GameStarted){
            createGame((GameStartedEventSource)e.getSource());
        }else if (e instanceof GameStartedWithServer){
            createGameAsServer((GameStartedWithServer.GameStartedWithServerEventSource)e.getSource());
        }

    }

    /**
     * Calls {@link GameController#createGameAsServer(GameStartedWithServer.GameStartedWithServerEventSource) GameController.createGameAsServer}
     * @param gameStartedWithServerEventSource
     */
    private void createGameAsServer(GameStartedWithServer.GameStartedWithServerEventSource gameStartedWithServerEventSource) {
        gameController.createGameAsServer(gameStartedWithServerEventSource);
    }

    /**
     * Creates new game via {@code GameController}
     * @param gameStartedEventSource {@code GameStarted.source} field value. Record containing game setup information fields
     */
    private void createGame(GameStartedEventSource gameStartedEventSource) {
        gameController.createGame(gameStartedEventSource.playerOptions());
    }

    public void createGameAsClient(Object gameInitData) {
        gameController.createGameAsClient(gameInitData);
    }
}
