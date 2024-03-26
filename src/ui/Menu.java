package ui;

import controller.BoardController;
import controller.MenuController;

import javax.swing.*;

/**
 * Might be good to have this as a parent class for all menus. Gives interface for {@link controller.MenuController}
 */
public abstract class Menu extends JFrame {


    /**
     * Menu action listeners report to {@link MenuController}.
     * If event needs to be responded to from greater scale, it is reported to {@code MenuController}
     */
    MenuController parentListener;

    /**
     * Abstract super constructor for Menu instances
     * @param parentListener Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     */
    public Menu(MenuController parentListener){
        this.parentListener = parentListener;
    }


}
