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
 * An interface for implementations of Clusters.
 * 
 * A Cluster has a unique id and normally a name describing the domain the
 * Cluster represents. Not all Clusters are necessarily named though.
 * As an alternative, every Cluster has a center {@link Item}, the Item closest
 * to the center of the Cluster. The center Item can thus indicate the domain of 
 * the Cluster.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */
public interface Cluster {
	
	/**
	 * Returns the unique id for this cluster.
	 * @return id
	 */
	public Long getId ();
	
	/**
	 * Returns the name (domain) of this cluster.
	 * @return name, or an empty String if this cluster hasn't been named yet.
	 */
	public String getName ();
	
	/**
	 * Returns the {@link Item} which is closest to the center of this cluster.
	 * @return center Item of this cluster
	 */
	public Item getCenter ();
	
}
