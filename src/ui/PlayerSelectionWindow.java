package ui;

import player.Player;
import server.ClientActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A window for selecting the player color (DARK or LIGHT) and sending the
 * selection to the server.
 */
public class PlayerSelectionWindow extends JFrame {

    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;

    private ClientActionListener clientActionListener;

    /**
     * Constructs a new PlayerSelectionWindow with the specified socket.
     *
     * @param socket the socket for communication with the server
     */
    public PlayerSelectionWindow(ClientActionListener clientActionListener) {
        this.clientActionListener = clientActionListener;

//        this.socket = socket;
//        try {
//            din = new DataInputStream(socket.getInputStream());
//            dout = new DataOutputStream(socket.getOutputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        setTitle("Player Selection");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

        JButton darkButton = new JButton("DARK");
        darkButton.setBackground(Color.darkGray);
        JButton lightButton = new JButton("LIGHT");
        darkButton.setBackground(Color.gray);

        darkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendPlayerColor(Player.DARK_PLAYER);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        lightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendPlayerColor(Player.LIGHT_PLAYER);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel.add(darkButton);
        panel.add(lightButton);

        add(panel);

        setVisible(true);
    }

    /**
     * Sends the selected player color to the server.
     *
     * @param playerColor the selected player color
     * @throws IOException if an I/O error occurs while sending the color
     */
    private void sendPlayerColor(int playerColor) throws IOException {
//            dout.writeInt(playerColor);
//            dout.flush();
//            String message = din.readUTF();
//            System.out.println("Message from server: " + message);

        clientActionListener.receiveColourSelected(playerColor);

        // Close the frame after sending the player color
        dispose();
    }
}
