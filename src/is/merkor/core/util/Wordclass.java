/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License ...
 * 
 *******************************************************************************/
package is.merkor.core.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A typesafe enum class containing valid wordclasses (part-of-speech).
 * 
 * @author Anna B. Nikulasdottir
 * @version 1.0
 *
 */
public class Wordclass {
	private final String name;
	
	private Wordclass (String name) {
		this.name = name;
	}
	
	public String toString () {
		return name;
	}
	
	public static final Wordclass NOUN = new Wordclass("noun");
	public static final Wordclass VERB = new Wordclass("verb");
	public static final Wordclass ADJECTIVE = new Wordclass("adjective");
	
	private static final String[] PRIVATE_VALUES = {NOUN.toString(), VERB.toString(), ADJECTIVE.toString()};
	public static final List<String> VALUES = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));
}
