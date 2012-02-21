package is.merkor.core.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileCommunicatorReading {

	public static BufferedReader createReader (String filename) throws IOException {
		 return (new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8")));
	}
	
	/**
	 * Reads lines from {@code filename} and returns a List of the lines as Strings.
	 * Omits empty lines.
	 * 
	 * @param filename name of the file to read from
	 * @return the file content as List of Strings (lines)
	 */
	public static List<String> readListFromFile(String filename) {
		List<String> stringList = new ArrayList<String>();
		try {
			BufferedReader in = createReader(filename);
			String line = "";
			while ((line = in.readLine()) != null) {
				if (!line.isEmpty())
					stringList.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringList;
	}
	/**
	 * Reads words from {@code filename} and returns a List of the words as Strings.
	 * 
	 * @param filename name of the file to read from
	 * @return the file content as List of Strings (words)
	 */
	public static List<String> getWordsFromFileAsList(String filename) {
		List<String> stringList = new ArrayList<String>();
		try {
			BufferedReader in = createReader(filename);
			String line = "";
			String[] words;
			while ((line = in.readLine()) != null) {
				if (!line.isEmpty()) {
					words = line.split(" ");
					for (int i = 0; i < words.length; i++)
						stringList.add(words[i]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringList;
	}
}
