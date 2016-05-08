import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kyle on 5/5/2016.
 */
public class Data {
    // FileHandler
    // lines of data
    FileHandler fh = null;
    String[] fileData = {};
    List<HashMap<Integer, Integer>> data = new ArrayList<>();
    List<Node> layer = new ArrayList<>();
    int numDecisionVariables = 0;
    int totalData = 0;

    public Data(FileHandler fh) {
        this.fh = fh;
        fileData = fh.linesInFile;

        // fileData[0] := first line in file
        // 0/1 := first / seconds words in line
        // +1 in order to also get classification
        // -1 in order to NOT get first line of fileData
        numDecisionVariables = Integer.valueOf(fh.getWordAt(fileData[0], 0)) + 1;
        totalData = Integer.valueOf(fh.getWordAt(fileData[0], 1)) - 1;

        createData();
    }

    public void createData() {
        for (int i = 1; i <= totalData; i++) {
            HashMap<Integer, Integer> tmpDataLine = new HashMap<>();
            String curLine = fileData[i];

            for (int j = 0; j < numDecisionVariables; j++) {
                int dataValue = 0;
                dataValue = Integer.valueOf(fh.getWordAt(curLine, j));

                if (j == numDecisionVariables - 1)
                    tmpDataLine.put(0, dataValue);
                else
                    tmpDataLine.put(j + 1, dataValue);  // j+1 so that we don't accidentally put it in index 0
            }

            data.add(tmpDataLine);
        }
    }

    public void displayData() {
        System.out.println("Num elements: " + data.size());

        for (HashMap<Integer, Integer> h : data) {
            System.out.println(h);
        }
    }
}
