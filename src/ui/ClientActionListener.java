package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class ClientActionListener implements ActionListener {
    private Socket s;
    private DataInputStream din;
    private DataOutputStream dout;

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
                connectToServer(host, port);

                passInfo(dout, din);

                close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "An error occurred while communicating with the server: " + ex.getMessage());
                System.err.println("Please make sure the server is running and try again.");
            }
        }
    }

    public void connectToServer(String host, int port) throws IOException {
        Socket s = new Socket(host, port);
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());
    }

    private void close() throws IOException {
        dout.close();
        s.close();
    }

    public void passInfo(DataOutputStream dout, DataInputStream din) throws IOException {
        String str = "", str2 = "";
        while (!str.equals("stop")) {
            str = JOptionPane.showInputDialog(null, "Enter response to server:");
            dout.writeUTF(str);
            dout.flush();
            str2 = din.readUTF();
            JOptionPane.showMessageDialog(null, "Server says: " + str2);
        }
    }
}
