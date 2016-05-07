import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Handles file input
 * 
 * @author kjwkc3
 *
 */
public class FileHandler {

	String[] linesInFile = {};

	/**
	 * Constructor. Attempts to open a file given a filename. Returns
	 * IOException if filename is invalid
	 * 
	 * @param filename
	 *            name of the file in the directory to open
	 */
	public FileHandler(String filename) {
		try {
			linesInFile = openFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads lines in from the given file
	 * 
	 * @param path
	 *            name of the file to be opened
	 * @return an array of Strings representing each line within the file
	 * @throws IOException
	 */
	public String[] openFile(String path) throws IOException {

		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		List<String> lines = new ArrayList<>();

		String curString = "";
		while ((curString = br.readLine()) != null) {
			lines.add(curString);
		}

		linesInFile = lines.toArray(new String[0]);

		br.close();
		return linesInFile;
	}

	/**
	 * separates a given string into it's individual words
	 * 
	 * @param line
	 *            string to be split
	 * @return an array of individual words from the given line
	 */
	public String[] splitLine(String line) {
		int numWords = getNumWords(line);
		String[] words = new String[numWords];

		for (int i = 0; i < numWords; i++) {
			words[i] = getWordAt(line, i);
		}

		return words;
	}

	/**
	 * gets the word in a given position within the line
	 * 
	 * @param line
	 *            sentence in which the word belongs
	 * @param pos
	 *            location of the wanted word
	 * @return the word at the given position within the line
	 */
	public String getWordAt(String line, int pos) {
		String word = "";
		int curPos = -1; // to grab the first word if needed

		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == ' ') {
				curPos++;
				if (curPos == pos)
					return word;
				word = "";
			} else
				word += line.charAt(i);
		}

		curPos++; // to grab the last word in the line
		if (curPos == pos)
			return word;
		else
			return "No word found"; // generic error
	}

	/**
	 * counts the number of words within the given sentence
	 * 
	 * @param line
	 *            sentence in which to evaluate
	 * @return number of words within the sentence
	 */
	public int getNumWords(String line) {
		int numWords = 0;

		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == ' ') {
				numWords++;
			}
		}
		numWords++; // need to get the last word between the last space and end
					// of line

		return numWords;
	}

}
