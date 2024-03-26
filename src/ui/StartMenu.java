package ui;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * StartMenu class ought to create start menu for the player where they would be
 * able to connect to a server or to start an offline session with AI or their friend.
 */
public class StartMenu extends JFrame{
    private Renderer renderer = new Renderer();
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private boolean menuOpen = true;
    private boolean onlineMenuOpen = true;
    private JPanel menu = new JPanel();

    /**
     * Constructor of StartMenu class runs a configFrame() method
     * that configurates our StartMenu frame.
     */
    public StartMenu() {
        updateLookAndFeel("Nimbus");
        configFrame();
        configMenu();
    }

    private void configFrame() {
        setSize(WIDTH, HEIGHT);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void configMenu() {

        JButton createServer = renderer.createButton("Create Server", new ServerActionListener(), 150, 50);
        menu.add(createServer);
        JButton joinServer = renderer.createButton("Join Server", new ClientActionListener(), 150, 50);
        menu.add(joinServer);

        ActionListener playOnlineListener = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(onlineMenuOpen){
                    createServer.setVisible(false);
                    joinServer.setVisible(false);
                    onlineMenuOpen = false;
                }else{
                    createServer.setVisible(true);
                    joinServer.setVisible(true);
                    onlineMenuOpen = true;
                }
            }
        };

        JButton playOnline = renderer.createButton("Play online", playOnlineListener, 150, 50);
        menu.add(playOnline);

        ActionListener playListener = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(menuOpen){
                    playOnline.setVisible(false);
                    menuOpen = false;
                }else{
                    playOnline.setVisible(true);
                    menuOpen = true;
                }
            }
        };

        JButton play = renderer.createButton("Play", playListener, 150,50);
        menu.add(play);

        ActionListener backListener = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                menu.setVisible(false);
                play.setVisible(true);
            }
        };

        JButton back = renderer.createButton("Back", backListener, 150, 50);

        add(menu);
        add(playOnline, CENTER_ALIGNMENT);
        add(play,CENTER_ALIGNMENT);
        add(createServer, CENTER_ALIGNMENT);
        add(joinServer, CENTER_ALIGNMENT);
        add(back, CENTER_ALIGNMENT);

        setVisible(true);
    }

    /*private void configFrame() {
        setSize(WIDTH, HEIGHT);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton createServer = renderer.createButton("Create Server", new ServerActionListener(), 150, 50);
        ActionListener listener = new Actionlistener() {
            public void actionperformed(Actionevent e){
                if(menuOpen){
                    createServer.setVisible(false);
                }
            }
        }
        JButton play = rendered.createButton("Play", listener, 150,50);
        add(play,CENTER_ALIGNMENT);
        add(createServer, CENTER_ALIGNMENT);
        add(renderer.createButton("Join Server", new ClientActionListener(), 150, 50), CENTER_ALIGNMENT);

        setVisible(true);
    }*/

    /**
     * choosing the type of buttons and various other things for the window.
     * @param nameOfStyle probably best to use nimbus or metal, as those are autoinstalled on lab machines
     */
    private void updateLookAndFeel(String nameOfStyle){

        try {

            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){

                if ("Nimbus".equals(info.getName())){
                    UIManager.setLookAndFeel(info.getClassName());
                    break;

                }

            }

        } catch (Exception e) {
            // Nimbus look and feel is unavailable, probably best to leave it at standard
        }
    }



    /*
    public static void main(String[] args) {
        new StartMenu();
    }
    */

}
