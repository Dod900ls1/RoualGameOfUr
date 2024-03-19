package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            ServerSocket ss = new ServerSocket(3333);
            System.out.println("Server started. Waiting for client to connect...");
            Socket s = ss.accept();
            System.out.println("Client connected.");

            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String str = "", str2 = "";
            while (!str.equals("stop")) {
                str = din.readUTF().toString();
                System.out.println("Client says: " + str);
                str2 = br.readLine();
                dout.writeUTF(str2);
                dout.flush();
            }

            din.close();
            dout.close();
            s.close();
            ss.close();
        } catch (IOException error) {
            System.err.println("An error occurred: " + error.getMessage());
            // TODO We can provide instructions or suggestions to resolve the issue.
            System.err.println("Please make sure port 3333 is available and try again.");
        }
    }

}
