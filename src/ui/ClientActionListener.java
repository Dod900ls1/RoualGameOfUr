package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;


public class ClientActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // Show dialog to get host and port from user
        JTextField hostField = new JTextField("138.251.29.207"); // Default IP address provided
        hostField.addFocusListener(new FocusListener() { // Gets rid of a default value if user clicks on a textField
            @Override
            public void focusGained(FocusEvent e) {
                hostField.setText("");
            }
            @Override
            public void focusLost(FocusEvent e) {            
            }
        });
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
                // Connect to the specified host and port
                Socket s = new Socket(host, port);
                DataInputStream din = new DataInputStream(s.getInputStream());
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());


                String str = "", str2 = "";
                while (!str.equals("stop")) {
                    str = JOptionPane.showInputDialog(null, "Enter response to server:");
                    dout.writeUTF(str);
                    dout.flush();
                    str2 = din.readUTF();
                    JOptionPane.showMessageDialog(null, "Server says: " + str2);
                }

                // Close resources
                dout.close();
                s.close();
            } catch (ConnectException ex) {
                //TODO Handle connection refused error
                JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server: Connection refused.\nPlease make sure the server is running and try again.");
            } catch (IOException ex) {
                //TODO Handle other IO exceptions
                JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server: " + ex.getMessage());
                // TODO Provide instructions or suggestions to the user on how to resolve the issue.
                System.err.println("Please make sure the server is running and try again.");
            }
        }
    }

}
