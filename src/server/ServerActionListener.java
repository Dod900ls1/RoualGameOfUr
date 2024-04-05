package server;

import ai.agent.Agent;
import controller.MenuController;
import controller.action.game.GameStartedWithServer;
import player.Player;
import player.PlayerOptions;
import server.message.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

// TODO - add a default option in here and clients
/**
 * ActionListener implementation for starting a server.
 */
public class ServerActionListener extends NetworkActionListener {

    private final ClientActionListener clientActionListener;
    private final MenuController parentListener;
    private ServerSocket serverSocket;
    private int serverColor;

    /**
     * Constructs a new ServerActionListener with the specified parent listener.
     *
     * @param parentListener the parent MenuController
     */
    public ServerActionListener(MenuController parentListener, ClientActionListener clientActionListener) {
        this.parentListener = parentListener;
        this.clientActionListener = clientActionListener;

    }

    /**
     * Starts the server on the specified IP address and socket ID.
     *
     * @param socketId  the socket ID on which the server will listen
     */
    private void start(int socketId) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            serverSocket = new ServerSocket(socketId);
            JOptionPane.showMessageDialog(null,
                    "Server started on " + address.getHostAddress() + " : " + socketId + ". Waiting for client to connect...");
            System.out.println("Server started on " + address.getHostAddress() + " : " + socketId + ". Waiting for client to connect...");
            remoteSocket = serverSocket.accept();

            System.out.println("Client connected.");

            din = new DataInputStream(remoteSocket.getInputStream());
            dout = new DataOutputStream(remoteSocket.getOutputStream());

            startListening();

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
<<<<<<< HEAD
||||||| cbd6090
        JTextField ipAddressField = new JTextField("localhost");
=======
        this.parentListener.closeStartMenu();
        JTextField ipAddressField = new JTextField("localhost");
>>>>>>> refs/remotes/origin/main
        JTextField socketIdField = new JTextField("1111");
        Object[] message = { "Enter Socket ID:", socketIdField };
        int option = JOptionPane.showConfirmDialog(null, message, "Server Configuration", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            int socketId = parseSocketId(socketIdField.getText());
            if (socketId != -1) {
                start(socketId);

                //passInfo(this.din, this.dout);

                //stop();
            } else {
                showInvalidSocketIdError();
            }
        }else{
            this.parentListener.openStartMenu();
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

            GameStartedWithServer gameStartedEvent = new GameStartedWithServer(
                    new GameStartedWithServer.GameStartedWithServerEventSource(new PlayerOptions[] {
                            new PlayerOptions(serverColor, true, null, null ),
                            new PlayerOptions(clientColor, false, Agent.Agents.REMOTE, null)}
                            , this)
            );
            this.parentListener.actionPerformed(gameStartedEvent);

            dout.writeUTF("game has started");
            dout.flush();

        } catch (IOException e) {
            System.err.println("An error occured while communicating with client: " + e.getMessage());
        }
    }






//    @Override
//    public void sendMessageToRemote(Message message) {
//        sendMessageToClient(message);
//    }
//
//    public void sendMessageToClient(Message messageToClient){
//        switch (messageToClient.type()){
//            case READY_TO_START:
//                sendReadyToStart(messageToClient);
//                break;
//            case ASSIGN_COLOR:
//                break;
//            case GAME_STATE:
//                sendGameState(messageToClient);
//                break;
//            case PLAYER_MOVE:
//                break;
//
//        }
//
//    }
//
//    private void sendGameState(Message messageToClient) {
//
//        //sends message wth current game state stash (JSON) to client - client updates their game from stash and then plays their turn
//    }
//
//    private void sendReadyToStart(Message readyToStartMessage){
//
//        //send ready to start message with message data as a string with game info - readyToStartMessage needs data field to be this string
//    }
//
//
//
//


    @Override
    void processMessageFromRemote(Message message) {
        switch (message.type()){
            case ASSIGN_COLOR -> {
                receiveRemoteColourAssignment(message);
            }
            case GAME_STATE -> {
                receiveGameState(message);
            }
            case GAME_OVER -> {
                receiveGameState(message);

            }
        }
    }

    private void receiveRemoteColourAssignment(Message remoteColourAssignmentMessage) {
        serverColor = (Integer)remoteColourAssignmentMessage.data();
        int clientColor;
        if (serverColor == Player.LIGHT_PLAYER){
            clientColor = Player.DARK_PLAYER;
        }else{
            clientColor = Player.LIGHT_PLAYER;
        }
        GameStartedWithServer gameStartedEvent = new GameStartedWithServer(
                new GameStartedWithServer.GameStartedWithServerEventSource(new PlayerOptions[] {
                        new PlayerOptions(serverColor, true, null, null ), //SERVER SHOULD GO FIRST
                        new PlayerOptions(clientColor, false, Agent.Agents.REMOTE, null),
                        }
                        , this)
        );
        this.parentListener.actionPerformed(gameStartedEvent);

    }



    /**
     * Stops the server and closes resources.
     */
    private void stop() {
        try {
            din.close();
            dout.close();
            serverSocket.close();
            remoteSocket.close();
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
     * Displays a message indicating that the socket id entered is not valid
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
