package client;

import client.ui.ChatUI;
import client.ui.LoginFrame;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ChatClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private ChatUI ui;

    public ChatClient() {
        // Primero crea la ventana

        LoginFrame login = new LoginFrame(null);    // Luego le agregas el listener con acceso a la misma instancia
        login.setLoginAction(e -> {
            username = login.getUsername();
            if (username == null || username.isEmpty()) {
                JOptionPane.showMessageDialog(login, "Por favor ingresa un nombre.");
                return;
            }

            login.dispose(); // Cierra la ventana de login

            try {
                socket = new Socket("localhost", 9090); // Usa tu IP si aplica
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(username);

                ui = new ChatUI(username, event -> {
                    String msg = ui.getMessageAndClear();
                    if (!msg.isEmpty()) {
                        out.println(msg);
                    }
                });

                new MessageReader().start();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al conectar: " + ex.getMessage());
                System.exit(1);
            }
        });

        login.setVisible(true);
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
