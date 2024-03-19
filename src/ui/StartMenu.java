package ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class StartMenu extends JFrame{
    private Renderer renderer = new Renderer();
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    public StartMenu(){
        configFrame();
    }

    private void configFrame(){
        setSize(WIDTH, HEIGHT);
        setLayout(new FlowLayout());
        add(renderer.createButton("Aboba", new ServerActionListener(), 100, 50), CENTER_ALIGNMENT);
        add(renderer.createButton("Aboba", new ClientActionListener(), 100, 50), CENTER_ALIGNMENT);
        setVisible(true);
    }

    class ServerActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("aboba");
        }

    }

    class ClientActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("aboba2");
        }

    }
    
    public static void main(String[] args) {
        StartMenu startMenu = new StartMenu();
    }
}
