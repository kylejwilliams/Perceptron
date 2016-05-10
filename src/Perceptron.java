
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Node class
 *
 * This class is a container that holds a value of data and a weight that connects between an input node and the
 * output node
 */
class Node {
    int val = 0;
    double weight = 0.0;

    Node(int val, double weight) {
        this.val = val;
        this.weight = weight;
    }
}

/**
 * Perceptron class
 *
 * This class models our perceptron. The perceptron consists of an input layer of nodes and a single output node.
 * When reading in data, the data value is placed in a corresponding input node and that value is modified by the
 * accompanied weight. The perceptron sums over each of these values and passes them to a threshold function which
 * maps the inputted value to two values, -1 and 1, which correspond to the two classifications.
 *
 */
class Perceptron {
    private ArrayList<Node> inputLayer = new ArrayList<>();
    private Node outputLayer;
    private int maxIterations = 0;
    private Data trainingData;

    /**
     * This method creates a perceptron
     * @param maxIterations
     *                      maximum number of times to run through the perceptron when training. This value prevents
     *                      the perceptron from entering an infinite loop when given a non-linear separable dataset
     * @param trainingData
     *                      a data object containing the data used to train the perceptron
     */
    Perceptron(int maxIterations, Data trainingData) {
        this.maxIterations = maxIterations;
        this.trainingData = trainingData;
        Random r = new Random();

        // create the input neurons ..
        for (int i = 0; i < trainingData.data.get(0).size() - 1; i++) {
            double tmpWeight = 2 * r.nextDouble() - 1; // returns val between [-1, 1]
            inputLayer.add(new Node(-1, tmpWeight));
        }

        // .. and the output neuron
        double tmpWeight = 2 * r.nextDouble() - 1;
        outputLayer = new Node(0, tmpWeight);

    }

    /**
     * This method represents the threshold for our neuron to fire. it takes in the sum of all of the input neurons
     * and, will return the a classification of a given data object
     *
     * @param s
     *          the cumulative sum of the inputs
     * @return
     *          a classification of the data object, represented by +- 1
     */
    private int threshold(double s) {
        return (s > 0) ? 1 : -1;
    }

    /**
     * Returns a value based on the difference between our guessed classification and the actual classification.
     *
     * This
     * method was created because of weird errors since our classification range is [-1, 1] instead of a typical [0,
     * 1]. For instance, if our actual classification was 1 and our guessed classification was -1, then our error
     * would be +2. This is too large to use for fine-tuning our network, and so I've mapped the values from the [-2.
     * 2] range back to [-1, 1]
     *
     * @param actual
     *                  the actual classification of the data
     * @param guessed
     *                  our guessed classification of the data
     * @return
     *                  an error rate corresponding to how far off we are in our guess. If we guess too low, we
     *                  return a 1 so that our next guess will be higher. If we guess too high, we return a -1 to
     *                  take a smaller guess next time.
     */
    private double errorRate(int actual, int guessed) {
        if (actual == guessed) return 0;
        else if (actual > guessed) return 1; // our guess needs to be higher
        else return -1; // our guess needs to be lower
    }

    /**
     * This method goes about training our perceptron. Each iteration up to our maximum, we run through every data
     * point within our dataset and pass it through the network. Then we evaluate our guessed classification with the
     * actual classification and we adjust the weights accordingly. We stop training once we have either hit the
     * maximum number of iterations or our perceptron has converged to an optimal solution
     */
    void train() {
        int iter = 0;
        double sum = 0.0;
        int actualClassification = 0;
        int guessedClassification;
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

               // !!!! NOTE: For some reason, I cannot get this to work when the output layer's weight is in there.
               // By removing that line though, my perceptron works perfectly fine. Any idea what I did wrong?
               //guessedClassification = threshold(sum * outputLayer.weight);
               guessedClassification = threshold(sum);

               if (errorRate(actualClassification, guessedClassification) != 0) errorCount++;

               outputLayer.weight = normalizeWeight(outputLayer.weight + learningRate() * errorRate
                       (actualClassification, guessedClassification) * guessedClassification);
               for (Node n : inputLayer) {
                   n.weight = normalizeWeight(n.weight + learningRate() *
                           errorRate(actualClassification, guessedClassification) * n.val);
               }

               sum = 0.0;
           }
           actualClassification = 0;
           System.out.println(errorCount);
       } while (iter < maxIterations && errorCount != 0);

    }

    /**
     * This method goes about testing our perceptron after training. It essentially follows the same process as
     * training, except this time we do not alter the weights, and we instead print the accuracy of our network in
     * guessing the new data.
     * @param testingData
     *                      A new dataset for the perceptron to evaluate
     */
    void test(Data testingData) {
        int actualClassification = 0;
        int guessedClassification;
        double sum = 0.0;
        int numErrors = 0;

        for (int i = 0; i < trainingData.data.size(); i++) {
            HashMap<Integer, Integer> curDatum = testingData.data.get(i);
            for (int j = 0; j < curDatum.size(); j++) {
                if (j == 0) actualClassification = curDatum.get(j);
                else inputLayer.get(j - 1).val = curDatum.get(j);
            }

            for (Node n : inputLayer)
                sum += n.val * n.weight;

            // !!! See note above !!!
            //guessedClassification = threshold(sum * outputLayer.weight);
            guessedClassification = threshold(sum);
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
        System.out.println("Success rate: " + (1 - (double)numErrors / trainingData.data.size()));
    }

    /**
     * This method maps a value of [-inf, inf] to [-1, 1]
     *
     * I used this because my weights were becoming extremely unnatural (like a weight of 64.3 or something), so this
     * was just a safeguard to make sure that doesn't happen. Probably isn't needed in itself, but is needed because
     * of an error elsewhere
     *
     * @param val
     *              a given weight in the range [-inf, inf]
     * @return
     *              the weight mapped to the range [-1, 1]
     */
    private double normalizeWeight(double val) {
        return Math.tanh(val);
    }

    /**
     * This method just returns a constant small value to ensure that only small changes are made to the weights of
     * the network and to prevent us from oscillating around an optimal solution
     * @return
     */
    private double learningRate() {
        return 0.1;
    }
}
