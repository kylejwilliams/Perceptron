/**
 * Created by Kyle on 5/5/2016.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Perceptron {
    ArrayList<Node> inputLayer = new ArrayList<>();
    Node outputLayer;

    int maxIterations = 0;
    Data trainingData;
    Data testingData;

    public Perceptron(int maxIterations, Data trainingData) {
        this.maxIterations = maxIterations;
        this.trainingData = trainingData;
        Random r = new Random();

        // init the layers of the network
        for (int i = 0; i < trainingData.data.get(0).size() - 1; i++) {
            double tmpWeight = 2 * r.nextDouble() - 1; // returns val between [-1, 1]
            inputLayer.add(new Node(-1, tmpWeight));
        }

        double tmpWeight = 2 * r.nextDouble() - 1;
        outputLayer = new Node(0, tmpWeight);

    }

    // threshold function
    int threshold(double s) {
        if ( s > 0) return 1;
        else return -1;
    }

    public void train() {
        int iter = 0;
        double errorRate;
        double sum = 0.0;
        int actualClassification = 0;
        int guessedClassification;
        double totalError = 0.0;
        int errorCount;

       do {
           errorCount = 0;
           iter++;

           for (int i = 0; i < trainingData.data.size(); i++) {
               HashMap<Integer, Integer> curDatum = trainingData.data.get(i);
               for (int j = 0; j < curDatum.size(); j++) {
                   if (j == 0) actualClassification = curDatum.get(j);
                   else inputLayer.get(j - 1).val = curDatum.get(j);
               }

               for (Node n : inputLayer) {
                   sum += n.val * n.weight;
               }

               guessedClassification = threshold(sum * outputLayer.weight);
               errorRate = (actualClassification - guessedClassification);
               if (errorRate != 0) errorCount++;

               outputLayer.weight = normalizeWeight(outputLayer.weight + learningRate() * errorRate * guessedClassification);
               for (Node n : inputLayer) {
                   n.weight = normalizeWeight(n.weight + learningRate() * errorRate * n.val);
               }

               sum = 0.0;
           }
           actualClassification = 0;
           System.out.println(errorCount);
       } while (iter < maxIterations && errorCount != 0);

    }

    public void test(Data testingData) {
        int actualClassification = 0;
        int guessedClassification = 0;
        double sum = 0.0;
        int numErrors = 0;

        this.testingData = testingData;

        for (int i = 0; i < trainingData.data.size(); i++) {
            HashMap<Integer, Integer> curDatum = testingData.data.get(i);
            for (int j = 0; j < curDatum.size(); j++) {
                if (j == 0) actualClassification = curDatum.get(j);
                else inputLayer.get(j - 1).val = curDatum.get(j);
            }

            for (Node n : inputLayer)
                sum += n.val * n.weight;

            guessedClassification = threshold(sum * outputLayer.weight);
            if (guessedClassification == actualClassification) {
                System.out.println("iter " + i + " : SUCCESS!");
            }
            else {
                System.out.println("iter " + i + " : FAILURE");
                numErrors++;
            }
            sum = 0.0;
        }
        System.out.println();
        System.out.println("num errors: " + numErrors + " | total data count: " + trainingData.data.size());
        System.out.println("Success rate: " + (double)numErrors / trainingData.data.size());
    }

    public double normalizeWeight(double val) {
        return Math.tanh(val);
    }
    public double learningRate() {
        return 0.1;
    }
}
