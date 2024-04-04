package ui;

import ai.agent.Agent;
import ai.metric.Metric;
import controller.MenuController;
import controller.action.game.GameStarted;
import controller.action.menu.MenuClosed;
import player.Player;
import player.PlayerOptions;
import server.ClientActionListener;
import server.ServerActionListener;

import javax.swing.*;
import javax.swing.SwingConstants;
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


    /**
     * Example of {@link MenuClosed.MenuClosedEventSource#params() MenuClosed.MenuClosedEventSource.params} field
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

    /**
     * configures frame size, layout etc
     */
    private void configFrame() {
        setSize(WIDTH, HEIGHT);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * configures menu and options. Buttons are self-referential and therefore cannot be easily broken into smaller sections.
     */
    private void configMenu() {

        //these button need to be manually created as the actionlisteners are self-referential;
        JButton play = new JButton("Play");
        JButton playOnline = new JButton("Play Online");
        JButton playAgainstAI = new JButton("Play Locally");
        //JButton playLocally = new JButton("Play Locally");

        ClientActionListener clientActionListener = new ClientActionListener(parentListener);
        ServerActionListener serverActionListener = new ServerActionListener(parentListener, clientActionListener);


        JButton createServer = renderer.createButton("Create Server", serverActionListener, 150, 50);
        JButton joinServer = renderer.createButton("Join Server", clientActionListener, 150, 50);

        String[] AIDifficultySettings = {"Human Player", "Random","Greedy - Maximise Advancement","Greedy - Maximise Post Board","Easy Expectiminimax - Maximise Advancement","Easy Expectiminimax - Maximise Post Board","Hard Expectiminimax - Maximise Advancement","Hard Expectiminimax - Maximise Post Board"};
        JComboBox<String> player1AISetting = new JComboBox<String>(AIDifficultySettings);
        JComboBox<String> player2AISetting = new JComboBox<String>(AIDifficultySettings);
        JButton start = new JButton("Start Game");
        JButton back = new JButton("Back");
        JButton instructions = new JButton("Instructions");
        
        JLabel textLabel = new JLabel();
        textLabel.setText("Game made by Group 15");

        ActionListener playListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                play.setVisible(false);
                playOnline.setVisible(true);
                playAgainstAI.setVisible(true);
                //playLocally.setVisible(true);
                instructions.setVisible(true);
                textLabel.setText("please choose your game mode");

            }
        };

        ActionListener playOnlineListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                playOnline.setVisible(false);
                playAgainstAI.setVisible(false);
                //playLocally.setVisible(false);
                instructions.setVisible(false);

                createServer.setVisible(true);
                joinServer.setVisible(true);
                back.setVisible(true);
                textLabel.setText("either make or join a server on your network");
            }
        };

        ActionListener playAgainstAIListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //add AI difficulty picker
                playOnline.setVisible(false);
                playAgainstAI.setVisible(false);
                //playLocally.setVisible(false);
                instructions.setVisible(false);

                player1AISetting.setVisible(true);
                player2AISetting.setVisible(true);
                start.setVisible(true);
                back.setVisible(true);

                textLabel.setText("<HTML>Please Select the difficulty of the AI <br>Our AI functions on the principles of Expectiminimax,<br>where it follows possible game paths and completes the move most advantageous to the player.<br>The Greedy Agent does not look ahead at all, <br>the easy agent looks ahead 2 moves, and the hard agent 4. <br>Aditionally you can choose if the AI should attempt to maximase how far <br>its pieces are on the board or how mnay of its pieces are in the completed zone</HTML>");




                //#region example of how to fire GameStarted from start menu
                /*StartMenu.this.parentListener.actionPerformed(
                        new GameStarted(
                                new GameStarted.GameStartedEventSource(new PlayerOptions[]{
                                        new PlayerOptions(Player.LIGHT_PLAYER, false, Agent.Agents.GREEDY, Metric.Metrics.MAXIMISE_ADVANCEMENT),
                                        new PlayerOptions(Player.DARK_PLAYER, false, Agent.Agents.EXPECTIMINIMAX_HARD, Metric.Metrics.MAXIMISE_POSTBOARD)

                                })
                        )
                );*/
                //#endregion


            }
        };

        ActionListener backListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
                playOnline.setVisible(true);
                playAgainstAI.setVisible(true);
                //playLocally.setVisible(true);
                instructions.setVisible(false);
                //add any other objects shown in playAgainst AI, playLocally and Play Online
                createServer.setVisible(false);
                joinServer.setVisible(false);
                back.setVisible(false);
                player1AISetting.setVisible(false);
                player2AISetting.setVisible(false);
                start.setVisible(false);
                textLabel.setText("please choose your game mode");
            }
        };

        /*ActionListener playLocallyListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                runGame(true, true, null, null,null,null);
                playOnline.setVisible(false);
                playAgainstAI.setVisible(false);
                playLocally.setVisible(false);
                instructions.setVisible(false);
                back.setVisible(true);
            }
        };*/

        ActionListener startListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String playerOneAction = player1AISetting.getSelectedItem().toString();
                String playerTwoAction = player2AISetting.getSelectedItem().toString();
                startGameFromAI(playerOneAction, playerTwoAction);
                
            }
        };

        ActionListener instructionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                play.setVisible(true);
                playOnline.setVisible(false);
                playAgainstAI.setVisible(false);
                //playLocally.setVisible(false);
                instructions.setVisible(false);
                createServer.setVisible(false);
                joinServer.setVisible(false);
                start.setVisible(false);
                player1AISetting.setVisible(false);
                player2AISetting.setVisible(false);
                back.setVisible(false);
                textLabel.setText("<html>Game Rules: <br>The Royal Game of Ur is played on a board consisting of 21 squares, divided into three rows. <br>Each player has 7 pieces, and the objective is to move all pieces off the board before your opponent does.<br>Players take turns rolling a set of 4 dice 4-sided dice, each with 2 corners marked.<br>A roll of the dice determines how many spaces a player can move their pieces.<br>Pieces can only move in one direction along the board, following the path indicated by the coloured squares.<br>If a player lands on a square occupied by an opponent's piece, the opponent's piece is sent back to the starting area,<br>but if a player lands on a square that displays a rosette, their piece is safe and cannot be sent back.<br>Multiple pieces cannot be placed on the same square<br>A player can only move their pieces off the board if they get exactly the right number, no more.<br>The first player to move all their pieces off the board wins the game.<br>Enjoy the Royal Game of Ur and may the best player win!</html>");
            }
        };

        play.addActionListener(playListener);
        back.addActionListener(backListener);
        playOnline.addActionListener(playOnlineListener);
        playAgainstAI.addActionListener(playAgainstAIListener);
        //playLocally.addActionListener(playLocallyListener);
        start.addActionListener(startListener);
        instructions.addActionListener(instructionListener);

        Dimension preferedSize = new Dimension(150,50);

        play.setPreferredSize(preferedSize);
        back.setPreferredSize(preferedSize);
        playOnline.setPreferredSize(preferedSize);
        playAgainstAI.setPreferredSize(preferedSize);
        //playLocally.setPreferredSize(preferedSize);
        start.setPreferredSize(preferedSize);
        instructions.setPreferredSize(preferedSize);
        textLabel.setPreferredSize(new Dimension(800,200));
        player1AISetting.setPreferredSize(new Dimension(250,50));
        player1AISetting.setBackground(Color.GRAY);
        player2AISetting.setPreferredSize(new Dimension(250,50));
        player2AISetting.setBackground(Color.DARK_GRAY);

        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        play.setVisible(true);
        back.setVisible(false);
        start.setVisible(false);
        playOnline.setVisible(false);
        playAgainstAI.setVisible(false);
        //playLocally.setVisible(false);
        createServer.setVisible(false);
        joinServer.setVisible(false);
        player1AISetting.setVisible(false);
        player2AISetting.setVisible(false);
        instructions.setVisible(false);

        add(back, CENTER_ALIGNMENT);
        add(play,CENTER_ALIGNMENT);
        add(playOnline, CENTER_ALIGNMENT);
        add(playAgainstAI,CENTER_ALIGNMENT);
        //add(playLocally, CENTER_ALIGNMENT);
        add(instructions,CENTER_ALIGNMENT);
        add(createServer, CENTER_ALIGNMENT);
        add(joinServer, CENTER_ALIGNMENT);
        add(player1AISetting,CENTER_ALIGNMENT);
        add(player2AISetting, CENTER_ALIGNMENT);
        add(start,CENTER_ALIGNMENT);
        add(textLabel);


        setVisible(true);
    }

    /**
     * gets information from the AI start options @ runs game with these
     * @param playerOneAction type of player - should be gotten from JComboBox default is "Human Player"
     * @param playerTwoAction type of player - should be gotten from JComboBox
     */
    private void startGameFromAI(String playerOneAction, String playerTwoAction){

        Object[] playerOneSettings = getSettingsFromAction(playerOneAction);
        Object[] playerTwoSettings = getSettingsFromAction(playerTwoAction);
        boolean playerOne = (boolean) playerOneSettings[0];
        boolean playerTwo = (boolean) playerTwoSettings[0];
        Agent.Agents agentOne = (Agent.Agents) playerOneSettings[1];
        Agent.Agents agentTwo = (Agent.Agents) playerTwoSettings[1];
        Metric.Metrics metricOne = (Metric.Metrics) playerOneSettings[2];
        Metric.Metrics metricTwo  = (Metric.Metrics) playerTwoSettings[2];

        runGame(playerOne, playerTwo, agentOne, agentTwo, metricOne, metricTwo);
    }

    /**
     * chooses game settings based on what options the user has selected in JComboBox
     * @param action
     * @return settings[0] = isPlayerHuman, settings[1] = type of agent, settings[2] = metric agent works on
     * settings[1] and settings[2] are null if player is human
     */
    private Object[] getSettingsFromAction(String action){
        Object[] settings = new Object[3];
        switch(action){

            case "Human Player":
                settings[0] = true;
                settings[1] = null;
                settings[2] = null;
                break;
            case "Random":
                settings[0] = false;
                settings[1] = Agent.Agents.RANDOM;
                settings[2] = Metric.Metrics.MAXIMISE_ADVANCEMENT;
                break;
            case "Greedy - Maximise Advancement":
                settings[0] = false;
                settings[1] = Agent.Agents.GREEDY;
                settings[2] = Metric.Metrics.MAXIMISE_ADVANCEMENT;
                break;
            case "Greedy - Maximise Post Board":
                settings[0] = false;
                settings[1] = Agent.Agents.GREEDY;
                settings[2] = Metric.Metrics.MAXIMISE_POSTBOARD;
                break;
            case "Easy Expectiminimax - Maximise Advancement":
                settings[0] = false;
                settings[1] = Agent.Agents.EXPECTIMINIMAX_EASY;
                settings[2] = Metric.Metrics.MAXIMISE_ADVANCEMENT;
                break;
            case "Easy Expectiminimax - Maximise Post Board":
                settings[0] = false;
                settings[1] = Agent.Agents.EXPECTIMINIMAX_EASY;
                settings[2] = Metric.Metrics.MAXIMISE_POSTBOARD;
                break;
            case "Hard Expectiminimax - Maximise Advancement":
                settings[0] = false;
                settings[1] = Agent.Agents.EXPECTIMINIMAX_HARD;
                settings[2] = Metric.Metrics.MAXIMISE_ADVANCEMENT;
                break;
            case "Hard Expectiminimax - Maximise Post Board":
                settings[0] = false;
                settings[1] = Agent.Agents.EXPECTIMINIMAX_HARD;
                settings[2] = Metric.Metrics.MAXIMISE_POSTBOARD;
                break;
            default:
                settings[0] = true;
                settings[1] = null;
                settings[2] = null;
        }
        return settings;
    }

    /**
     * makes an instance of the game based on information about both players
     * @param playerOne true if player is human, false if player is AI
     * @param playerTwo true if player is human, false if player is AI
     * @param agentOne null if player is human, type of AI otherwise
     * @param agentTwo null if player is human, type of AI otherwise
     * @param metricOne null if player is human, metrics for AI otherwise
     * @param metricTwo null if player is human, metric for AI otherwise
     */
    private void runGame(boolean playerOne, boolean playerTwo, Agent.Agents agentOne, Agent.Agents agentTwo, Metric.Metrics metricOne, Metric.Metrics metricTwo){
        setVisible(false);
        //dispose();
        StartMenu.this.parentListener.actionPerformed(
            new GameStarted(
                    new GameStarted.GameStartedEventSource(new PlayerOptions[]{
                            new PlayerOptions(Player.LIGHT_PLAYER, playerOne,agentOne,metricOne),
                            new PlayerOptions(Player.DARK_PLAYER, playerTwo, agentTwo, metricTwo)

                    })
            )
    );
    }

    /**
     * choosing the type of buttons and various other things for the window.
     * @param nameOfStyle probably best to use nimbus or metal, as those are autoinstalled on lab machines
     */
    private void updateLookAndFeel(String nameOfStyle){

        try {

            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){

                if (info.getName().equals("Nimbus")){
                    UIManager.setLookAndFeel(info.getClassName());
                    break;

                }

            }

        } catch (Exception e) {
            // Nimbus look and feel is unavailable, probably best to leave it at standard
        }
    }

}
