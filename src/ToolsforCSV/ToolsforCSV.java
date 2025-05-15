// ToolsforCSV.java
package ToolsforCSV;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class ToolsforCSV {

    public List<String[]> readAllDataAtOnce(String file) {
        List<String[]> allData = null;
        try (FileReader filereader = new FileReader(file);
             CSVReader csvReader = new CSVReader(filereader)) {
            allData = csvReader.readAll();
        } catch (IOException | CsvException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return allData;
    }

    public void writeAllDataAtOnce(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(data);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public void updatePaymentStatusInCSV(String filePath, String plateNumber, String exitTimestamp, String newStatus) {
        List<String[]> allRows = readAllDataAtOnce(filePath);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (String[] row : allRows) {
            if (row.length >= 4 &&
                row[1].trim().equalsIgnoreCase(plateNumber.trim()) &&
                row[0].trim().equalsIgnoreCase(exitTimestamp.trim()) &&
                row[2].trim().equalsIgnoreCase("out")) {
                row[3] = newStatus;
                break; // Assuming we only update the first matching 'out' record
            }
        }
        writeAllDataAtOnce(filePath, allRows);
    }
}