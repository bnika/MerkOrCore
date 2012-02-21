/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core;

/**
 * An interface for implementations of item pairs.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */

public interface Pair extends Comparable<Pair> {
	
	/**
	 * Returns the unique id for this Pair.
	 * @return id
	 */
	public Long getId ();
	
	/**
	 * Returns the left Item of this Pair.
	 * @return left Item
	 */
	public Item getFrom ();
	
	/**
	 * Returns the right Item of this Pair.
	 * @return right Item
	 */
	public Item getTo ();
	
	public int compareTo (Pair pair);
}
