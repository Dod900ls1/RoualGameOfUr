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
        JTextField socketIdField = new JTextField();
        Object[] message = { "Enter Socket ID:", socketIdField };
        int option = JOptionPane.showConfirmDialog(null, message, "Socket ID", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            int socketId = parseSocketId(socketIdField.getText());
            if (socketId != -1) {
                startServer(socketId);
            } else {
                showInvalidSocketIdError();
            }
        }
    }

    private int parseSocketId(String socketIdText) {
        try {
            return Integer.parseInt(socketIdText);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private void startServer(int socketId) {
        try {
            ServerSocket ss = new ServerSocket(socketId);
            System.out.println("Server started on port " + socketId + ". Waiting for client to connect...");
            Socket s = ss.accept();
            System.out.println("Client connected.");

            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

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
            s.close();
            ss.close();
        } catch (IOException ex) {
            //TODO Provide user with instructions
            showServerError(ex.getMessage());
        }
    }

    private void showServerStartedMessage(int socketId) {
        JOptionPane.showMessageDialog(null,
                "Server started on port " + socketId + ".\nClient connected.\nYou can now communicate with the client.");
    }

    private void showInvalidSocketIdError() {
        JOptionPane.showMessageDialog(null, "Invalid socket ID entered. Please enter a valid integer.");
    }

    private void showServerError(String errorMessage) {
        JOptionPane.showMessageDialog(null, "An error occurred: " + errorMessage);
        JOptionPane.showMessageDialog(null,
                "Please make sure the specified port is available and try again.");
    }
}
