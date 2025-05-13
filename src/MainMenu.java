import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import payment.PaymentHandler; // Correct import for PaymentHandler
import ToolsforCSV.ToolsforCSV; // Correct import for ToolsforCSV
import calculate.calculate; // Correct import for calculate
import Platenumber.plate; // Correct import for plate

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

    public MainMenu() {
        setTitle("Parking Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        paymentHandler = new PaymentHandler(); // Initialize PaymentHandler

        createMenuPanel();
        createResultsPanel();
        createPaymentPanel(); // Create the payment panel

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(resultsPanel, "Results");
        mainPanel.add(paymentPanel, "Payment"); // Add the payment panel

        add(mainPanel);
        cardLayout.show(mainPanel, "Menu");

        setVisible(true);
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(5, 1, 10, 10)); // Added space for the Payment button

        JButton startBtn = new JButton("Start Parking System");
        JButton csvButton = new JButton("Test CSV file");
        JButton calculateBtn = new JButton("View Parking Summary"); // Renamed
        JButton paymentBtn = new JButton("Initiate Payment"); // New Payment Button
        JButton exitBtn = new JButton("Exit");

        final String filepathCSV = "local_db/db.csv";
        ToolsforCSV csvReaderlol = new ToolsforCSV();
        calculate calculatefunc = new calculate();

        startBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new Platenumber.plate()));
        csvButton.addActionListener(e -> csvReaderlol.readAllDataAtOnce(filepathCSV));

        calculateBtn.addActionListener(e -> {
            allParkingData = csvReaderlol.readAllDataAtOnce(filepathCSV);
            List<String[]> calculationResults = calculatefunc.calculateParkingFees(allParkingData);
            populateResultsPanels(calculationResults);
            cardLayout.show(mainPanel, "Results");
        });

        paymentBtn.addActionListener(e -> cardLayout.show(mainPanel, "Payment")); // Show Payment Panel

        exitBtn.addActionListener(e -> System.exit(0));

        menuPanel.add(startBtn);
        menuPanel.add(csvButton);
        menuPanel.add(calculateBtn);
        menuPanel.add(paymentBtn);
        menuPanel.add(exitBtn);
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

        paymentInputPanel.add(plateNumberLabel);
        paymentInputPanel.add(plateNumberTextField);
        paymentInputPanel.add(calculatePaymentButton);

        paymentDetailsTextArea = new JTextArea(5, 20);
        paymentDetailsTextArea.setEditable(false);
        paymentDetailsScrollPane = new JScrollPane(paymentDetailsTextArea);

        payButton = new JButton("Pay Now");
        payButton.setEnabled(false); // Initially disabled

        backToMenuFromPaymentButton = new JButton("Back to Menu");
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
                        String[] parkingRecord = allParkingData.get(i); // Get the String array at index i
                        if (parkingRecord[1].equalsIgnoreCase(plateToPay) && parkingRecord[2].equalsIgnoreCase("in")) {
                            entryTime = LocalDateTime.parse(parkingRecord[0], calculate.FORMATTER);
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
        // ActionListener for Pay Now Button
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String platePaid = plateNumberTextField.getText().trim().toUpperCase();
                String paymentDetails = paymentDetailsTextArea.getText();
                double amount = 0.0;
                if (paymentDetails.contains("Estimated Fee: RM ")) {
                    String feeStr = paymentDetails.substring(paymentDetails.indexOf("RM ") + 3);
                    try {
                        amount = Double.parseDouble(feeStr);
                        paymentHandler.recordPayment(platePaid, amount);
                        paymentDetailsTextArea.append("\nPayment recorded successfully.");
                        payButton.setEnabled(false);

                        // Refresh the Results panel immediately after payment
                        ToolsforCSV csvReaderlol = new ToolsforCSV();
                        calculate calculatefunc = new calculate();
                        allParkingData = csvReaderlol.readAllDataAtOnce("local_db/db.csv");
                        System.out.println("db.csv read after payment. Number of records: " + allParkingData.size()); // Debug
                                                                                                                      // print
                        List<String[]> calculationResults = calculatefunc.calculateParkingFees(allParkingData);
                        System.out.println("calculateParkingFees called after payment. Number of results: "
                                + calculationResults.size()); // Debug print
                        populateResultsPanels(calculationResults);
                        System.out.println("populateResultsPanels called after payment"); // Debug print

                        paidTableModel.fireTableDataChanged();
                        ongoingTableModel.fireTableDataChanged();

                        // Optionally, you could switch back to the Results panel here:
                        // cardLayout.show(mainPanel, "Results");

                    } catch (NumberFormatException ex) {
                        paymentDetailsTextArea.append("\nError: Could not parse the fee.");
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
    
            for (int i = 0; i < results.size(); i++) { // Start from 0 to include potential header from calculate
                String[] row = results.get(i);
    
                if (row.length == 3) {
                    // Likely a row for paid exits from calculate
                    if (!row[0].equalsIgnoreCase("Warning")) {
                        paidTableModel.addRow(new Object[]{row[0], row[1], row[2]});
                    } else {
                        warningsTextArea.append(row[1] + "\n");
                    }
                } else if (row.length == 4) {
                    // Likely a row with payment status
                    if (!row[0].equalsIgnoreCase("Warning")) {
                        paidTableModel.addRow(new Object[]{row[0], row[1], row[3]}); // Payment status is at index 3 now
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
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}