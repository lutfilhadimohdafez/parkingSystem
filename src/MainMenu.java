import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import payment.PaymentHandler;
import ToolsforCSV.ToolsforCSV;
import calculate.calculate;
import Platenumber.plate;

public class MainMenu extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel, menuPanel, resultsPanel, paymentPanel;

    // Components for Results Panel
    JPanel paidPanel;
    JTable paidTable;
    DefaultTableModel paidTableModel;
    JPanel ongoingPanel;
    JTable ongoingTable;
    DefaultTableModel ongoingTableModel;
    JPanel warningsPanel;
    JTextArea warningsTextArea;
    JScrollPane warningsScrollPane;

    // Components for Payment Panel
    JPanel paymentInputPanel;
    JLabel plateNumberLabel;
    JTextField plateNumberTextField;
    JButton calculatePaymentButton;
    JTextArea paymentDetailsTextArea;
    JScrollPane paymentDetailsScrollPane;
    JButton payButton;
    JButton backToMenuFromPaymentButton;

    private List<String[]> allParkingData; // To hold the raw data
    private PaymentHandler paymentHandler; // Instance of PaymentHandler

    // Color Theme
    Color primaryColor = new Color(0xF79B72);       // #F79B72
    Color secondaryColor = new Color(0x2A4759);     // #2A4759
    Color lightGray = new Color(0xDDDDDD);          // #DDDDDD
    Color lighterGray = new Color(0xEEEEEE);         // #EEEEEE

    public MainMenu() {
        setTitle("Parking Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(lighterGray); // Set main panel background

        paymentHandler = new PaymentHandler(); // Initialize PaymentHandler

        createMenuPanel();
        createResultsPanel();
        createPaymentPanel(); // Create the payment panel

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(resultsPanel, "Results");
        mainPanel.add(paymentPanel, "Payment"); // Add the payment panel

        add(mainPanel);
        cardLayout.show(mainPanel, "Menu");
        setVisible(false); // Hide main menu initially - changed
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(2, 2, 10, 15)); // 2x2 GridLayout with spacing
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Padding around the panel
        menuPanel.setBackground(lighterGray);

        JButton startBtn = new JButton("Start Parking System");
        startBtn.setBackground(primaryColor);
        startBtn.setForeground(secondaryColor);
        startBtn.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton calculateBtn = new JButton("View Parking Summary"); // Renamed
        calculateBtn.setBackground(primaryColor);
        calculateBtn.setForeground(secondaryColor);
        calculateBtn.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton paymentBtn = new JButton("Initiate Payment"); // New Payment Button
        paymentBtn.setBackground(primaryColor);
        paymentBtn.setForeground(secondaryColor);
        paymentBtn.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBackground(primaryColor);
        exitBtn.setForeground(secondaryColor);
        exitBtn.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        final String filepathCSV = "local_db/db.csv";
        ToolsforCSV csvReaderlol = new ToolsforCSV();
        calculate calculatefunc = new calculate();

        startBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new Platenumber.plate()));

        calculateBtn.addActionListener(e -> {
            allParkingData = csvReaderlol.readAllDataAtOnce(filepathCSV);
            List<String[]> calculationResults = calculatefunc.calculateParkingFees(allParkingData);
            populateResultsPanels(calculationResults);
            cardLayout.show(mainPanel, "Results");
        });

        paymentBtn.addActionListener(e -> cardLayout.show(mainPanel, "Payment")); // Show Payment Panel
        exitBtn.addActionListener(e -> System.exit(0));

        menuPanel.add(startBtn);
        menuPanel.add(calculateBtn);
        menuPanel.add(paymentBtn);
        menuPanel.add(exitBtn);
        // menuPanel.add(csvButton); // Removed
    }

    private void createResultsPanel() {
        resultsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        int padding = 10;

        // Paid Exits Section
        paidPanel = new JPanel(new BorderLayout());
        paidPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        paidTableModel = new DefaultTableModel();
        paidTable = new JTable(paidTableModel);
        paidPanel.add(new JLabel("Paid Exits:"), BorderLayout.NORTH);
        paidPanel.add(new JScrollPane(paidTable), BorderLayout.CENTER);
        resultsPanel.add(paidPanel);

        // Ongoing Parking Section
        ongoingPanel = new JPanel(new BorderLayout());
        ongoingPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        ongoingTableModel = new DefaultTableModel();
        ongoingTable = new JTable(ongoingTableModel);
        ongoingPanel.add(new JLabel("Ongoing Parking:"), BorderLayout.NORTH);
        ongoingPanel.add(new JScrollPane(ongoingTable), BorderLayout.CENTER);
        resultsPanel.add(ongoingPanel);

        // Warnings Section
        warningsPanel = new JPanel(new BorderLayout());
        warningsPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        warningsTextArea = new JTextArea();
        warningsScrollPane = new JScrollPane(warningsTextArea);
        warningsPanel.add(new JLabel("Warnings:"), BorderLayout.NORTH);
        warningsPanel.add(warningsScrollPane, BorderLayout.CENTER);
        resultsPanel.add(warningsPanel);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backToMenuBtn = new JButton("Back to Menu");
        backToMenuBtn.setBackground(primaryColor);
        backToMenuBtn.setForeground(secondaryColor);
        backToMenuBtn.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        backButtonPanel.add(backToMenuBtn);
        resultsPanel.add(backButtonPanel);
    }

    private void createPaymentPanel() {
        paymentPanel = new JPanel(new BorderLayout());
        paymentInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        plateNumberLabel = new JLabel("Enter Plate Number:");
        plateNumberTextField = new JTextField(10);
        calculatePaymentButton = new JButton("Calculate Fee");
        calculatePaymentButton.setBackground(primaryColor);
        calculatePaymentButton.setForeground(secondaryColor);

        paymentInputPanel.add(plateNumberLabel);
        paymentInputPanel.add(plateNumberTextField);
        paymentInputPanel.add(calculatePaymentButton);

        paymentDetailsTextArea = new JTextArea(5, 20);
        paymentDetailsTextArea.setEditable(false);
        paymentDetailsScrollPane = new JScrollPane(paymentDetailsTextArea);

        payButton = new JButton("Pay Now");
        payButton.setEnabled(false); // Initially disabled
        payButton.setBackground(primaryColor);
        payButton.setForeground(secondaryColor);

        backToMenuFromPaymentButton = new JButton("Back to Menu");
        backToMenuFromPaymentButton.setBackground(primaryColor);
        backToMenuFromPaymentButton.setForeground(secondaryColor);
        backToMenuFromPaymentButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(payButton);
        buttonPanel.add(backToMenuFromPaymentButton);

        paymentPanel.add(paymentInputPanel, BorderLayout.NORTH);
        paymentPanel.add(paymentDetailsScrollPane, BorderLayout.CENTER);
        paymentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ActionListener for Calculate Fee Button
        calculatePaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String plateToPay = plateNumberTextField.getText().trim().toUpperCase();
                if (!plateToPay.isEmpty() && allParkingData != null) {
                    LocalDateTime entryTime = null;
                    for (int i = 1; i < allParkingData.size(); i++) {
                        String[] parkingRecord = allParkingData.get(i);
                        if (parkingRecord.length >= 3 && parkingRecord[1].equalsIgnoreCase(plateToPay) && parkingRecord[2].equalsIgnoreCase("in")) {
                            try {
                                entryTime = LocalDateTime.parse(parkingRecord[0], calculate.FORMATTER);
                            } catch (Exception ex) {
                                System.err.println("Error parsing entry time: " + ex.getMessage());
                            }
                            break;
                        }
                    }

                    if (entryTime != null) {
                        double fee = paymentHandler.calculateParkingFee(entryTime);
                        paymentDetailsTextArea.setText(
                                "Plate Number: " + plateToPay + "\nEstimated Fee: RM " + String.format("%.2f", fee));
                        payButton.setEnabled(true);
                    } else {
                        paymentDetailsTextArea.setText("No active 'IN' record found for plate: " + plateToPay);
                        payButton.setEnabled(false);
                    }
                } else {
                    paymentDetailsTextArea.setText("Please enter a plate number.");
                    payButton.setEnabled(false);
                }
            }
        });

        // ActionListener for Pay Now Button
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pay Now button clicked!"); // Debug 1
                String platePaid = plateNumberTextField.getText().trim().toUpperCase();
                String paymentDetails = paymentDetailsTextArea.getText();
                double amount = 0.0;
                if (paymentDetails.contains("Estimated Fee: RM ")) {
                    String feeStr = paymentDetails.substring(paymentDetails.indexOf("RM ") + 3);
                    try {
                        amount = Double.parseDouble(feeStr);
                        System.out.println("Amount to pay: RM " + amount); // Debug 2
                        paymentHandler.recordPayment(platePaid, amount);
                        paymentDetailsTextArea.append("\nPayment recorded successfully.");
                        payButton.setEnabled(false);
                        System.out.println("Payment process completed in ActionListener."); // Debug 3

                        // Show the payment confirmation image popup
                        paymentHandler.showPaymentConfirmationImage();

                        // Refresh the Results panel immediately after payment
                        ToolsforCSV csvReaderlol = new ToolsforCSV();
                        calculate calculatefunc = new calculate();
                        allParkingData = csvReaderlol.readAllDataAtOnce("local_db/db.csv");
                        List<String[]> calculationResults = calculatefunc.calculateParkingFees(allParkingData);
                        populateResultsPanels(calculationResults);

                        // Optionally, you could switch back to the Results panel here:
                        // cardLayout.show(mainPanel, "Results");

                    } catch (NumberFormatException ex) {
                        paymentDetailsTextArea.append("\nError: Could not parse the fee.");
                        ex.printStackTrace(); // Print the error stack trace
                    }
                } else {
                    paymentDetailsTextArea.append("\nError: No fee calculated.");
                }
            }
        });
    }

    private void populateResultsPanels(List<String[]> results) {
        paidTableModel.setRowCount(0);
        paidTableModel.setColumnCount(0);
        ongoingTableModel.setRowCount(0);
        ongoingTableModel.setColumnCount(0);
        warningsTextArea.setText("");

        if (!results.isEmpty()) {
            paidTableModel.setColumnIdentifiers(new Object[]{"Plate Number", "Fee (RM)", "Payment Status"});
            ongoingTableModel.setColumnIdentifiers(new Object[]{"Plate Number", "Estimated Fee (RM)"});

            for (int i = 0; i < results.size(); i++) {
                String[] row = results.get(i);

                if (row.length == 3) {
                    if (!row[0].equalsIgnoreCase("Warning")) {
                        paidTableModel.addRow(new Object[]{row[0], row[1], row[2]});
                    } else {
                        warningsTextArea.append(row[1] + "\n");
                    }
                } else if (row.length == 4) {
                    if (!row[0].equalsIgnoreCase("Warning")) {
                        paidTableModel.addRow(new Object[]{row[0], row[1], row[3]});
                    } else if (row[1].startsWith("Still parked:")) {
                        String plate = row[1].substring("Still parked: ".length()).trim();
                        String fee = row[2].replace("(estimated)", "").trim();
                        ongoingTableModel.addRow(new Object[]{plate, fee});
                    } else {
                        warningsTextArea.append(row[1] + "\n");
                    }
                } else if (row.length > 1 && row[0].equalsIgnoreCase("Warning")) {
                    warningsTextArea.append(row[1] + "\n");
                }
            }
        }
        paidTableModel.fireTableDataChanged();
        ongoingTableModel.fireTableDataChanged();
    }

    public static void main(String[] args) {
        // Use invokeLater to ensure GUI updates are done on the EDT
        SwingUtilities.invokeLater(() -> {
            // Create and show the welcome screen
            WelcomeScreen welcomeScreen = new WelcomeScreen();
            welcomeScreen.setVisible(true);
        });
    }

    // Inner class for the welcome screen
    static class WelcomeScreen extends JFrame {
        public WelcomeScreen() {
            setTitle("Smart Parking System");
            setSize(800, 600);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new GradientPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JLabel title = new JLabel("WELCOME TO", SwingConstants.CENTER);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            title.setFont(new Font("Arial", Font.BOLD, 28));
            title.setForeground(Color.WHITE);

            JLabel subtitle = new JLabel("SMART PARKING MANAGEMENT", SwingConstants.CENTER);
            subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            subtitle.setFont(new Font("Arial", Font.BOLD, 30));
            subtitle.setForeground(new Color(255, 215, 0));

            JButton startButton = new JButton("START");
            startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startButton.setFont(new Font("Arial", Font.BOLD, 18));
            startButton.setBackground(Color.BLACK);
            startButton.setForeground(Color.WHITE);
            startButton.setFocusPainted(false);
            startButton.setMaximumSize(new Dimension(150, 50)); // Center Button
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Close the welcome screen when the start button is clicked.
                    dispose();
                    // Create and show the main application window
                    MainMenu mainMenu = new MainMenu(); // Create MainMenu instance
                    mainMenu.setVisible(true); // Show the main menu
                }
            });
            // buttonPanel settings
            panel.add(Box.createVerticalStrut(100));
            panel.add(title);
            panel.add(Box.createVerticalStrut(20));
            panel.add(subtitle);
            panel.add(Box.createVerticalStrut(60));
            panel.add(startButton);

            add(panel);
            setVisible(true);
        }

        class GradientPanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(220, 53, 69), 0, getHeight(), new Color(139, 0, 0));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}

