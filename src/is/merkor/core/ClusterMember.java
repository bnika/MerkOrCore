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
 * An interface for implementations of clusterMembers.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */
public interface ClusterMember {
	
	/**
	 * Returns the Item of this ClusterMember.
	 * 
	 * @return the Item of this ClusterMember
	 */
	public Item getItem ();
	
	/**
	 * Returns the Cluster of this ClusterMember.
	 * 
	 * @return the Cluster of this ClusterMember
	 */
	public Cluster getCluster ();
	
	/**
	 * Returns the similarity value of this ClusterMember to the center of
	 * the Cluster it belongs to. The closer to 1.0 the value is, the closer
	 * to the center it is.
	 * 
	 * @return similarity value
	 */
	public Double getValue ();	
}
