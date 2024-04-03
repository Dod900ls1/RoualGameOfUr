package ui;

import controller.GameController;
import controller.action.game.RollDice;
import states.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * StartMenu class ought to create start menu for the player where they would be
 * able to connect to a server or to start an offline session with AI or their friend.
 */
public class GameInterface extends JFrame{
    private final GameController controller;
    private JPanel gamePanel = new JPanel();
    private JPanel dicePanel = new JPanel();

    private BoardInterface boardInterface;
    private JButton roll;
    private JButton exit;

    /**
     * Constructor for new game interface
     *
     * @param controller     {@code GameController} instance presiding over the current {@code UrGame} model in play
     */
    public GameInterface(GameController controller){
        this.controller = controller;
        configFrame();
        configBoard();
    }

   public void resetForNewTurn(boolean userInputRequired){
        boardInterface.resetForNewTurn();
        if (userInputRequired){
            enableRoll();
        }
   }

    private void configFrame() {
        setSize(800, 600);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public GameState getGameState(){

        return controller.getGameState();
    }

    private void configBoard() {

//        ActionListener buttonListener = new ActionListener(){
//            public void actionPerformed(ActionEvent e){
//                //normal button is pressed
//                JButton button = (JButton)e.getSource();
//                button.setBackground(Color.BLACK);
//                getGameState();
//            }
//        };
//
//        ActionListener starListener = new ActionListener(){
//            public void actionPerformed(ActionEvent e){
//                JButton button = (JButton)e.getSource();
//                button.setBackground(Color.PINK);
//                getGameState();
//            }
//        };

//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 3; j++) {
//                if((i == 2 || i == 3) && (j == 0 || j == 2)){
//                    gamePanel.add(new JLabel("out box"));
//                }else if((i==4 && j==1) || (( i == 1 || i == 7) && ( j == 0 || j == 2))){
//                    boardSpaces[i][j] = new JButton("✯");
//                    boardSpaces[i][j].addActionListener(starListener);
//
//                    boardSpaces[i][j].setPreferredSize(new Dimension(150,50));
//                    gamePanel.add(boardSpaces[i][j]);
//                }else{
//                    boardSpaces[i][j] = new JButton(" ");
//                    boardSpaces[i][j].addActionListener(buttonListener);
//
//                    boardSpaces[i][j].setPreferredSize(new Dimension(150,50));
//                    gamePanel.add(boardSpaces[i][j]);
//                }
//
//            }
//        }



        boardInterface = controller.getBoardController().getBoardInterface();
        add(boardInterface);

        ActionListener rollListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                controller.actionPerformed(new RollDice(GameInterface.this, e.getID()));
            }
        };

        ActionListener exitListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setVisible(false);
                dispose();
            }
        };

        roll = new JButton("Roll");
        exit = new JButton("Exit");
        roll.addActionListener(rollListener);
        exit.addActionListener(exitListener);
        roll.setPreferredSize(new Dimension(150,50));
        exit.setPreferredSize(new Dimension(150,50));
        add(roll);
        add(exit);
        setVisible(true);
    }

    public void showRollResult(int rollResult) {
        System.out.println(rollResult);
        remove(dicePanel);
        dicePanel = new JPanel();
        ArrayList<Integer> dice = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            if(rollResult > 0){
                dice.add(1);
                rollResult--;
            }else{
                dice.add(0);
            }
        }
        Collections.shuffle(dice);
        Random random = new Random();
        for(Integer i : dice){
            String location = "src/ui/diceStates/dice" + random.nextInt(3) + "_" + i.toString() + ".png";
            JLabel label = new JLabel(new ImageIcon(location));
            label.setVisible(true);
            dicePanel.add(label);
        }
        dicePanel.setLayout(new GridLayout(2,2));
        add(dicePanel);
    }

    public void disableRoll(){
        roll.setEnabled(false);
    }

    private void enableRoll() {
        roll.setEnabled(true);
    }


    public void showNoMovesMessage() {
        System.out.println("no moves");
        //TODO Show as dialog
    }
}
