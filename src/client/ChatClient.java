package client;

import client.ui.ChatUI;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private ChatUI ui;

    public ChatClient() {
        this.username = JOptionPane.showInputDialog("Ingresa tu usuario:");
        if (username == null || username.isEmpty())
            System.exit(0);

        try {
            // socket = new Socket("localhost", 9090);
            socket = new Socket("192.168.1.1", 9090);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(username);

            ui = new ChatUI(username, e -> {
                String msg = ui.getMessageAndClear();
                if (!msg.isEmpty())
                    out.println(msg);
            });

            new MessageReader().start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar: " + e.getMessage());
            System.exit(1);
        }
    }

    private class MessageReader extends Thread {
        public void run() {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    ui.appendMessage(line);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Conexión perdida.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClient::new);
    }
}
