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

    public plate() {
        setTitle("Parking Entry/Exit");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        plateNumberField = new JTextField(10);
        inButton = new JButton("IN");
        outButton = new JButton("OUT");

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

        add(new JLabel("Plate Number:"));
        add(plateNumberField);
        add(inButton);
        add(outButton);

        setVisible(true);
    }

    private void recordParkingEvent(String plateNumber, String status) {
        if (plateNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a plate number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Kuala_Lumpur")); // Be explicit with time zone
        String timestamp = now.format(FORMATTER);
        String paymentStatus = ""; // Initialize payment status as empty for "in" events

        if (status.equalsIgnoreCase("out")) {
            // For simplicity in this example, we'll just set it to "PENDING" on exit.
            // In a real scenario, you would likely check the payments.csv or have a more sophisticated way to determine the payment status.
            paymentStatus = "PENDING";
        }

        String dataToAppend = String.format("%s,%s,%s,%s\n", timestamp, plateNumber, status, paymentStatus);

        try (FileWriter fw = new FileWriter(CSV_FILE_PATH, true)) {
            fw.write(dataToAppend);
            JOptionPane.showMessageDialog(this, "Record added to db.csv: " + timestamp + ", " + plateNumber + ", " + status + (status.equalsIgnoreCase("out") ? ", " + paymentStatus : ""), "Success", JOptionPane.INFORMATION_MESSAGE);
            plateNumberField.setText(""); // Clear the field after recording
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to db.csv file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}