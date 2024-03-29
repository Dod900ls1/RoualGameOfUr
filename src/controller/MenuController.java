package controller;

import controller.action.game.GameStarted;
import controller.action.menu.MenuClosed;
import controller.action.menu.MenuClosed.MenuClosedEventSource;
import ui.StartMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller for menus. Menu views switched out by controller. Menu events listened for by controller.
 */
public class MenuController implements ActionListener {


    /**
     * {@code MenuController} reports to {@link MainController}.
     * If event needs to be responded to from whole system scale, it is reported to {@code MainController}
     */
    final MainController parentListener;

    /**
     * Constructor for new {@code MenuController} instance
     * @param parentListener Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     */
    public MenuController(MainController parentListener) {
        this.parentListener = parentListener;
    }


    /**
     * {@code ActionListener} override.
     * Receive events from menu views
     * May pass event up chain of command to {@link MainController}
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e instanceof MenuClosed){
            respondToMenuClosed((MenuClosedEventSource)e.getSource());
        }else if (e instanceof GameStarted){
            parentListener.actionPerformed(e);
        }

    }

    /**
     * Creates and shows start menu in a new thread
     */
    public void showStartMenu() {
        Thread startMenuThread = new Thread(() -> new StartMenu(this));
        startMenuThread.start();

    }


    private void respondToMenuClosed(MenuClosedEventSource closedMenu){
        System.out.println("Menu closed");

    }


}
