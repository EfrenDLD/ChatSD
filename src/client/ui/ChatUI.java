package client.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class ChatUI {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton imageButton;
    private JPanel chatPanel;
    private JScrollPane scrollPane;
    private final Map<String, Color> userColors = new HashMap<>();
    private final Color[] colorPalette = {
            new Color(220, 248, 198), // verde claro
            new Color(255, 242, 204), // amarillo suave
            new Color(204, 229, 255), // azul cielo
            new Color(255, 204, 229), // rosa claro
            new Color(255, 224, 178), // naranja claro
            new Color(225, 245, 254), // celeste
            new Color(240, 240, 240) // gris claro
    };
    private int colorIndex = 0;

    public ChatUI(String username, ActionListener sendListener) {
        frame = new JFrame("Chat - " + username);
        frame.setIconImage(new ImageIcon(getClass().getResource("logo.png")).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        // Crear panel de chat que puede contener texto e imágenes
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        inputField = new JTextField();
        sendButton = new JButton("Enviar");
        sendButton.setBackground(new Color(0, 120, 215));
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(sendListener);
        inputField.addActionListener(sendListener);

        imageButton = new JButton("📷 Imagen");
        imageButton.setBackground(new Color(50, 180, 100));
        imageButton.setForeground(Color.WHITE);
        imageButton.addActionListener(e -> selectAndSendImage());

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(imageButton, BorderLayout.WEST);

        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        inputField.requestFocus();
    }

    public void appendMessage(String msg) {
        if (msg.startsWith("IMG:")) {
            // Imagen sin usuario, solo mostrar la imagen como está
            JLabel label = new JLabel("[Imagen sin remitente]");
            chatPanel.add(label);
            return;
        }

        String sender = "Sistema";
        String content = msg;

        // Detectar si el mensaje tiene formato "usuario: mensaje"
        int sep = msg.indexOf(": ");
        if (sep != -1) {
            sender = msg.substring(0, sep);
            content = msg.substring(sep + 2);
        }

        Color bgColor = getUserColor(sender);

        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new BorderLayout());
        msgPanel.setBackground(bgColor);
        msgPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        msgPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        JLabel nameLabel = new JLabel(sender + ":");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLabel msgLabel;
        if (msg.startsWith("IMG:")) {
            try {
                String base64 = msg.substring(4);
                byte[] imageBytes = java.util.Base64.getDecoder().decode(base64);
                ImageIcon icon = new ImageIcon(imageBytes);
                msgLabel = new JLabel(icon);
            } catch (Exception e) {
                msgLabel = new JLabel("[Error al mostrar imagen]");
            }
        } else {
            msgLabel = new JLabel(content);
            msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        }

        msgPanel.add(nameLabel, BorderLayout.NORTH);
        msgPanel.add(msgLabel, BorderLayout.CENTER);

        chatPanel.add(msgPanel);
        chatPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        chatPanel.revalidate();
        scrollToBottom();
    }

    private void scrollToBottom() {
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        SwingUtilities.invokeLater(() -> vertical.setValue(vertical.getMaximum()));
    }

    private void selectAndSendImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                byte[] imageBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
                // Prefijamos el mensaje con "IMG:" para que el receptor sepa que es una imagen
                sendImage("IMG:" + base64Image);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error al leer la imagen.");
            }
        }
    }

    private void sendImage(String base64Encoded) {
        // enviar la imagen como un mensaje de texto
        inputField.setText(base64Encoded);
        sendButton.doClick(); // Simula el click en enviar
    }

    private Color getUserColor(String username) {
        return userColors.computeIfAbsent(username, name -> {
            Color color = colorPalette[colorIndex % colorPalette.length];
            colorIndex++;
            return color;
        });
    }

    public String getMessageAndClear() {
        String msg = inputField.getText();
        inputField.setText("");
        return msg;
    }
}
