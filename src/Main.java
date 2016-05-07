import java.io.IOException;
import java.util.Scanner;

/**
 * Created by kjwkc3 on 5/4/2016.
 *  // path to file: E:\workspace\Perceptron\Resources\apascal_train.txt
 */

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String filename;

        // get file from user
        System.out.println("Enter a filename or path");
        System.out.print("> ");
        filename = sc.next();

        sc.close();

        Data data = new Data(new FileHandler(filename));
        data.displayData();

        // turn file into data
        // pass data to perceptron
        // train the perceptron
        // test the perceptron
        // take in more data and classify it
    }

}
