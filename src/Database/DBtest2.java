package Database;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DBtest2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter your city: ");
        String city = scanner.nextLine();

        String csvFile = "output.csv";
        try (FileWriter writer = new FileWriter(csvFile, true)) {
            writer.append(name).append(",").append(String.valueOf(age)).append(",").append(city).append("\n");
            System.out.println("Data saved to " + csvFile);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
}


