package server;
import javax.swing.*;

import controller.MenuController;
import ui.PlayerSelectionWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientActionListener implements ActionListener {

    private MenuController parentListener;

    public ClientActionListener(MenuController parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        connect();
    }

    public void connect() {
        // Show dialog to get host and port from user
        JTextField hostField = new JTextField("138.251.29.207"); // Default IP address provided
        JTextField portField = new JTextField();
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
                Socket socket = new Socket(host, port);
                new PlayerSelectionWindow(socket); // Create PlayerSelectionWindow when connected
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "An error occurred while communicating with the server: " + e.getMessage());
                // System.err.println("Please make sure the server is running and try again.");
                System.err.println(e.fillInStackTrace());
            }
        }
    }
}
