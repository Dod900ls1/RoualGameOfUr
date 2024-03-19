package ui;

import java.awt.FlowLayout;
import javax.swing.JFrame;

public class StartMenu extends JFrame {
    private Renderer renderer = new Renderer();
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

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
        StartMenu startMenu = new StartMenu();
    }
}
