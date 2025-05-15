package payment;

import javax.swing.*;

import java.awt.Image;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class PaymentHandler {

    private static final String PAYMENT_FILE_PATH = "local_db/payments.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final double HOURLY_RATE = 2.0; // Should be consistent with calculate.java

    public double calculateParkingFee(LocalDateTime entryTime) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"));
        long duration = ChronoUnit.MINUTES.between(entryTime, now);
        double hours = Math.ceil((double) duration / 60);
        return hours * HOURLY_RATE;
    }

    public void recordPayment(String plateNumber, double amount) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"));
        String timestamp = now.format(FORMATTER);
        String dataToAppend = String.format("%s,%s,%.2f\n", timestamp, plateNumber, amount);

        try (FileWriter fw = new FileWriter(PAYMENT_FILE_PATH, true)) {
            fw.write(dataToAppend);
            System.out.println("Payment recorded: " + timestamp + ", " + plateNumber + ", RM " + String.format("%.2f", amount));
            // In a real application, you might want to provide feedback to the user here.
        } catch (IOException e) {
            System.err.println("Error writing payment to file: " + e.getMessage());
            // Handle the error appropriately in a real application.
        }
    }

    public void showPaymentConfirmationImage() {
        // Create a new JFrame with fixed size
        JFrame imageFrame = new JFrame("Payment Confirmation");
        imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int width = 400;
        int height = 300;
        imageFrame.setSize(width, height);
        imageFrame.setLocationRelativeTo(null);
    
        // Load the image
        ImageIcon originalIcon = new ImageIcon("resources/payment_confirm.png"); // Update with your image path
    
        // Scale the image to fit the window size
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
    
        // Add the label to the frame
        imageFrame.getContentPane().add(imageLabel);
    
        imageFrame.setVisible(true);
    }
    
}

