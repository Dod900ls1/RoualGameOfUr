package ui;

import controller.MenuController;

import javax.swing.*;

/**
 * Might be good to have this as a parent class for all menus. Gives interface for {@link controller.MenuController}
 */
public abstract class Menu extends JFrame {
    /**
     * Passed with {@code MenuClosedEventSource} event as {@code source}
     * @param menu This {@code Menu} subclass instance
     * @param params {@code Record} subclass instance with any menu input that needs to be passed back to {@code parentListener} ({@link MenuController})
     */
    public record MenuClosedEventSource<T extends Record>(Menu menu, T params){}

    MenuController parentListener;

    /**
     * Abstract super constructor for Menu instances
     * @param parentListener Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     */
    public Menu(MenuController parentListener){
        this.parentListener = parentListener;
    }


}
