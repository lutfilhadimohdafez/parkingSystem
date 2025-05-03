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


        mainPanel.add(menuPanel, "Menu");
        //mainPanel.add(parkingPanel, "Parking");
        //mainPanel.add(csvFilePanel, "CSVfile");

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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}