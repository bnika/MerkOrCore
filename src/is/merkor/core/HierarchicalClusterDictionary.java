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
 * An interface for the access of {@link HierarchicalCluster}s.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */

public interface HierarchicalClusterDictionary {
	
	/**
	 * Returns the names of the clusters in the {@link HierarchicalCluster} the
	 * {@code cluster} belongs to as a list of strings.
	 * 
	 * @param cluster a cluster for which a hierarchicalCluster will be searched for
	 * @return a list of the names of all clusters in the hierarchicalCluster found, an empty
	 * list if nothing is found
	 * @throws IllegalArgumentException if param is {@code null}
	 */
	public List<String> getClustersAsString (Cluster cluster);
	
	/**
	 * Returns a list of all clusters in the {@link HierarchicalCluster} the
	 * {@code cluster} belongs to.
	 * 
	 * @param cluster a cluster for which a hierarchicalCluster will be searched for
	 * @return a list of all clusters in the hierarchicalCluster found, an empty
	 * list if nothing is found
	 * @throws IllegalArgumentException if param is {@code null}
	 */
	public List<? extends Cluster> getClusters (Cluster cluster);

}
