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
 * An interface for implementations of relationType.
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */
public interface RelationType {
	
	/**
	 * Returns the unique id for this RelationType.
	 * @return id
	 */
	public Long getId ();
	
	/**
	 * Returns the name of this RelationType.
	 * @return name
	 */
	public String getName ();
	
	/**
	 * Returns the description of this RelationType, might be
	 * more appropriate for display than the exact name.
	 * @return description
	 */
	public String getDescription ();
}
