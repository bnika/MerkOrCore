/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 * Options for the MerkOrCore command line interface.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */
public class MerkorCoreCommandLineOptions {
	
	private static Option help;
	private static Option help_h;
	private static Option clusters;
	
	private static Option host;
	private static Option port;
	private static Option items; 
	private static Option relations;
	private static Option rel_from;
	private static Option rel_to;
	private static Option rel_type;
	private static Option number;
	private static Option cluster_id;
	private static Option clusters_matching;
	private static Option clusters_having;
	private static Option domains_having;
	private static Option items_for_cluster;
	private static Option items_for_domain;
	
	public static Options options = new Options();
	
	public static void createOptions () {
		
		createBooleanOptions();
		createArgumentOptions();
		
		options.addOption(help);
		options.addOption(help_h);
		options.addOption(clusters);
		options.addOption(host);
		options.addOption(port);
		options.addOption(items);
		options.addOption(relations);
		options.addOption(rel_from);
		options.addOption(rel_to);
		options.addOption(rel_type);
		options.addOption(number);
		options.addOption(cluster_id);
		options.addOption(clusters_matching);
		options.addOption(clusters_having);
		options.addOption(domains_having);
		options.addOption(items_for_cluster);
		options.addOption(items_for_domain);
		
	}

	private static void createBooleanOptions() {
		help = new Option("help", "print this message");
		help_h = new Option("h", "print this message");
		clusters = new Option("clusters", "get all cluster names");
	}
	private static void createArgumentOptions() {
		host   = OptionBuilder.withArgName("Redis host")
			.hasArg()
			.withDescription("the host for Redis (default=localhost)")
			.create("host");
		port   = OptionBuilder.withArgName("Redis port")
			.hasArg()
			.withDescription("the port for Redis (default=6379)")
			.create("port");
		items   = OptionBuilder.withArgName("lemma")
			.hasArg()
			.withDescription("get all lexical items having given lemma as lemma")
			.create("items");
		relations = OptionBuilder.withArgName("lemma")
			.hasArg()
			.withDescription("get all relations for the given lemma")
			.create("relations");
		rel_from = OptionBuilder.withArgName("lemma")
			.hasArg()
			.withDescription("get all relations having the given lemma as the left element")
			.create("rel_from");
		rel_to = OptionBuilder.withArgName("lemma")
			.hasArg()
			.withDescription("get all relations having the given lemma as the right element")
			.create("rel_to");
		rel_type = OptionBuilder.withArgName("rel_type")
			.hasArg()
			.withDescription("get all relations having the given relation type")
			.create("rel_type");
		number = OptionBuilder.withArgName("number")
			.hasArg()
			.withDescription("get the n top elements from required list of relations")
			.create("n");
		cluster_id = OptionBuilder.withArgName("cluster_id")
		.hasArg()
		.withDescription("get the cluster with the given id")
		.create("cluster_id");
		clusters_matching = OptionBuilder.withArgName("regex")
			.hasArg()
			.withDescription("get all clusters matching the given regex")
			.create("clusters_matching");
		clusters_having = OptionBuilder.withArgName("lemma")
			.hasArg()
			.withDescription("get all clusters having the given lemma")
			.create("clusters_having");
		domains_having = OptionBuilder.withArgName("lemma")
			.hasArg()
			.withDescription("get all domains the given lemma belongs to")
			.create("domains_having");
		items_for_cluster = OptionBuilder.withArgName("cluster_id")
			.hasArg()
			.withDescription("get all lexical items belonging to the given cluster")
			.create("items_for_cluster");
		items_for_domain = OptionBuilder.withArgName("domain")
			.hasArg()
			.withDescription("get all lexical items belonging to the given domain")
			.create("items_for_domain");
	}

}
