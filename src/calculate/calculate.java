package calculate;

import ToolsforCSV.ToolsforCSV;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class calculate extends ToolsforCSV {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final double HOURLY_RATE = 2.0; // RM 2.00 per hour
    private static final String PAYMENT_FILE_PATH = "local_db/payments.csv";

    public List<String[]> calculateParkingFees(List<String[]> dbcsvList) {
        List<String[]> results = new ArrayList<>();
        Map<String, LocalDateTime> entryTimes = new HashMap<>();
        Map<String, LocalDateTime> exitTimes = new HashMap<>();
        Map<String, String> paymentStatuses = new HashMap<>();
        List<String[]> paymentRecords = readPaymentData(PAYMENT_FILE_PATH);

        // Add header for the results
        //results.add(new String[]{"Plate Number", "Fee (RM)", "Payment Status"});

        // First pass: Collect entry and exit times
        for (int i = 1; i < dbcsvList.size(); i++) {
            if (dbcsvList.get(i).length >= 3) {
                String timeStr = dbcsvList.get(i)[0];
                String plateNumber = dbcsvList.get(i)[1];
                String status = dbcsvList.get(i)[2];
                LocalDateTime timestamp = LocalDateTime.parse(timeStr, FORMATTER);

                if (status.equalsIgnoreCase("in")) {
                    entryTimes.put(plateNumber, timestamp);
                } else if (status.equalsIgnoreCase("out")) {
                    exitTimes.put(plateNumber, timestamp);
                }
            }
        }

        // Second pass: Calculate fees and determine payment status for exited vehicles
        for (Map.Entry<String, LocalDateTime> exitEntry : exitTimes.entrySet()) {
            String plate = exitEntry.getKey();
            LocalDateTime exitTime = exitEntry.getValue();
            if (entryTimes.containsKey(plate)) {
                LocalDateTime entryTime = entryTimes.get(plate);
                double fee = calculateParkingFee(entryTime, exitTime);
                String paymentStatus = isPaymentFound(plate, exitTime, paymentRecords) ? "PAID" : "NOT PAID";
                results.add(new String[]{plate, String.format("%.2f", fee), paymentStatus});
                entryTimes.remove(plate); // Remove processed entry
            } else {
                 results.add(new String[]{"Warning", "Exit without entry for: " + plate, "N/A", "NOT PAID"});
            }
        }

        // Handle ongoing parking
        for (Map.Entry<String, LocalDateTime> entry : entryTimes.entrySet()) {
            String plate = entry.getKey();
            LocalDateTime entryTime = entry.getValue();
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"));
            double fee = calculateParkingFee(entryTime, now);
            results.add(new String[]{"Warning", "Still parked: " + plate, String.format("%.2f", fee) + " (estimated)", "PENDING"});
        }

        return results;
    }

    // Helper method to read payment data from payments.csv
    private List<String[]> readPaymentData(String filePath) {
        List<String[]> payments = new ArrayList<>();
        try (FileReader filereader = new FileReader(filePath);
             CSVReader csvReader = new CSVReader(filereader)) {
            List<String[]> allRows = csvReader.readAll();
            // Skip header if it exists
            if (!allRows.isEmpty() && allRows.get(0).length == 3 && allRows.get(0)[0].equalsIgnoreCase("timestamp")) {
                payments = allRows.subList(1, allRows.size());
            } else {
                payments = allRows;
            }
        } catch (IOException | CsvException e) {
            System.err.println("Error reading payment data: " + e.getMessage());
        }
        return payments;
    }

    // Helper method to check if a payment exists for a given plate number and time
    private boolean isPaymentFound(String plateNumber, LocalDateTime exitTime, List<String[]> paymentRecords) {
        DateTimeFormatter paymentFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Consider a time window for payment matching (e.g., payment occurred before or around exit time)
        long windowMinutes = 60; // Check for payments within the last hour before exit

        for (String[] record : paymentRecords) {
            if (record.length == 3 && record[1].trim().equalsIgnoreCase(plateNumber.trim())) {
                try {
                    LocalDateTime paymentTime = LocalDateTime.parse(record[0], paymentFormatter);
                    if (paymentTime.isBefore(exitTime) || paymentTime.isEqual(exitTime) || ChronoUnit.MINUTES.between(paymentTime, exitTime) <= windowMinutes) {
                        return true;
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing payment timestamp: " + e.getMessage());
                }
            }
        }
        return false;
    }

    // Helper method to calculate the fee between two LocalDateTime objects
    private double calculateParkingFee(LocalDateTime entryTime, LocalDateTime exitTime) {
        long duration = ChronoUnit.MINUTES.between(entryTime, exitTime);
        double hours = Math.ceil((double) duration / 60);
        return hours * HOURLY_RATE;
    }
}