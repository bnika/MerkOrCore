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
 * An interface for implementations of lexical items.
 * 
 * <ul>Attributes:
 * <li> Long id, a unique id for the item </li>
 * <li> String lemma </li>
 * <li> int sense, the sense number of the item, default is 1 for monosemous items </li>
 * <li> String wordclass </li>
 * <li> boolean hasMoreSenses, indicates if there exists another item with the same 
 * 							lemma and wordclass having another sense number</li>
 *  
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */

public interface Item extends Comparable<Item> {
	
	/**
	 * Returns the unique id for this item.
	 * @return id
	 */
	public Long getId ();
	
	/**
	 * Returns the lemma of this item.
	 * @return lemma
	 */
	public String getLemma ();
	
	/**
	 * Returns the sense number of this item, items having
	 * the same lemma and wordclass are distinguished by sense numbers. 
	 * @return sense number
	 */
	public int getSense ();
	
	/**
	 * Returns the wordclass (part-of-speech) of this item.
	 * @return wordclass
	 */
	public String getWordclass ();
	
	/**
	 * Returns a boolean indicating if there exists another
	 * item with the same lemma and wordclass.
	 * @return true if there exists another item with the same lemma and wordclass,
	 * 			false otherwise.
	 */
	public boolean getHasMoreSenses ();
	
	/**
	 * Returns the number of relations this item is a part of.
	 * That is, how many wordpairs contain this item.
	 * 
	 * @return number of relations for this item
	 */
	public int getWordpairCount ();
	
	public String getComment ();
	
	public void setComment (String comment);
	
	public boolean getHumanCorrected ();
}
