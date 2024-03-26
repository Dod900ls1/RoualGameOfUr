package controller.action.menu;

import ui.Menu.MenuClosedEventSource;

import java.awt.event.ActionEvent;

/**
 * Fired to {@code MenuController} when {@code Menu} instance is closed
 */
public class MenuClosed extends ActionEvent {


   public MenuClosed(MenuClosedEventSource menuSourceWithParams){
       this(menuSourceWithParams, ActionEvent.ACTION_PERFORMED, "Menu closed");
   }

    public MenuClosed(Object source, int id, String command) {
        super(source, id, command);
    }
}
