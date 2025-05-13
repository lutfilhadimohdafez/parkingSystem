package MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Parking Management System");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(220, 53, 69);
                Color color2 = new Color(139, 0, 0);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("WELCOME TO", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("SMART PARKING MANAGEMENT", SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Arial", Font.BOLD, 30));
        subtitle.setForeground(new Color(255, 215, 0));

        JLabel institution = new JLabel("Managed by Team 1", SwingConstants.CENTER);
        institution.setAlignmentX(Component.CENTER_ALIGNMENT);
        institution.setFont(new Font("Arial", Font.BOLD, 30));
        institution.setForeground(Color.WHITE);

        JButton startButton = new JButton("START");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFocusPainted(false);
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setPreferredSize(new Dimension(120, 50));
        startButton.setMaximumSize(new Dimension(150, 50));

        startButton.addActionListener(e -> {
            
            new Platenumber.plate(); // Call the plate.java class
        });

        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(subtitle);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(institution);
        mainPanel.add(Box.createVerticalStrut(40));
        mainPanel.add(startButton);

        add(mainPanel);
        setVisible(true);
    }
}
