package test.core;


import static org.junit.Assert.*;

import java.util.List;

import is.merkor.core.cli.Main;
import is.merkor.core.cli.MerkorCoreCommandLineOptions;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

public class TestMain {
	
	CommandLineParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new GnuParser();
	}
	
	@Test
	public void testProcessCommandLineItems () {
		String[] args = {"-items", "skúr"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(!res.isEmpty());
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineItemsRegex () {
		String[] args = {"-items", "skúr*"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(res.size() > 10);
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test (expected = ParseException.class)
	public void testProcessCommandLineItemNoLemma () throws Exception {
		String[] args = {"-items"};
	    MerkorCoreCommandLineOptions.createOptions();
	    Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	
	}
	@Test (expected = ParseException.class)
	public void testProcessCommandLineItemNonValidParam () throws Exception {
		String[] args = {"-it", "skúr"};
	    MerkorCoreCommandLineOptions.createOptions();
	    Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	
	}
	@Test
	public void testProcessCommandLineRelations () {
		String[] args = {"-relations", "skúr"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(res.size() > 10);
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test (expected = ParseException.class)
	public void testProcessCommandLineRelationNoLemma () throws Exception {
		String[] args = {"-relations"};
	    MerkorCoreCommandLineOptions.createOptions();
	    Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	
	}
	@Test
	public void testProcessCommandLineTwoLemmata () {
		String[] args = {"-rel_from", "skúr", "-rel_to", "skin"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(!res.isEmpty());
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineFromRel () {
		String[] args = {"-rel_from", "opinn", "-rel_type", "coord_adj"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(!res.isEmpty());
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineToRel () {
		String[] args = {"-rel_to", "skúr", "-rel_type", "og"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(!res.isEmpty());
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineTopRels () {
		String[] args = {"-relations", "skúr", "-n", "5"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(res.size() > 5);
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineTopRelsByType () {
		String[] args = {"-rel_type", "og", "-n", "5"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(res.size() == 5);
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineTopRelsByTypeNoN () {
		String[] args = {"-rel_type", "án"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(!res.isEmpty());
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineClustersMatching () {
		String[] args = {"-clusters_matching", "íþrótt*"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(!res.isEmpty());
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineClustersHaving () {
		String[] args = {"-clusters_having", "skúr"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(!res.isEmpty());
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineDomainsHaving () {
		String[] args = {"-domains_having", "skúr"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(!res.isEmpty());
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineItemsForCluster () {
		String[] args = {"-items_for_cluster", "55"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(!res.isEmpty());
	    	for(String s : res)
	    		System.out.println(s);
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineItemsForDomain () {
		String[] args = {"-items_for_domain", "veður"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(!res.isEmpty());
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineClusters () {
		String[] args = {"-clusters"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(!res.isEmpty());
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineClusterById () {
		String[] args = {"-cluster_id", "25"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(!res.isEmpty());
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
	@Test
	public void testProcessCommandLineClusterByNonValidId () {
		String[] args = {"-cluster_id", "500"};
	    try {
	    	MerkorCoreCommandLineOptions.createOptions();
	    	List<String> res = Main.processCommandLine(parser.parse(MerkorCoreCommandLineOptions.options, args));
	    	assertTrue(res.isEmpty());
	    }
	    catch (ParseException exp) {
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    }
	}
}
