package ui;

import javax.swing.*;
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
public class ServerActionListener implements ActionListener {
    /**
     * Starts a server with the specified IP address and socket ID.
     * 
     * @param ipAddress the IP address to bind the server to
     * @param socketId the port number to listen for connections
     */    /**
     * Parses the socket ID from a string.
     * 
     * @param socketIdText the string representation of the socket ID
     * @return the parsed socket ID, or -1 if parsing fails
     */
    private void startServer(String ipAdderss, int socketId) {
        try {
            ServerSocket serverSocket = new ServerSocket(socketId);
            JOptionPane.showMessageDialog(null,
                    "Server started on " + ipAdderss + " : " + socketId + ". Waiting for client to connect...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            DataInputStream din = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());

            showServerStartedMessage(socketId);

            String str = "", str2 = "";
            while (!str.equals("stop")) {
                str = din.readUTF().toString();
                JOptionPane.showMessageDialog(null, "Client says: " + str);
                str2 = JOptionPane.showInputDialog(null, "Enter response to client:");
                dout.writeUTF(str2);
                dout.flush();
            }

            din.close();
            dout.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException ex) {
            // TODO Provide user with instructions
            showServerError(ex.getMessage());
        }
    }

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
                startServer(ipAddress, socketId);
            } else {
                showInvalidSocketIdError();
            }
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
     * Displays an error message for an invalid socket ID.
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
