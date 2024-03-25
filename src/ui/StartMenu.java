package ui;

import java.awt.FlowLayout;
import javax.swing.JFrame;

/**
 * StartMenu class ought to create start menu for the player where they would be
 * able to connect to a server or to start an offline session with AI or their friend.
 */
public class StartMenu extends JFrame {
    private Renderer renderer = new Renderer();
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    /**
     * Constructor of StartMenu class runs a configFrame() method
     * that configurates our StartMenu frame.
     */
    public StartMenu() {
        configFrame();
    }

    private void configFrame() {
        setSize(WIDTH, HEIGHT);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(renderer.createButton("Create Server", new ServerActionListener(), 150, 50), CENTER_ALIGNMENT);
        add(renderer.createButton("Join Server", new ClientActionListener(), 150, 50), CENTER_ALIGNMENT);

        setVisible(true);
    }

    public static void main(String[] args) {
        new StartMenu();
    }
}
