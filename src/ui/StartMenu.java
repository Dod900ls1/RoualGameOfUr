package ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


import javax.swing.JFrame;

public class StartMenu extends JFrame {
    private Renderer renderer = new Renderer();
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    public StartMenu() {
        configFrame();
    }

    private void configFrame() {
        setSize(WIDTH, HEIGHT);
        setLayout(new FlowLayout());
        add(renderer.createButton("Create Server", new ServerActionListener(), 150, 50), CENTER_ALIGNMENT);
        add(renderer.createButton("Join Server", new ClientActionListener(), 150, 50), CENTER_ALIGNMENT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    class ServerActionListener implements ActionListener {

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
            //TODO We can provide instructions or suggestions to resolve the issue.
            System.err.println("Please make sure port 3333 is available and try again.");
        }
        }

    }

    class ClientActionListener implements ActionListener {

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
            //TODO We can provide instructions or suggestions to the user on how to resolve the issue.
            System.err.println("Please make sure the server is running and try again.");
        }
        }

    }

    public static void main(String[] args) {
        StartMenu startMenu = new StartMenu();
    }
}
