package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CardLayoutExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("CardLayout Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Create the CardLayout and main panel
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        // Create individual cards
        JPanel card1 = new JPanel();
        card1.add(new JLabel("This is Card 1"));
        JButton nextButton1 = new JButton("Next");
        card1.add(nextButton1);

        JPanel card2 = new JPanel();
        card2.add(new JLabel("This is Card 2"));
        JButton backButton2 = new JButton("Back");
        card2.add(backButton2);

        // Add cards to the cardPanel
        cardPanel.add(card1, "Card1");
        cardPanel.add(card2, "Card2");

        // Button actions
        nextButton1.addActionListener(e -> cardLayout.show(cardPanel, "Card2"));
        backButton2.addActionListener(e -> cardLayout.show(cardPanel, "Card1"));

        // Add cardPanel to frame
        frame.add(cardPanel);
        frame.setVisible(true);
    }
}

