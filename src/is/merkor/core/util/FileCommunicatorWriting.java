package is.merkor.core.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class FileCommunicatorWriting {
	
	/**
	 * Appends a line to a file
	 * @param filename the name of a file to which the line should be appended to
	 * @param line the line to be written
	 */
	public static void writeLineAppend (String filename, String line) {
		try {
			BufferedWriter out = createWriter(filename, true);
			out.write(line);
			out.write("\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Writes a List of Strings to a file, overrides if file exists (non append)
	 * @param filename file to write the list into
	 * @param list the list containing the strings to write
	 */
	public static void writeListNonAppend (String filename, List<String> list) {
		try {
			BufferedWriter out = createWriter(filename, false);
			for(String s : list) {
				out.write(s);
				out.write("\n");
			}
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void writeListAppend(List<String> list, String filename) {
		try {
			BufferedWriter out = createWriter(filename, true);
			for(String s : list) {
				out.write(s);
				out.write("\n");
			}
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	public static BufferedWriter createWriter (String filename, boolean append) throws IOException {
		 return (new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, append), "UTF8")));
	}
	public static void writeBufferToFile(String text, String filename) {
		try {
			BufferedWriter out = createWriter(filename, false);
			out.write(text);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
