import javax.swing.*;

import Platenumber.plate;

import java.awt.*;


public class MainMenu extends JFrame {

    CardLayout cardLayout;
    JPanel mainPanel, menuPanel, parkingPanel;

    public MainMenu() {
        setTitle("Parking Management System");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createMenuPanel();
        createParkingPanel();

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(parkingPanel, "Parking");

        add(mainPanel);
        cardLayout.show(mainPanel, "Menu");

        setVisible(true);
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton startBtn = new JButton("Start Parking System");
        JButton exitBtn = new JButton("Exit");

        startBtn.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new plate());
        });
        exitBtn.addActionListener(e -> System.exit(0));

        menuPanel.add(startBtn);
        menuPanel.add(exitBtn);
    }

    private void createParkingPanel() {
        parkingPanel = new JPanel();
        parkingPanel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Parking System Panel", SwingConstants.CENTER);
        JTextField plateInput = new JTextField();
        JButton backBtn = new JButton("Back to Menu");

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        parkingPanel.add(label, BorderLayout.NORTH);
        parkingPanel.add(plateInput, BorderLayout.CENTER);
        parkingPanel.add(backBtn, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        new MainMenu();
    }
}