package server;

import javax.swing.*;

import controller.MenuController;
import controller.action.game.GameStarted;
import player.Player;
import player.PlayerOptions;
import ui.Menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// TODO - add a default option in here and clients
/**
 * ActionListener implementation for starting a server.
 */
public class ServerActionListener extends Menu implements ActionListener {
    private MenuController parentListener;
    private DataInputStream din;
    private DataOutputStream dout;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int serverColor;

    /**
     * Constructs a new ServerActionListener with the specified parent listener.
     *
     * @param parentListener the parent MenuController
     */
    public ServerActionListener(MenuController parentListener) {
        super(parentListener);
    }

    /**
     * Starts the server on the specified IP address and socket ID.
     *
     * @param ipAddress the IP address of the server
     * @param socketId  the socket ID on which the server will listen
     */
    private void start(String ipAdderss, int socketId) {
        try {
            serverSocket = new ServerSocket(socketId);
            JOptionPane.showMessageDialog(null,
                    "Server started on " + ipAdderss + " : " + socketId + ". Waiting for client to connect...");
            clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            din = new DataInputStream(clientSocket.getInputStream());
            dout = new DataOutputStream(clientSocket.getOutputStream());

            showServerStartedMessage(socketId);
        } catch (IOException ex) {
            // TODO Provide user with instructions
            showServerError(ex.getMessage());
        }
    }

    /**
     * Handles the action event when the server button is clicked.
     * Opens a dialog for configuring the server and starts the server.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JTextField ipAddressField = new JTextField("138.251.29.207");
        JTextField socketIdField = new JTextField();
        Object[] message = { "Enter IP Address:", ipAddressField, "Enter Socket ID:", socketIdField };
        int option = JOptionPane.showConfirmDialog(null, message, "Server Configuration", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String ipAddress = ipAddressField.getText();
            int socketId = parseSocketId(socketIdField.getText());
            if (socketId != -1) {
                start(ipAddress, socketId);

                passInfo(this.din, this.dout);

                stop();
            } else {
                showInvalidSocketIdError();
            }
        }
    }

    /**
     * Passes information between client and server.
     *
     * @param din  the DataInputStream for reading data from the client
     * @param dout the DataOutputStream for writing data to the client
     */
    private void passInfo(DataInputStream din, DataOutputStream dout) {
        try {
            int clientColor = din.readInt();
            System.out.println(clientColor);

            if (clientColor != Player.LIGHT_PLAYER) {
                serverColor = Player.DARK_PLAYER;
            } else {
                serverColor = Player.LIGHT_PLAYER;
            }

            new GameStarted(
                    new GameStarted.GameStartedEventSource(new PlayerOptions[] {
                            new PlayerOptions(clientColor, false),
                            new PlayerOptions(serverColor, false)

                    }));

            dout.writeUTF("game has started");
            dout.flush();

        } catch (IOException e) {
            System.err.println("An error occured while communicating with client: " + e.getMessage());
        }
    }

    /**
     * Stops the server and closes resources.
     */
    private void stop() {
        try {
            din.close();
            dout.close();
            serverSocket.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("An error occured while closing resources: " + e.getMessage());
        }
    }

    /**
     * Parses the socket ID from a string.
     * 
     * @param socketIdText the string representation of the socket ID
     * @return the parsed socket ID, or -1 if parsing fails
     */
    private int parseSocketId(String socketIdText) {
        try {
            return Integer.parseInt(socketIdText);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /**
     * Displays a message indicating that the server has started.
     * 
     * @param socketId the port number on which the server is listening
     */
    private void showServerStartedMessage(int socketId) {
        JOptionPane.showMessageDialog(null,
                "Server started on port " + socketId
                        + ".\nClient connected.\nYou can now communicate with the client.");
    }

    /**
     * Displays a message indicating that the server has started.
     *
     * @param socketId the port number on which the server is listening
     */
    private void showInvalidSocketIdError() {
        JOptionPane.showMessageDialog(null, "Invalid socket ID entered. Please enter a valid integer.");
    }

    /**
     * Displays an error message for a server-related error.
     * 
     * @param errorMessage the error message to display
     */
    private void showServerError(String errorMessage) {
        JOptionPane.showMessageDialog(null, "An error occurred: " + errorMessage);
        JOptionPane.showMessageDialog(null,
                "Please make sure the specified port is available and try again.");
    }
}
