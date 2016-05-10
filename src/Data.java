import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Data Class
 *
 * This class represents a table of data used throughout the program. Data is created from a space-separated file
 * where each column represents a different attribute and the last column within the file is the classifier.
 */
class Data {
    List<HashMap<Integer, Integer>> data = new ArrayList<>();

    /**
     * Creates a new data object
     * @param fh
     *          a file handler used to get the data from a file
     */
    Data(FileHandler fh) {
        String[] fileData = fh.linesInFile;

        // +1 in order to also get classification
        int numDecisionVariables = Integer.valueOf(fh.getWordAt(fileData[0], 0)) + 1;

        // -1 in order to NOT get first line of fileData
        int totalData = Integer.valueOf(fh.getWordAt(fileData[0], 1)) - 1;

        for (int i = 1; i <= totalData; i++) {
            HashMap<Integer, Integer> tmpDataLine = new HashMap<>();
            String curLine = fileData[i];

            for (int j = 0; j < numDecisionVariables; j++) {
                int dataValue;
                dataValue = Integer.valueOf(fh.getWordAt(curLine, j));

                if (j == numDecisionVariables - 1)
                    tmpDataLine.put(0, dataValue);
                else
                    tmpDataLine.put(j + 1, dataValue);  // j+1 so that we don't accidentally put it in index 0
            }

            data.add(tmpDataLine);
        }
    }
}
