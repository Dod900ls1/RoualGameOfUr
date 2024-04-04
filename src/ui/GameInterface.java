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
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * StartMenu class ought to create start menu for the player where they would be
 * able to connect to a server or to start an offline session with AI or their friend.
 */
public class GameInterface extends JFrame{
    private final GameController controller;
    private JPanel gamePanel = new JPanel();
    private JPanel dicePanel;
    private JPanel otherButtonsPanel;

    private BoardInterface boardInterface;
    private JButton roll;
    private JButton exit;
    private JButton instructions;
    

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
        }else{
            disableRoll();
        }
   }

    private void configFrame() {
        setSize(800, 600);
        setLayout(new FlowLayout());
        WindowListener exitListener = new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                setVisible(false);
                dispose();
                controller.gameClosed();
            }
        };
        addWindowListener(exitListener);
    }

    public GameState getGameState(){

        return controller.getGameState();
    }

    private void configBoard() {
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
                controller.gameClosed();
            }
        };

        ActionListener instructionsListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JOptionPane.showInternalMessageDialog(null,
                    "<html>The Royal Game of Ur is played on a board consisting of 21 squares, divided into three rows. <br>Each player has 7 pieces, and the objective is to move all pieces off the board before your opponent does.<br>Players take turns rolling a set of 4 dice 4-sided dice, each with 2 corners marked.<br>A roll of the dice determines how many spaces a player can move their pieces.<br>Pieces can only move in one direction along the board, following the path indicated by the coloured squares.<br>If a player lands on a square occupied by an opponent's piece, the opponent's piece is sent back to the starting area,<br>but if a player lands on a square that displays a rosette, their piece is safe and cannot be sent back.<br>Multiple pieces cannot be placed on the same square<br>A player can only move their pieces off the board if they get exactly the right number, no more.<br>The first player to move all their pieces off the board wins the game.<br>Enjoy the Royal Game of Ur and may the best player win!</html>","Instructions",1);

            }
        };
        dicePanel = new JPanel();
        otherButtonsPanel = new JPanel();
        otherButtonsPanel.setLayout(new GridLayout(2,2));
        otherButtonsPanel.add(dicePanel);
        roll = new JButton("Roll");
        exit = new JButton("Exit");
        instructions = new JButton("Instructions");
        roll.addActionListener(rollListener);
        exit.addActionListener(exitListener);
        instructions.addActionListener(instructionsListener);
        roll.setPreferredSize(new Dimension(150,50));
        exit.setPreferredSize(new Dimension(150,50));
        otherButtonsPanel.add(roll);
        otherButtonsPanel.add(exit);
        otherButtonsPanel.add(instructions);
        add(otherButtonsPanel);
        setVisible(true);
    }

    public void showRollResult(int rollResult) {
        System.out.println(rollResult);
        dicePanel.removeAll();
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
            JLabel label = new JLabel(new ImageIcon(new ImageIcon(location).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
            label.setVisible(true);
            dicePanel.add(label);
        }
        dicePanel.setLayout(new GridLayout(2,2));
        dicePanel.revalidate(); 
        dicePanel.repaint();
    }

    public void disableRoll(){
        roll.setEnabled(false);
    }

    private void enableRoll() {
        roll.setEnabled(true);
    }


    public void showNoMovesMessage() {
        System.out.println("no moves");
        JOptionPane.showMessageDialog(null,
            "There are currently no moves you can make","No Moves",2);
    }
}
