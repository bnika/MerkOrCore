package is.merkor.core.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A data container for the mapping of relation type names to the corresponding id
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */
public class RelationTypeMap {

	private static final String relTypesFilename = "merkor_relationTypes.csv";
	public static final Map<String, Long> NAME_ID_MAP;
	
	static {
		NAME_ID_MAP = new HashMap<String, Long>();
		List<String> relationTypes = FileCommunicatorReading.readListFromFile(relTypesFilename);
		relationTypes.remove(0);
		String[] lineArr;
		for (String line : relationTypes) {
			line = line.replaceAll("\"", "");
			lineArr = line.split("\t");
			Long id = parseId(lineArr[0]);
			if (null != id) {
				NAME_ID_MAP.put(lineArr[1], id);
				if (NAME_ID_MAP.get(lineArr[2]) != null) {
					if (lineArr[2].equals("og"))
						NAME_ID_MAP.put("og_adj", id);
					else if (lineArr[2].equals("er eiginleiki"))
						NAME_ID_MAP.put("lýsir", id);
				}
				else
					NAME_ID_MAP.put(lineArr[2], id);
				// already changed the description of attributeOf, so do not add "er_eiginleiki" for id==5
				if (lineArr[2].contains(" ") && id != 5L) {
					String underscoreDescr = lineArr[2].replaceAll(" ", "_");
					NAME_ID_MAP.put(underscoreDescr, id);
				}
			}
		}
	}

	private static Long parseId(String string) {
		try {
			return Long.parseLong(string);
		} catch (NumberFormatException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	/*
	 * Write out the current map
	 */
	public static void main (String[] args) {
		try {
			BufferedWriter out = FileCommunicatorWriting.createWriter("relationTypesMap_MerkorCore.csv", false);
			
			for (String s : NAME_ID_MAP.keySet()) {
				out.write(s);
				out.write("\t");
				out.write(NAME_ID_MAP.get(s).toString());
				out.write("\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
