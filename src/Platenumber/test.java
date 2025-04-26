package Platenumber;
import javax.swing.SwingUtilities;
public class test {

    public static void main(String[] args) {


        // async call for swing
        SwingUtilities.invokeLater(() -> new plate());


    }
}

