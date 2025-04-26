package Platenumber;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;


public class plate {

    
    public plate() {
        
        JFrame plate = new JFrame();
        plate.setResizable(false);
        JLayeredPane plateLayer = new JLayeredPane();
        plateLayer.setPreferredSize(new Dimension(500, 300));
        plate.setTitle("Plate Number");
        plate.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        plate.setLocationRelativeTo(null);
        plate.setSize(500,500);
        plate.setVisible(true);


        JLabel PlateLabel = new JLabel("Enter Your Plate Number:");
        PlateLabel.setBounds(50, 50, 150, 30);

        JTextField textPlate = new JTextField();
        textPlate.setBounds(200, 50, 200, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(200, 100, 100, 30);
        submitButton.addActionListener(e -> {
            String PlateID = textPlate.getText();
            JOptionPane.showConfirmDialog(null, "Do you confirm your plate is " + PlateID, "Confirmation", JOptionPane.YES_NO_OPTION);
            
            String csvFile = "src/Database/plate.csv";
            try (FileWriter PlateWriter = new FileWriter(csvFile, true)) {
            PlateWriter.append(PlateID).append("\n");
            System.out.println("Data saved to " + csvFile);
            } catch (IOException PlateException) {
            System.out.println("An error occurred while writing to the file.");
            PlateException.printStackTrace();
            }
            
        });

        // jangan lupa code for main menu back button
        JButton back = new JButton("Back");
        back.setBounds(200, 150, 100, 30);

        

        plateLayer.add(PlateLabel);
        plateLayer.add(textPlate);
        plateLayer.add(submitButton);
        plateLayer.add(back);
        plate.add(plateLayer);
    


        
        
    }
}
