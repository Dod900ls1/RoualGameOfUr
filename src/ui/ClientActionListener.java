package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Socket s = new Socket("localhost", 3333);
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String str = "", str2 = "";
            while (!str.equals("stop")) {
                str = br.readLine();
                dout.writeUTF(str);
                dout.flush();
                str2 = din.readUTF();
                System.out.println("Server says: " + str2);
            }

            dout.close();
            s.close();
        } catch (IOException error) {
            System.err.println("An error occurred while communicating with the server: " + error.getMessage());
            // TODO We can provide instructions or suggestions to the user on how to resolve
            // the issue.
            System.err.println("Please make sure the server is running and try again.");
        }
    }

}
