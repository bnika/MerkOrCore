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
import java.util.Map;

/**
 * An interface for the access of {@link ClusterMember}s.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */
public interface ClusterMemberDictionary {
	
	/**
	 * Retrieves all clusters for {@code lemma}.
	 * 
	 * @param lemma the lemma to find clusters for, may not be {@code null} or empty
	 * @return a List of clusterMembers, or an empty List if nothing is found.
	 * @throws IllegalArgumentException if param is {@code null} or empty.
	 */
	public List<ClusterMember> getClustersFor (String lemma);
	
	/**
	 * Retrieves all clusters for an {@link Item}
	 * having {@code id} as its id.
	 * 
	 * @param id id of an item
	 * @return a list of clusters containing items with id {@code id}.
	 * @throws IllegalArgumentException if param is {@code null}.
	 */
	public List<Cluster> getClustersFor(Long id);
	
	/**
	 * Retrieves all {@link ClusterMember}s for the
	 * {@link Cluster} having id {@code clusterId}.
	 * 
	 * @param clusterId the id of the cluster to retrieve items for
	 * @return a list of clusterItems, an empty list if nothing is found
	 * @throws IllegalArgumentException if param is {@code null}
	 */
	public List<? extends ClusterMember> getClusterItemsForCluster (Long clusterId);
	
	/**
	 * Retrieves all {@link ClusterMember}s containing
	 * {@code item}.
	 * 
	 * @param item the item to retrieve clusterItems for
	 * @return a list of clusterItems, an empty list if nothing is found
	 * @throws IllegalArgumentExcpetion if param is {@code null}
	 */
	public List<? extends ClusterMember> getClusterItemsForItem (Item item);
	
	/**
	 * Retrieves the domain names for {@code lemma}. Returns a Map
	 * whith domain names as keys and {@link Item}s having {@code lemma}
	 * as lemma as values.
	 * 
	 * @param lemma the lemma to find domain names for, may not be {@code null} or empty.
	 * @return a Map of domain names and Items, or an empty Map if nothing is found.
	 * @throws IllegalArgumentException if param is {@code null} or empty.
	 */
	public Map<String, Item> getDomainsFor (String lemma);
	
	/**
	 * Retrieves all Items belonging to {@code domain}. 
	 * 
	 * @param domain the domain to retrieve Items for
	 * @return a List of items, or an empty List if nothing is found.
	 * @throws IllegalArgumentException if param is {@code null} or empty.
	 */
	public List<Item> getItemsForDomain (String domain);
}
