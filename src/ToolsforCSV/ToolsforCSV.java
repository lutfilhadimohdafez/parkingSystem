package ToolsforCSV;

import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;

public class ToolsforCSV {

    public void readCSVfile(String file) {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        try (FileReader filereader = new FileReader(file);
             CSVReader csvReader = new CSVReader(filereader)) {
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> readAllDataAtOnce(String file) {
        List<String[]> allData = new ArrayList<>();

        try (FileReader filereader = new FileReader(file);
             CSVReader csvReader = new CSVReaderBuilder(filereader)
                     .withSkipLines(1)
                     .build()) {
            allData = csvReader.readAll();

            // Print Data (you can keep this for now or remove it later)
            for (String[] row : allData) {
                for (String cell : row) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return allData;
    }

    // New method to read all data with headers
    public List<String[]> readAllDataWithHeaders(String file) {
        List<String[]> allData = new ArrayList<>();

        try (FileReader filereader = new FileReader(file);
             CSVReader csvReader = new CSVReader(filereader)) {
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                allData.add(nextRecord);
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return allData;
    }
}