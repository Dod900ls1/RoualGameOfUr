package ui;

import controller.GameController;
import controller.action.game.RollDice;
import player.Player;
import states.GameState;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
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
    private JPanel dicePanel;
    private JLabel playerTurnLabel;
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

   public void resetForNewTurn(boolean userInputRequired, int playerColour){
        boardInterface.resetForNewTurn();
        if (userInputRequired){
            enableRoll();
        }else{
            disableRoll();
        }
        
        try{
            String playerTurnText = String.format("Current player: %s", (playerColour== Player.LIGHT_PLAYER?"light":"dark"));
            playerTurnLabel.setIcon(new ImageIcon(ImageIO.read(new File("src/ui/pieces/"+ (playerColour== Player.LIGHT_PLAYER?"1":"2") + "-noBg.png")).getScaledInstance(70, 70, Image.SCALE_DEFAULT)));
            playerTurnLabel.setHorizontalTextPosition(JLabel.CENTER);
            playerTurnLabel.setVerticalTextPosition(JLabel.BOTTOM);
            playerTurnLabel.setText(playerTurnText);
            playerTurnLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createSoftBevelBorder(BevelBorder.RAISED),
                BorderFactory.createEmptyBorder(10,10,10,10)));
        }catch(IOException e){
            e.printStackTrace();
        }
        
        
        this.repaint();
   }

    private void configFrame() {
        setSize(1000, 800);
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
        playerTurnLabel = new JLabel("");
        add(playerTurnLabel);

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
                    "<html>The Royal Game of Ur is played on a board consisting of 24 squares, divided into three rows. <br>Each player has 7 pieces, and the objective is to move all pieces off the board before your opponent does.<br>Players take turns rolling a set of 4 dice 4-sided dice, each with 2 corners marked.<br>A roll of the dice determines how many spaces a player can move their pieces.<br>Pieces can only move in one direction along the board, following the path indicated by the coloured squares.<br>If a player lands on a square occupied by an opponent's piece, the opponent's piece is sent back to the starting area,<br>but if a player lands on a square that displays a thistle, their piece is safe and cannot be sent back.<br>Multiple pieces cannot be placed on the same square<br>A player can only move their pieces off the board if they get exactly the right number, no more.<br>The first player to move all their pieces off the board wins the game.<br>Enjoy the Royal Game of Ur and may the best player win!</html>","Instructions",1);

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
        Dimension buttonDimension = new Dimension(200,200);
        roll.setPreferredSize(buttonDimension);
        exit.setPreferredSize(buttonDimension);
        instructions.setPreferredSize(buttonDimension);
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
        // playerTurnLabel.setText("turn skipped: no legal moves");
        // try{
        //     playerTurnLabel.setIcon(new ImageIcon(ImageIO.read(new File("src/ui/pieces/0-noBg.png")).getScaledInstance(70, 70, Image.SCALE_DEFAULT)));
        // }catch(IOException e){
        //     e.printStackTrace();
        // }

        System.out.println("no moves");
    }

    /**
     * Shows if player is winner or loser in dialog box
     * @param isWinner
     */
    public void showWinOrLoseMessage(boolean isWinner) {
        String gameOverText;
        if (isWinner) {
            gameOverText = "Congratulations! You won";
        } else {
            gameOverText = "Better luck next time. You lost";
        }
        showGameOverDialog(gameOverText);
    }

    /**
     * Shows winner and loser in same dialog
     * @param winner
     */
    public void showWinAndLoseMessage(int winner){
        String winnerString, loserString;
        if (winner == Player.LIGHT_PLAYER){
            winnerString = "LIGHT";
            loserString = "DARK";
        }else{
            winnerString = "DARK";
            loserString="LIGHT";
        }
        String gameOverText = String.format("Winner is %s, Loser is %s", winnerString, loserString);
        showGameOverDialog(gameOverText);
    }

    /**
     * Shows game over message in dialog box
     * @param gameOverText
     */
    private void showGameOverDialog(String gameOverText){
        JOptionPane.showInternalMessageDialog(null,gameOverText,"Game Over",1);
        setVisible(false);
        dispose();

    }






}
