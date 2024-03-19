package ui;

import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.event.*;

public class Renderer {

    public JButton createButton(String buttonName, ActionListener listener, int width, int height){
        JButton button = new JButton(buttonName);
        button.addActionListener(listener);
        button.setPreferredSize(new Dimension(width, height));
        return button;
    }   
}
