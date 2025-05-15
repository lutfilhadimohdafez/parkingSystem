package Platenumber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class plate extends JFrame {

    private JTextField plateNumberField;
    private JButton inButton;
    private JButton outButton;
    private final String CSV_FILE_PATH = "local_db/db.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Color Theme
    private Color primaryColor = new Color(0xF79B72);       // #F79B72
    private Color secondaryColor = new Color(0x2A4759);     // #2A4759
    private Color lightGray = new Color(0xDDDDDD);          // #DDDDDD
    private Color lighterGray = new Color(0xEEEEEE);        // #EEEEEE

    public plate() {
        setTitle("Parking Entry/Exit");
        // Start the frame maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // Disable window resizing
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        plateNumberField = new JTextField(15);
        plateNumberField.setFont(new Font("Arial", Font.PLAIN, 16));

        inButton = new JButton("IN");
        inButton.setBackground(Color.GREEN); // Set IN button to green
        inButton.setForeground(Color.WHITE);
        inButton.setFont(new Font("Arial", Font.BOLD, 16));

        outButton = new JButton("OUT");
        outButton.setBackground(Color.RED); // Set OUT button to red
        outButton.setForeground(Color.WHITE); // Set OUT button text to white
        outButton.setFont(new Font("Arial", Font.BOLD, 16));

        inButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordParkingEvent(plateNumberField.getText().trim().toUpperCase(), "in");
            }
        });

        outButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordParkingEvent(plateNumberField.getText().trim().toUpperCase(), "out");
            }
        });

        JLabel plateLabel = new JLabel("Plate Number:");
        plateLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        // Get the preferred width of the plateLabel and plateNumberField
        int plateLabelWidth = plateLabel.getPreferredSize().width;
        int plateNumberFieldWidth = plateNumberField.getPreferredSize().width;
        // Find the maximum of the two widths
        int maxLabelAndFieldWidth = Math.max(plateLabelWidth, plateNumberFieldWidth);

        // Set the button width to match the maximum width
        inButton.setPreferredSize(new Dimension(maxLabelAndFieldWidth, 40));
        outButton.setPreferredSize(new Dimension(maxLabelAndFieldWidth, 40));

        // Add components using GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(plateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(plateNumberField, gbc);

        // Create a panel for the buttons to ensure they are side by side
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 10 pixel horizontal gap, 0 vertical gap

        // Add the buttons to the buttonPanel
        buttonPanel.add(inButton);
        buttonPanel.add(outButton);

        // Add the buttonPanel to the main frame
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(buttonPanel, gbc);

        setVisible(true);
    }

    private void recordParkingEvent(String plateNumber, String status) {
        if (plateNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a plate number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Kuala_Lumpur"));
        String timestamp = now.format(FORMATTER);
        String paymentStatus = ""; // Initialize paymentStatus

        if (status.equalsIgnoreCase("out")) {
            paymentStatus = "PENDING";
        }

        String dataToAppend = String.format("\"%s\",\"%s\",\"%s\",\"%s\"%n", timestamp, plateNumber, status, paymentStatus);

        try (FileWriter fw = new FileWriter(CSV_FILE_PATH, true)) {
            fw.write(dataToAppend);
            JOptionPane.showMessageDialog(this, "Record added to db.csv: " + timestamp + ", " + plateNumber + ", " + status + (status.equalsIgnoreCase("out") ? ", " + paymentStatus : ""), "Success", JOptionPane.INFORMATION_MESSAGE);
            plateNumberField.setText("");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to db.csv file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
