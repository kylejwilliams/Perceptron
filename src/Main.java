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
        int maxIter = 0;

        // get file from user
        System.out.println("Enter a filename or path");
        System.out.print("> ");
        filename = sc.next();
        System.out.println("Enter a max number of iterations:");
        System.out.print("> ");
        maxIter = sc.nextInt();

        Data trainingData = new Data(new FileHandler(filename));

        Perceptron perceptron = new Perceptron(maxIter, trainingData);
        perceptron.train();

        System.out.println("Enter a filename for a test dataset");
        System.out.println("> ");
        filename = sc.next();

        Data testData = new Data(new FileHandler(filename));
        perceptron.test(testData);

        sc.close();
    }

}
