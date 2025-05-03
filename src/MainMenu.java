import javax.swing.*;

import Platenumber.plate;
import ToolsforCSV.ToolsforCSV;
import calculate.calculate;

import java.io.File;

import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {

    CardLayout cardLayout;
    JPanel mainPanel, menuPanel, parkingPanel, csvFilePanel;

    public MainMenu() {
        setTitle("Parking Management System");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createMenuPanel();
        createParkingPanel();
        createCSVFile();

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(parkingPanel, "Parking");
        mainPanel.add(csvFilePanel, "CSVfile");

        add(mainPanel);
        cardLayout.show(mainPanel, "Menu");

        setVisible(true);
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton startBtn = new JButton("Start Parking System");
        JButton csvButton = new JButton("Test CSV file");
        JButton calculateBtn = new JButton("Calculate your car plate");
        JButton exitBtn = new JButton("Exit");

        // filepath

        // absolute path on another file folder below
        // final String filepathCSV =
        // "C:\\Users\\USER\\Documents\\db_test_java\\db.csv";

        final String filepathCSV = "local_db\\db.csv";

        // for csv reader
        ToolsforCSV csvReaderlol = new ToolsforCSV();

        // calc class
        calculate calculatefunc = new calculate();

        startBtn.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new plate());
        });
        csvButton.addActionListener(e -> csvReaderlol.readCSVfile(filepathCSV));

        calculateBtn.addActionListener(e-> calculatefunc.calculatePlateNumber());



        exitBtn.addActionListener(e -> System.exit(0));

        menuPanel.add(startBtn);
        menuPanel.add(csvButton);
        menuPanel.add(calculateBtn);
        menuPanel.add(exitBtn);
    }

    private void createParkingPanel() {
        parkingPanel = new JPanel();
        parkingPanel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Parking System Panel", SwingConstants.CENTER);
        JTextField plateInput = new JTextField();
        JButton backBtn = new JButton("Back to Menu");

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        parkingPanel.add(label, BorderLayout.NORTH);
        parkingPanel.add(plateInput, BorderLayout.CENTER);
        parkingPanel.add(backBtn, BorderLayout.SOUTH);
    }

    private void createCSVFile() {
        csvFilePanel = new JPanel();
        csvFilePanel.setLayout(new BorderLayout());

        JLabel labelcsvfile = new JLabel("CSVFilePanel", SwingConstants.CENTER);
        JTextField csvFileInput = new JTextField();
        JButton backBtncsv = new JButton("Back to Menu");

        backBtncsv.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        csvFilePanel.add(labelcsvfile, BorderLayout.NORTH);
        csvFilePanel.add(csvFileInput, BorderLayout.CENTER);
        csvFilePanel.add(backBtncsv, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}