package server;

import ai.agent.Agent;
import controller.GameController;
import controller.MainController;
import controller.MenuController;
import controller.action.game.GameStartedAsClient;
import player.PlayerOptions;
import server.message.Message;
import server.message.MessageType;
import ui.PlayerSelectionWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

public class ClientActionListener extends NetworkActionListener {
    private Socket socket;
    private MenuController parentListener;
    private MainController mainController;

    private GameController gameController;
    private int colourSelected;


    /**
     * Constructs a new ClientActionListener with the specified parent listener.
     *
     * @param parentListener the parent MenuController
     */
    public ClientActionListener(MenuController parentListener) {
        this.parentListener = parentListener;
    }



    /**
     * Handles the action event when the client button is clicked.
     * Connects to the server and opens a PlayerSelectionWindow for selecting player
     * color.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        connect();
    }

    /**
     * Connects to the server using the specified host and port.
     * Shows a dialog to get the host and port from the user.
     */
    public void connect() {
        // Show dialog to get host and port from user
        JTextField hostField = new JTextField("localhost"); // Default IP address provided
        JTextField portField = new JTextField("1111");
        Object[] message = { "Enter Host:", hostField, "Enter Port:", portField };
        int option = JOptionPane.showConfirmDialog(null, message, "Connect to Server", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String host = hostField.getText();
            int port;

            try {
                port = Integer.parseInt(portField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid port number entered. Please enter a valid integer.");
                return; // Exit method if port number is invalid
            }

            try {
                remoteSocket = new Socket(host, port);
                startListening();
                new PlayerSelectionWindow(this); // Create PlayerSelectionWindow when connected

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "An error occurred while communicating with the server: " + e.getMessage());
                // System.err.println("Please make sure the server is running and try again.");
                System.err.println(e.fillInStackTrace());
            }
        }
    }

    @Override
    void processMessageFromRemote(Message message) {
        switch (message.type()) {
            case READY_TO_START -> {
                receiveReadyToStart(message);
            }
            case GAME_STATE -> {
                receiveGameState(message);
            }

        }

    }


    /**
     * Receives message READY_TO_START
     * @param message
     */
    private void receiveReadyToStart(Message message) {

        //flip player options - client is now human, server is remote

        PlayerOptions[] playerOptions = new PlayerOptions[]{
                new PlayerOptions(getRemoteColourFromLocalColour(colourSelected), false, Agent.Agents.REMOTE, null),
                new PlayerOptions(colourSelected, true, null, null)
        };

        mainController = new MainController();
        mainController.actionPerformed(
                new GameStartedAsClient(
                        new GameStartedAsClient.GameStartedAsClientEventSource(playerOptions, this)
                )
        );
        gameController = mainController.getGameController();






    }


    public void start(){

    }

    public void receiveColourSelected(int playerColour) {
        this.colourSelected = playerColour;
        int remoteColour = getRemoteColourFromLocalColour(colourSelected);
        sendMessageToRemote(new Message(MessageType.ASSIGN_COLOR, remoteColour));
    }


}
