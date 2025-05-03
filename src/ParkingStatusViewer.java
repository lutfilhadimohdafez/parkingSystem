import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ParkingStatusViewer {

    public static void main(String[] args) {
        Map<String, Long> parkingData = new HashMap<>();
        parkingData.put("WXY1234", System.currentTimeMillis());
        parkingData.put("ABC5678", System.currentTimeMillis() - 3600000);
        showParkingStatus(parkingData);
    }

    public static void showParkingStatus(Map<String, Long> parkingData) {
        final int TOTAL_PARKING = 50;

        JFrame window = new JFrame("Parking Status");
        window.setSize(400, 400);
        window.setLayout(new BorderLayout());
        window.setLocationRelativeTo(null);

        int parkedCount = parkingData.size();
        int available = TOTAL_PARKING - parkedCount;

        JLabel statusLabel = new JLabel("Parking Available: " + available + " / " + TOTAL_PARKING, SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Guna JTextPane ganti JTextArea
        JTextPane vehicleList = new JTextPane();
        vehicleList.setEditable(false);

        // Guna StyledDocument untuk align center
        StyledDocument doc = vehicleList.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        try {
            doc.insertString(doc.getLength(), "Plate Number    Entry Time\n", null);
            doc.insertString(doc.getLength(), "----------------------------------\n", null);

            for (Map.Entry<String, Long> entry : parkingData.entrySet()) {
                String plateNumber = entry.getKey();
                long entryTime = entry.getValue();
                String formattedTime = new SimpleDateFormat("yyyy-MM-dd    HH:mm:ss").format(new Date(entryTime));
                doc.insertString(doc.getLength(), plateNumber + "    " + formattedTime + "\n", null);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(vehicleList);

        JButton back = new JButton("Back");
        back.setPreferredSize(new Dimension(100, 30));
        back.addActionListener(e -> window.dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(back);

        window.add(statusLabel, BorderLayout.NORTH);
        window.add(scrollPane, BorderLayout.CENTER);
        window.add(bottomPanel, BorderLayout.SOUTH);
        window.setVisible(true);
    }
}


     