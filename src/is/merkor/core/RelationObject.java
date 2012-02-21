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
 * A wrapper class for an {@link Item} connected with a list of {@link Relation}s.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */


public class RelationObject {
	
	private Item item;
	private List<? extends Relation> relationList;
	
	/**
	 * Creates a new relationObject and initializes the {@code item} and the {@code relationList} attributes.
	 * @param item
	 * @param relationList a list of relations
	 * @throws IllegalArgumentException if param 'code' or param 'relationList' is {@code null}
	 */
	public RelationObject (Item item, List<? extends Relation> relationList) {
		if (null == item || null == relationList)
			throw new IllegalArgumentException("param 'item' or param 'relationList' may not be null!");
		this.item = item;
		this.relationList = relationList;
	}
	
	/**
	 * Returns this relationObject's {@code item} attribute.
	 * @return this relationObject's item
	 */
	public Item getItem () {
		return item;
	}
	/**
	 * Returns a list of relations connected to this relationObject's {@code item}.
	 * @return a list of relations or an empty list
	 */
	public List<? extends Relation> getRelation () {
		return relationList;
	}
	
	/**
	 * Returns a string representation of this relationObject.
	 * Calls the toString() method of this relationObject's {@code item} and {@code relationList}
	 */
	public String toString () {
		return item.toString() + " " + relationList.toString();
	}

}
