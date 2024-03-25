package ui;

import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.event.*;

/**
 * The Renderer class provides methods for creating graphical user interface components.
 */
public class Renderer {

    /**
     * Creates and configures a JButton with the specified name, ActionListener, width, and height.
     * 
     * @param buttonName the text to be displayed on the button
     * @param listener the ActionListener to be notified when the button is clicked
     * @param width the width of the button
     * @param height the height of the button
     * @return the configured JButton
     */
    public JButton createButton(String buttonName, ActionListener listener, int width, int height){
        JButton button = new JButton(buttonName);
        button.addActionListener(listener);
        button.setPreferredSize(new Dimension(width, height));
        return button;
    }   
}
