package ui;

import controller.MenuController;
import controller.action.menu.MenuClosed;
import controller.action.menu.MenuClosed.*;
import game.UrGame;
import player.Player;
import player.PlayerHuman;
import player.PlayerOptions;

import java.io.File;
import java.util.Random;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * StartMenu class ought to create start menu for the player where they would be
 * able to connect to a server or to start an offline session with AI or their friend.
 */
public class GameInterface extends JFrame{
    private UrGame game = new UrGame(new PlayerOptions[] {new PlayerOptions(1, true), new PlayerOptions(1, true)});
    Player[] players = game.getPlayers(); //TODO convert from player to playerHuman
    private JPanel gamePanel = new JPanel();
    JPanel dicePanel = new JPanel();
    private JButton[][] boardSpaces = new JButton[8][3];

    public GameInterface(){
        configFrame();
        configBoard();
    }

    private void configFrame() {
        setSize(10000, 10000);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void getGameState(){
        //get the game state
        move();
    }

    public void move(){
        //change position of pieces && let the player make a move
    }

    private void configBoard() {

        ActionListener buttonListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //normal button is pressed
                JButton button = (JButton)e.getSource();
                button.setBackground(Color.BLACK);
                getGameState();
            }
        };

        ActionListener starListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JButton button = (JButton)e.getSource();
                button.setBackground(Color.PINK);
                getGameState();
            }
        };

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                if((i == 2 || i == 3) && (j == 0 || j == 2)){
                    gamePanel.add(new JLabel("out box"));
                }else if((i==4 && j==1) || (( i == 1 || i == 7) && ( j == 0 || j == 2))){
                    boardSpaces[i][j] = new JButton("✯");
                    boardSpaces[i][j].addActionListener(starListener);

                    boardSpaces[i][j].setPreferredSize(new Dimension(150,50));
                    gamePanel.add(boardSpaces[i][j]);
                }else{
                    boardSpaces[i][j] = new JButton(" ");
                    boardSpaces[i][j].addActionListener(buttonListener);
    
                    boardSpaces[i][j].setPreferredSize(new Dimension(150,50));
                    gamePanel.add(boardSpaces[i][j]);
                }

            }   
        }

        gamePanel.setLayout(new GridLayout(8,3));
        add(gamePanel);

        ActionListener rollListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                roll(players[0].rollDiceGetArray());
            }
        };

        ActionListener exitListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setVisible(false);
                dispose();
            }
        };

        JButton roll = new JButton("Roll");
        JButton exit = new JButton("Exit");
        roll.addActionListener(rollListener);
        exit.addActionListener(exitListener);
        roll.setPreferredSize(new Dimension(150,50));
        exit.setPreferredSize(new Dimension(150,50));
        add(roll);
        add(exit);
        setVisible(true);
    }

    private void roll(int[] dice){
        remove(dicePanel);
        dicePanel = new JPanel();
        Random random = new Random();
        for(int i = 0; i < 4; i++){
            String location = "src/ui/diceStates/dice" + random.nextInt(3) + "_";
            if(dice[i]==0){
                location += 0 + ".png";
            }else{
                location += 1 + ".png";
            }
            JLabel label = new JLabel(new ImageIcon(location));
            label.setVisible(true);
            dicePanel.add(label);
        }
        dicePanel.setLayout(new GridLayout(2,2));
        add(dicePanel);
        setVisible(true);
    }
}

