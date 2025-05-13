package calculation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class SimpleParkingSystem extends JFrame {

    static HashMap<String, Long> plateEntryTime = new HashMap<>();
    static HashMap<String, String> plateLocation = new HashMap<>();

    public SimpleParkingSystem() {
        setTitle("Parking System");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton readPlateBtn = new JButton("Register Car");
        JButton statusBtn = new JButton("View Status");
        JButton paymentBtn = new JButton("Make Payment");

        add(readPlateBtn);
        add(statusBtn);
        add(paymentBtn);

        readPlateBtn.addActionListener(e -> registerCar());
        statusBtn.addActionListener(e -> viewStatus());
        paymentBtn.addActionListener(e -> makePayment());

        setVisible(true);
    }

    public static void registerCar() {
        String plate = JOptionPane.showInputDialog("Enter Plate Number:");
        String loc = JOptionPane.showInputDialog("Enter Parking Spot:");

        if (plate != null && loc != null && !plate.isEmpty() && !loc.isEmpty()) {
            plateEntryTime.put(plate, System.currentTimeMillis());
            plateLocation.put(plate, loc);
            JOptionPane.showMessageDialog(null, "Car " + plate + " parked at " + loc);
        } else {
            JOptionPane.showMessageDialog(null, "Missing input.");
        }
    }

    public static void viewStatus() {
        if (plateEntryTime.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No cars parked.");
        } else {
            StringBuilder msg = new StringBuilder("Cars Parked:\n");
            for (String plate : plateEntryTime.keySet()) {
                msg.append(plate).append(" at ").append(plateLocation.get(plate)).append("\n");
            }
            JOptionPane.showMessageDialog(null, msg.toString());
        }
    }

    public static void makePayment() {
        String plate = JOptionPane.showInputDialog("Enter your plate number:");

        if (plateEntryTime.containsKey(plate)) {
            long now = System.currentTimeMillis();
            long duration = (now - plateEntryTime.get(plate)) / 1000; // seconds
            double fee = Math.ceil(duration / 60.0) * 0.01; // RM0.01 per minute

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Parked at: " + plateLocation.get(plate) +
                    "\nDuration: " + duration + "s\nFee: RM" + fee +
                    "\nConfirm payment?");

            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Payment successful. Thank you!");
                plateEntryTime.remove(plate);
                plateLocation.remove(plate);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Plate not found.");
        }
    }
}
