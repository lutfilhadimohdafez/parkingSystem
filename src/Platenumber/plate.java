package Platenumber;


import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.CSVWriter;


public class plate {
static CardLayout cl;
static JPanel CardSearch;
    public plate() {

        JFrame plate = new JFrame();
        plate.setResizable(false);
        JLayeredPane plateLayer = new JLayeredPane();
        plateLayer.setPreferredSize(new Dimension(500, 300));
        plate.setTitle("Plate Number");
        plate.setLocationRelativeTo(null);
        plate.setSize(500, 500);
        plate.setVisible(true);
        plate.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        plate.setLocationRelativeTo(null);

        JLabel PlateLabel = new JLabel("Enter Your Plate Number:");
        PlateLabel.setBounds(50, 50, 150, 30);

        JLabel DateLabel = new JLabel("Enter date (YYYY/MM/DD):");
        DateLabel.setBounds(50, 100, 150, 30);

        JTextField textPlate = new JTextField();
        textPlate.setBounds(200, 50, 200, 30);

        JTextField textDate = new JTextField();
        textDate.setBounds(200, 100, 200, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(200, 150, 100, 30);
        submitButton.addActionListener(e -> {
            String PlateID = textPlate.getText();
            String DateID = textDate.getText();
            if (PlateID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Plate number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int result = JOptionPane.showConfirmDialog(null, "Do you confirm your plate is " + PlateID, "Confirmation", JOptionPane.YES_NO_OPTION);
            switch (result) {
                case JOptionPane.NO_OPTION:
                    return;
                case JOptionPane.YES_OPTION:
                    String EntryStatus = "IN";
                    String csvFile = "src/Database/plate.csv";
                    try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile, true), 
                            CSVWriter.DEFAULT_SEPARATOR, 
                            CSVWriter.NO_QUOTE_CHARACTER, 
                            CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                            CSVWriter.DEFAULT_LINE_END)) {
                        String[] data = {PlateID, DateID, EntryStatus};
                        writer.writeNext(data);
                        System.out.println("Data saved to " + csvFile);

                        

                    } catch (IOException PlateException) {
                        System.out.println("An error occurred while writing to the file.");
                    }

                    
                    break;
                default:
                    break;
            }
           
            
        });

        JButton back = new JButton("Back");
        back.setBounds(200, 200, 100, 30);
        back.addActionListener(e -> {
            plate.dispose();
        });

        JButton search = new JButton("test");
        search.setBounds(200,250,100,30);
        search.addActionListener(e -> {
        

        });

        JButton searchPlateButton = new JButton("Search Plate");
        searchPlateButton.setBounds(200, 300, 150, 30);
        CardLayout searchcard = new CardLayout();
        JPanel callcsvpanel = new JPanel(searchcard);

        searchPlateButton.addActionListener(e -> {
            
            JFrame callcsvframe = new JFrame();
            callcsvframe.setSize(400, 300);

            JPanel card1 = new JPanel();
            JLabel card1label = new JLabel("Enter Your Plate Number");
            JTextField card1text = new JTextField();
            card1text.setBounds(50,20,100,30);
            card1.add(card1text);

            card1.setLayout(null);
            card1.add(card1text);

            JButton button1 = new JButton("Next");
            button1.setBounds(10,100,150,40);
            card1label.setBounds(100,100,150,40);
            card1.add(button1);

            JPanel card2 = new JPanel();
            card2.setLayout(null); // Set layout to null for absolute positioning
            card2.add(new JLabel("this is card 2"));
            card2.setBounds(50,150,100,100);

            JTextField cardtext2 = new JTextField();
            cardtext2.setBounds(200, 50, 300, 30); // Adjusted position for visibility
            card2.add(cardtext2);

            JButton button2 = new JButton("Proceed");
            button2.setBounds(10, 100, 150, 30); // Adjusted position for visibility

            card2.add(button2);

            callcsvpanel.add(card1, "card1");
            callcsvpanel.add(card2, "card2");

            button1.addActionListener(e1 -> {
                searchcard.show(callcsvpanel, "card2");
                String searchPlate = card1text.getText();
                String csvFile = "src/Database/plate.csv";
                boolean found = false;

                try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(csvFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(",");
                        if (values.length > 0 && values[0].equals(searchPlate)) {
                            cardtext2.setText("Plate Found: " + values[0] + ", Date: " + values[1]);
                            found = true;
                            break;
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error reading the file.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                if (!found) {
                    cardtext2.setText("Plate not found.");
                }
            });

            button2.addActionListener(e1 -> 
                callcsvframe.dispose()
            );

            callcsvframe.setVisible(true);
            callcsvframe.add(callcsvpanel);
        });

        plateLayer.add(searchPlateButton);

        plateLayer.add(PlateLabel);
        plateLayer.add(DateLabel);
        plateLayer.add(textDate);
        plateLayer.add(textPlate);
        plateLayer.add(submitButton);
        plateLayer.add(back);
        plate.add(plateLayer);
        
    }
}
    // TODO: #5 buat when typed in no plate, dia display output and proceed to pay
