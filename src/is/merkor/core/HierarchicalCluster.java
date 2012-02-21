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
 * An interface for implementations of HierarchicalClusters.
 * 
 * A HierarchicalCluster is a node in a binary tree which has {@link Cluster}s as leafs.
 * A HierarchicalCluster can be accessed by its id, but also by a leaf Cluster. Then the
 * hierarchy can be retrieved bottom up, returning a List of the Clusters in that hierarchy,
 * either as Clusters or as Strings ("domain_id").
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */
public interface HierarchicalCluster {

	/**
	 * Returns the unique id for this cluster.
	 * @return id
	 */
	public Long getId ();
	
	/**
	 * Returns the left leaf Cluster of this HierarchicalCluster.
	 * If this HierCluster does not have a left leaf Cluster, returns
	 * an empty Cluster with id=0.
	 *  
	 * @return a Cluster
	 */
	public Cluster getClusterId1(); 
	
	/**
	 * Returns the right leaf Cluster of this HierarchicalCluster.
	 * If this HierCluster does not have a right leaf Cluster, returns
	 * an empty Cluster with id=0.
	 *  
	 * @return a Cluster
	 */
	public Cluster getClusterId2(); 
	
	/**
	 * Returns the left sub HierCluster of this HierarchicalCluster.
	 * If this HierCluster does not have a left sub HierCluster, returns
	 * an empty HierCluster with id=0.
	 *  
	 * @return a HierarchicalCluster
	 */
	public HierarchicalCluster getSubCluster1();
	
	/**
	 * Returns the right sub HierCluster of this HierarchicalCluster.
	 * If this HierCluster does not have a right sub HierCluster, returns
	 * an empty HierCluster with id=0.
	 *  
	 * @return a HierarchicalCluster
	 */
	public HierarchicalCluster getSubCluster2();
	
	/**
	 * Returns the the top HierCluster of this HierarchicalCluster.
	 * If this HierCluster does not have a top sub HierCluster, returns
	 * an empty HierCluster with id=0.
	 *  
	 * @return a HierarchicalCluster
	 */
	public HierarchicalCluster getTopCluster();
	
	/**
	 * Returns a List of Strings ("domain_id") representing
	 * all Clusters in the hierarchy of this HierarchicalCluster.
	 * 
	 * @return a List of Strings representing Clusters
	 */
	public List<String> printClusters ();
	
	/**
	 * Returns a List of all leaf Clusters in the hierarchy
	 * of this HierarchicalCluster.
	 * 
	 * @return a List of Clusters
	 */
	public List<? extends Cluster> retrieveClusterList (); 

}
