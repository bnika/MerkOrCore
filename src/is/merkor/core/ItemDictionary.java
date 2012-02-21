/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core;

import java.util.List;


/**
 * An interface for the access of {@link Item}s.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */
public interface ItemDictionary {
	/**
	 * Returns all Items available in the MerkOr datasource used.
	 * 
	 * @return a List of Items
	 */
	public List<? extends Item> getAllItems ();
	
	/**
	 * Retrieve all {@link Item}s having {@code lemma} as attribute lemma.
	 * If the lemma has only one sense and wordclass, only one item will be found, otherwise
	 * one item for each sense and wordclass will be returned.
	 * 
	 * @param lemma the lemma to retrieve Items for, may not be {@code null} or empty.
	 * @return a List of Items having lemma as lemma, or empty List if nothing is found.
	 * @throws IllegalArgumentException if param is {@code null} or empty.
	 */
	public List<? extends Item> getItemsFor (String lemma);
	
	/**
	 * Retrieve all {@link Item}s having {@code lemma} as attribute lemma and {@code wordclass}
	 * as wordclass (part-of-speech).
	 * If the lemma has only one sense, only one item will be found, otherwise
	 * one item for each sense will be returned.
	 * 
	 * @param lemma the lemma to retrieve Items for, may not be {@code null} or empty.
	 * @param wordclass the part-of-speech of the lemma to search for, may not be {@code null} or empty.
	 * @return a List of Items having lemma as lemma, or empty List if nothing is found.
	 * @throws IllegalArgumentException if param is {@code null} or empty.
	 */
	public List<? extends Item> getItemsFor (String lemma, String wordclass);
	
	/**
	 * Retrieve all {@link Item}s where attribute lemma matches {@code regex}.
	 * 
	 * @param regex the regular expression to match against lemma
	 * @return a List of items having lemma matching regex, or empty List if no match is found
	 * @throws IllegalArgumentException if param is {@code null}
	 */
	public List<? extends Item> getItemsMatching (String regex);
}
