package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ServerActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // Show a dialog to get the socket ID from the user
        JTextField socketIdField = new JTextField();
        Object[] message = { "Enter Socket ID:", socketIdField };
        int option = JOptionPane.showConfirmDialog(null, message, "Socket ID", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                // Parse the socket ID entered by the user
                int socketId = Integer.parseInt(socketIdField.getText());

                // Start the server with the specified socket ID
                ServerSocket ss = new ServerSocket(socketId);
                System.out.println("Server started on port " + socketId + ". Waiting for client to connect...");
                Socket s = ss.accept();
                System.out.println("Client connected.");

                DataInputStream din = new DataInputStream(s.getInputStream());
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());

                // Show a dialog to display the input/output streams
                JOptionPane.showMessageDialog(null, "Server started on port " + socketId
                        + ".\nClient connected.\nYou can now communicate with the client.");

                // Continuously read from the client and display messages
                String str = "", str2 = "";
                while (!str.equals("stop")) {
                    str = din.readUTF().toString();
                    System.out.println("Client says: " + str);
                    str2 = JOptionPane.showInputDialog(null, "Enter response to client:");
                    dout.writeUTF(str2);
                    dout.flush();
                }

                // Close resources
                din.close();
                dout.close();
                s.close();
                ss.close();
            } catch (NumberFormatException ex) {
                //TODO Properly handle invalid socket ID input
                JOptionPane.showMessageDialog(null, "Invalid socket ID entered. Please enter a valid integer.");
            } catch (IOException ex) {
                // Handle IO exceptions
                JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage());
                // TODO We can provide instructions or suggestions to resolve the issue.
                JOptionPane.showMessageDialog(null, "Please make sure the specified port is available and try again.");
            }
        }
    }
}
