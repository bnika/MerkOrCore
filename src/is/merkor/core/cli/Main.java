/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

/**
 * A command line interface to the MerkOr data stored in Redis.
 * 
 * Run java -jar MerkOrCore.jar -help to see options available.
 * 
 * More instructions in file README.markdown included in the MerkOrCore package.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */
public class Main {
	
	private static MerkorCommandLineQueries queries;
	// default Redis connection
	private static String redisHost = "localhost";
	private static int redisPort = 6379;
	
	// number of default relations to get if no number is entered by the user
	private static final String DEFAULT_NUMBER = "100";
	
	public static List<String> processCommandLine (final CommandLine cmdLine) {
		List<String> results = new ArrayList<String>();
		
		if (cmdLine.hasOption("help") || cmdLine.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar MerkOrCore.jar", MerkorCoreCommandLineOptions.options);
			results.add("no message");
		}
		// non default redis host / port?
		if (cmdLine.hasOption("host")) {
			redisHost = cmdLine.getOptionValue("host");
		}
		if (cmdLine.hasOption("port")) {
			try {
				redisPort = Integer.parseInt(cmdLine.getOptionValue("port"));
			} catch (NumberFormatException e) {
				results.add(e.getMessage());
				return results;
			}
		}
		// now Redis host and port are set or default is used
		try {
			queries = new MerkorCommandLineQueries(redisHost, redisPort);
		} catch (Exception e) {
			results.add("Couldn't connect to Redis. See logs/logging.log for info");
			return results;
		}
		
		// info on lexical items wanted? ///////////////////////	
		String value = cmdLine.getOptionValue("items");
		if (value != null) {
			if (value.contains("?") || value.contains("*"))
				return queries.getItemsForRegex(value);
			else
				return queries.getItemsForLemma(value);
		}
		
		// info on relations wanted? ///////////////////////////
		value = cmdLine.getOptionValue("relations");
		String number = cmdLine.getOptionValue("n");
		if (null != value && null != number)
			return queries.getMostRelated(value, number);
		else if (null != value) {
			return queries.getRelationsFor(value);
		}
		
		String fromLemma = cmdLine.getOptionValue("rel_from");
		String toLemma = cmdLine.getOptionValue("rel_to");
		String type = cmdLine.getOptionValue("rel_type");
		
		if (null != fromLemma && null != toLemma)
			return queries.getRelationsFor(fromLemma, toLemma);
		if (null != fromLemma && null != type)
			return queries.getRelationsHavingLeft(fromLemma, type);
		if (null != toLemma && null != type)
			return queries.getRelationsHavingRight(toLemma, type);
		
		if (null != fromLemma && null == toLemma && null == type) {
			results.add("param -rel_from needs to be followed by param -rel_to or -rel_type");
			return results;
		}
		if (null != toLemma && null == fromLemma && null == type) {
			results.add("param -rel_to needs to be accompanied by param -rel_from or -rel_type");
			return results;
		}
		
		if (null != type && null != number)
			return queries.getTopRelations(type, number);
		if (null != type && null == number)
			return queries.getTopRelations(type, DEFAULT_NUMBER);
		
		// info on clusters and domains wanted? ///////////////////////////
		
		if (cmdLine.hasOption("clusters"))
			return queries.getAllClusterNames();
		
		String clusterId = cmdLine.getOptionValue("cluster_id");
		if (null != clusterId)
			return queries.getClusterById(clusterId);
		
		String clustersMatching = cmdLine.getOptionValue("clusters_matching");
		if (null != clustersMatching)
			return queries.getClustersMatching(clustersMatching);
		
		String clustersHaving = cmdLine.getOptionValue("clusters_having");
		if (null != clustersHaving)
			return queries.getAllClustersFor(clustersHaving);
		clustersHaving = cmdLine.getOptionValue("domains_having");
		if (null != clustersHaving)
			return queries.getAllDomainsFor(clustersHaving);
		
		String cluster = cmdLine.getOptionValue("items_for_cluster");
		if (null != cluster)
			return queries.getItemsForCluster(cluster);
		
		String domain = cmdLine.getOptionValue("items_for_domain");
		if (null != domain)
			return queries.getItemsForDomain(domain);
		
		return results;
	}
	
	public static void main (String[] args) throws Exception {
		
		List<String> results = new ArrayList<String>();
		PrintStream out = new PrintStream(System.out, true, "UTF-8");
		
		CommandLineParser parser = new GnuParser();
	    try {
	      
	    	MerkorCoreCommandLineOptions.createOptions();
	    	results = processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	out.print("\n");
	    	for (String str : results) {
	    		if(!str.equals("no message"))
	    			out.println(str);
			}
	    	out.print("\n");
			if (results.isEmpty()) {	
				out.println("nothing found for parameters: ");
				for (int i = 0; i < args.length; i++)
					out.println("\t" + args[i]);
				out.println("for help type: -help or see README.markdown");
				out.print("\n");
			}
	    }
	    catch(ParseException e) {
	        System.err.println("Parsing failed.  Reason: " + e.getMessage());
	    }	
	}
}
