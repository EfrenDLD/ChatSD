package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ChatClient() {
        requestUsername();
        initGUI();
        connect();
        listenMessages();
    }

    private void requestUsername() {
        username = JOptionPane.showInputDialog(null, "Ingrese su nombre de usuario:", "Nombre", JOptionPane.PLAIN_MESSAGE);
        if (username == null || username.trim().isEmpty()) {
            System.exit(0);
        }
    }

    private void initGUI() {
        frame = new JFrame("Chat de Java - " + username);
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(Color.BLACK);
        chatArea.setForeground(Color.GREEN);
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        inputField = new JTextField();
        inputField.addActionListener(e -> sendMessage());

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);

        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void connect() {
        try {
            Socket socket = new Socket("localhost", 9090);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(username);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "No se pudo conectar al servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void listenMessages() {
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    chatArea.append(line + "\n");
                }
            } catch (IOException e) {
                chatArea.append("\nConexión perdida con el servidor.\n");
            }
        }).start();
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            out.println(msg);
            inputField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClient::new);
    }
}

