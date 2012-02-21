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
* An interface for implementations of relations.
* 
* <ul>Attributes:
*    <li>Long id, unique id. </li>
*	 <li>Pair pair, pair of Items connected by the relation.</li>
*	 <li>RelationType type, type of relation connecting the Items.</li>
*
* @author Anna B. Nikulasdottir
* @version 0.8
* 
*/
public interface Relation extends Comparable <Relation> {
	
	/**
	 * Returns the unique id for this relation.
	 * @return id
	 */
	public Long getId ();
	
	/**
	 * Returns the Pair of {@link Item}s connected in this Relation.
	 * 
	 * @return pair
	 */
	public Pair getPair ();
	
	/**
	 * Returns the {@link RelationType} of this Relation.
	 * @return RelationType
	 */
	public RelationType getType ();
	
	/**
	 * Sets the {@link RelationType} of this Relation.
	 * @param type
	 * @throws IllegalArgumentException if param is {@code null}
	 */
	public void setType (RelationType type);
	
	/**
	 * Returns the confidence value of this relation.
	 * The confidence value for a relation is computed using results from
	 * the automatic extraction of semantic information from a corpus.
	 *  
	 * @return the confidence value of this relation
	 */
	public Double getConfidence();
	
	/**
	 * Returns the certainty value of this relation.
	 * The certainty value for a relation is a value from 1 to 3,
	 * 1 indicating a high certainty that this relation is valid,
	 * 3 a low certainty.
	 *  
	 * @return the certainty value of this relation
	 */
	public int getCertainty();
	
	/**
	 * Sets the certainty value of this relation.
	 * The certainty value for a relation is a value from 1 to 3,
	 * 1 indicating a high certainty that this relation is valid,
	 * 3 a low certainty.
	 *  
	 * @param cert has to be an int 1, 2, or 3
	 * @throws IllegalArgumentException if param is not 1, 2 or 3
	 */
	public void setCertainty (int cert);
	
	public int compareTo (Relation relation);

}
