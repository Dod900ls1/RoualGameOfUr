package ui;

import controller.MenuController;
import controller.action.game.GameStarted;
import controller.action.menu.MenuClosed;
import player.Player;
import player.PlayerOptions;
import server.ClientActionListener;
import server.ServerActionListener;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * StartMenu class ought to create start menu for the player where they would be
 * able to connect to a server or to start an offline session with AI or their friend.
 */
public class StartMenu extends Menu{
    private Renderer renderer = new Renderer();
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private JPanel menu = new JPanel();

    /**
     * Example of {@link MenuClosed.MenuClosedEventSource#params() MenuClosed.MenuClosedEventSource.params} field
     * //TODO figure out what information about play needs to be passed back so game can be created
     * @param playOnline e.g. Is play online?
     */
    record StartMenuClosed(boolean playOnline){}

    /**
     * Constructor of StartMenu class runs a configFrame() method
     * that configures our StartMenu frame.
     * @param parentListener Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     */
    public StartMenu(MenuController parentListener) {
        super(parentListener);
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

        //these button need to be manually created as the actionlisteners are self-referential;
        JButton play = new JButton("Play");
        JButton back = new JButton("Back");
        JButton playOnline = new JButton("Play Online");
        JButton playAgainstAI = new JButton("Play Against AI");
        JButton playLocally = new JButton("Play Locally");

        JButton createServer = renderer.createButton("Create Server", new ServerActionListener(parentListener), 150, 50);
        JButton joinServer = renderer.createButton("Join Server", new ClientActionListener(parentListener), 150, 50);


        ActionListener playListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                play.setVisible(false);
                playOnline.setVisible(true);
                playAgainstAI.setVisible(true);
                playLocally.setVisible(true);

            }
        };

        ActionListener playOnlineListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                playOnline.setVisible(false);
                playAgainstAI.setVisible(false);
                playLocally.setVisible(false);
                createServer.setVisible(true);
                joinServer.setVisible(true);
                back.setVisible(true);
            }
        };

        ActionListener playAgainstAIListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //add AI difficulty picker
                playOnline.setVisible(false);
                playAgainstAI.setVisible(false);
                playLocally.setVisible(false);
                back.setVisible(true);



                //#region example of how to fire GameStarted from start menu
                StartMenu.this.parentListener.actionPerformed(
                        new GameStarted(
                                new GameStarted.GameStartedEventSource(new PlayerOptions[]{
                                        new PlayerOptions(Player.LIGHT_PLAYER, false),
                                        new PlayerOptions(Player.DARK_PLAYER, false)

                                })
                        )
                );
                //#endregion


            }
        };

        ActionListener backListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
                playOnline.setVisible(true);
                playAgainstAI.setVisible(true);
                playLocally.setVisible(true);

                //add any other objects shown in playAgainst AI, playLocally and Play Online
                createServer.setVisible(false);
                joinServer.setVisible(false);
                back.setVisible(false);
            }
        };

        ActionListener playLocallyListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //GameInterface newGame = new GameInterface();

                playOnline.setVisible(false);
                playAgainstAI.setVisible(false);
                playLocally.setVisible(false);
                back.setVisible(true);
            }
        };

        play.addActionListener(playListener);
        back.addActionListener(backListener);
        playOnline.addActionListener(playOnlineListener);
        playAgainstAI.addActionListener(playAgainstAIListener);
        playLocally.addActionListener(playLocallyListener);

        play.setPreferredSize(new Dimension(150,50));
        back.setPreferredSize(new Dimension(150,50));
        playOnline.setPreferredSize(new Dimension(150,50));
        playAgainstAI.setPreferredSize(new Dimension(150,50));
        playLocally.setPreferredSize(new Dimension(150,50));

        play.setVisible(true);
        back.setVisible(false);
        playOnline.setVisible(false);
        playAgainstAI.setVisible(false);
        playLocally.setVisible(false);
        createServer.setVisible(false);
        joinServer.setVisible(false);

        add(back, CENTER_ALIGNMENT);
        add(play,CENTER_ALIGNMENT);
        add(playOnline, CENTER_ALIGNMENT);
        add(playAgainstAI,CENTER_ALIGNMENT);
        add(playLocally, CENTER_ALIGNMENT);
        add(createServer, CENTER_ALIGNMENT);
        add(joinServer, CENTER_ALIGNMENT);


        setVisible(true);
    }

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

}
