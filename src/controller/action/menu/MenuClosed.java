package controller.action.menu;

import controller.MenuController;
import ui.Menu;

import java.awt.event.ActionEvent;

/**
 * Fired to {@code MenuController} when {@code Menu} instance is closed
 */
public class MenuClosed extends ActionEvent {

    /**
     * Passed with {@code MenuClosed} event as {@code source}
     * @param menu Closed {@code Menu} subclass instance
     * @param params {@code Record} subclass instance with any menu input that needs to be passed back to {@code parentListener} ({@link MenuController})
     */
    public record MenuClosedEventSource<T extends Record>(Menu menu, T params){}

   public MenuClosed(MenuClosedEventSource menuSourceWithParams){
       this(menuSourceWithParams, ActionEvent.ACTION_PERFORMED, "Menu closed");
   }

    public MenuClosed(Object source, int id, String command) {
        super(source, id, command);
    }
}
