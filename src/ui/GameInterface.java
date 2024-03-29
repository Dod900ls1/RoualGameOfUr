package ui;

import controller.MenuController;
import controller.action.menu.MenuClosed;
import controller.action.menu.MenuClosed.*;

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

    private JPanel gamePanel = new JPanel();
    private JButton[][] boardSpaces = new JButton[8][3];

    public GameInterface(){
        configFrame();
        configBoard();
    }

    private void configFrame() {
        setSize(800, 600);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }



    private void configBoard() {

        ActionListener buttonListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JButton button = (JButton)e.getSource();
                button.setBackground(Color.BLACK);
            }
        };

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                boardSpaces[i][j] = new JButton(" ");
                boardSpaces[i][j].addActionListener(buttonListener);

                boardSpaces[i][j].setPreferredSize(new Dimension(150,50));
                gamePanel.add(boardSpaces[i][j]);
            }   
        }

        boardSpaces[2][0].setVisible(false);
        boardSpaces[3][0].setVisible(false);
        boardSpaces[2][2].setVisible(false);
        boardSpaces[3][2].setVisible(false);

        // TableCellRenderer tableRenderer;
        // JScrollPane scrollPane;
        // JTable table = new JTable(new JTableButtonModel(boardSpaces));
        // tableRenderer = table.getDefaultRenderer(JButton.class);
        // table.setDefaultRenderer(JButton.class, new JTableButtonRenderer(tableRenderer));
        // scrollPane = new JScrollPane(table);
        // add(scrollPane, BorderLayout.CENTER);
        /*new JTable(boardSpaces, new String[]{"Roll","Turn","Escape"});
        table.setDefaultRenderer(Object.class, new ButtonRenderer());
        */
        // table.setRowHeight(50);
        // table.setEnabled(true);
        // add(table,CENTER_ALIGNMENT);
        gamePanel.setLayout(new GridLayout(8,3));
        add(gamePanel);

        setVisible(true);
    }

    public static void main(String[] args){
        new GameInterface();
    }
}

class JTableButtonRenderer implements TableCellRenderer {

    private TableCellRenderer defaultRenderer;

    public JTableButtonRenderer(TableCellRenderer renderer) {
       defaultRenderer = renderer;

    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if(value instanceof Component){

            return (Component)value;
            
        }

        return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    }
}


class JTableButtonModel extends AbstractTableModel {

    ActionListener buttonListener = new ActionListener(){

        public void actionPerformed(ActionEvent e){
            JButton button = (JButton)e.getSource();
            button.setBackground(Color.BLACK);
        }

    };

    ActionListener rollListener = new ActionListener(){
        public void actionPerformed(ActionEvent e){
            //roll
        }
    };

    ActionListener quitListener = new ActionListener(){
        public void actionPerformed(ActionEvent e){
            //go back to menu
        }
    };



    private JButton[][] rows;
    private JButton rollDice;
    private JButton quitGame;
    private Object[] columns = {rollDice,"Turn",quitGame};


    public JTableButtonModel(JButton[][] rows){
        rollDice = new JButton("Roll");
        quitGame = new JButton("Quit");
    
        rollDice.addActionListener(rollListener);
        quitGame.addActionListener(quitListener);

        this.rows = rows;
    }

    public int getRowCount() {
       return rows.length;
    }
    public int getColumnCount() {
       return columns.length;
    }
    public Object getValueAt(int row, int column) {
       return rows[row][column];
    }
    public boolean isCellEditable(int row, int column) {
       return false;
    }

    public Class getColumnClass(int column) {
       return getValueAt(0, column).getClass();
    }
 }