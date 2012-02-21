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
 * An interface for the access of {@link Cluster}s.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */

public interface ClusterDictionary {
	
	/**
	 * Retrieves all {@link Cluster}s having names matching {@code domainRegex}.
	 * Implementing classes should define which kind of regular expressions
	 * are allowed. If no wildcards are used, the method retrieves Clusters with the exact
	 * domain name {@code domainRegex}. The matching should not be case sensitive.
	 * 
	 * @param domainRegex a plain String or a regular expression to match against domain names
	 * @return a List of Clusters matching domainRegex, or an empty List if nothing is found.
	 * @throws IllegalArgumentException if param is {@code null}.
	 */
	public List<? extends Cluster> getClustersMatching (String domainRegex);
	
	/**
	 * Returns the Cluster with the unique {@code id}.
	 * 
	 * @param id the id of a Cluster
	 * @return a Cluster, returns {@code null} if a Cluster with id does not exist.
	 * @throws IllegalArgumentException if param is {@code null}.
	 */
	public Cluster getClusterById (Long id);
	
	/**
	 * Retrieves all Cluster names (domain names) from the MerkOr data.
	 * 
	 * @return a List of Cluster names
	 */
	public List<String> getAllClusterNames (); 

}
