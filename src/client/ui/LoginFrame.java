package client.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JButton loginButton;

    public LoginFrame(ActionListener onLogin) {
        setTitle("Bienvenido a Los Imparables");
        setIconImage(new ImageIcon(getClass().getResource("logo.png")).getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setUndecorated(false);

        // Panel principal con fondo degradado
        JPanel backgroundPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(58, 123, 213);
                Color color2 = new Color(58, 213, 175);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Contenedor de contenido
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        JLabel logo = new JLabel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("logo.png"));
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            logo.setText("💬fsdfsdf");
            logo.setFont(new Font("Segoe UI", Font.PLAIN, 70));
        }

        JLabel titleLabel = new JLabel("Los imparables");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Ingresa tu nombre para cotorrear con los panas");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Caja de texto personalizada
        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBackground(new Color(255, 255, 255));
        usernameField.setForeground(Color.DARK_GRAY);
        usernameField.setCaretColor(Color.BLUE);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Botón moderno con radio
        loginButton = new JButton("ENTRAR");
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0, 150, 136));
        loginButton.setBorder(new LineBorder(Color.WHITE, 2, true));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Bordes redondeados al botón (truco con UIManager)
        loginButton.setBorder(new RoundedBorder(20));
        loginButton.setContentAreaFilled(false);
        loginButton.setOpaque(true);

        // Hover efecto
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 200, 180));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 150, 136));
            }
        });

        loginButton.addActionListener(onLogin);

        // Añadiendo componentes al panel
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(logo);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(titleLabel);
        formPanel.add(subtitleLabel);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(loginButton);

        backgroundPanel.add(formPanel);
        add(backgroundPanel);
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    // Clase para crear botones con bordes redondeados
    private static class RoundedBorder implements Border {

        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    public void setLoginAction(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

}
