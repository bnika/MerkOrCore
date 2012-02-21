/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core.cli;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import is.merkor.core.Cluster;
import is.merkor.core.ClusterMember;
import is.merkor.core.Item;
import is.merkor.core.Relation;
import is.merkor.core.RelationObject;
import is.merkor.core.RelationType;
import is.merkor.core.redis.data.RedisCluster;
import is.merkor.core.redis.data.RedisClusterMember;
import is.merkor.core.redis.data.RedisRelationType;
import is.merkor.core.redis.dictionaries.RedisClusterDictionary;
import is.merkor.core.redis.dictionaries.RedisClusterMemberDictionary;
import is.merkor.core.redis.dictionaries.RedisItemDictionary;
import is.merkor.core.redis.dictionaries.RedisRelationDictionary;
import is.merkor.core.util.RelationTypeMap;

/**
 * Processes queries to the MerkOr Redis resource, the methods mostly return lists of strings
 * for displaying the results e.g. in a shell.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */

public class MerkorCommandLineQueries {
	private RedisItemDictionary itemDict;
	private RedisRelationDictionary relDict;
	private RedisClusterDictionary clusterDict;
	private RedisClusterMemberDictionary clusterMembDict;
	
	private List<String> results;
	
	public MerkorCommandLineQueries (final String host, final int port) throws Exception {
		Jedis jedis = new Jedis(host, port);
		
		itemDict = new RedisItemDictionary(jedis);
		relDict = new RedisRelationDictionary(jedis);
		clusterDict = new RedisClusterDictionary(jedis);
		clusterMembDict = new RedisClusterMemberDictionary(jedis);
		
	}
	/**
	 * Returns a list of redisItems as strings having {@code lemma} as lemma.
	 */
	public List<String> getItemsForLemma (String lemma) {
		List<? extends Item> itemList = itemDict.getItemsFor(lemma);
		return itemListToStringList(itemList);
	}
	/**
	 * Returns a list of redisItems as strings which lemma matches {@code regex}
	 */
	public List<String> getItemsForRegex (String regex) {
		List<? extends Item> itemList = itemDict.getItemsMatching(regex);
		return itemListToStringList(itemList);
	}
	private List<String> itemListToStringList (List<? extends Item> list) {
		results = new ArrayList<String>();
		for (Item item : list) {
			results.add(item.toString());
		}
		return results;
	}
	/**
	 * Returns a list of the top {@code number} of relations (as strings) for {@code lemma}.
	 * 
	 */
	public List<String> getMostRelated (String lemma, String number) {
		results = new ArrayList<String>();
		try {
			Integer nrOfRelations = Integer.parseInt(number);
			List<RelationObject> relations = relDict.getMostRelatedWords(lemma, nrOfRelations);
			results = convertRelObjectsToStrings(relations, lemma);
			
		} catch (NumberFormatException e) {
			System.out.println("argument of '-n' has to be an integer! " + e.getMessage());
		}
		return results;
	}
	
	/**
	 * Returns a list of all relations (as strings) containing {@code lemma}.
	 */
	public List<String> getRelationsFor (String lemma) {
		results = new ArrayList<String>();
		List<RelationObject> relations = relDict.getRelationsFor(lemma);
		if (relations.isEmpty()) {
			results.add("No relation found for '" + lemma + "'");
		}
		else {
			results = convertRelObjectsToStrings(relations, lemma);
		}
		return results;
	}
	/**
	 * Returns a list of relations (as strings) containing both {@code lemma1} 
	 * and {@code lemma2}.
	 */
	public List<String> getRelationsFor (String lemma1, String lemma2) {
		results = new ArrayList<String>();
		List<RelationObject> relations = relDict.getRelationsFor(lemma1, lemma2);
		
		if (relations.isEmpty()) {
			results.add("No relations found for '" + lemma1 + "' and '" + lemma2 + "'");
		}
		else {
			formatRelations(relations, lemma1, lemma2);	
		}
		return results;
	}
	
	private void formatRelations (List<RelationObject> relations, String lemma1, String lemma2) {
		List<String> list1 = getItemsForLemma(lemma1);
		List<String> list2 = getItemsForLemma(lemma2);
		// if one of the lemma belongs to more than one item, it should be
		// reached on to convertRelObjectsToStrings() for the number-of-items-found message
		if (list1.size() > 1 && list2.size() == 1)
			results = convertRelObjectsToStrings(relations, lemma1);
		else if (list2.size() > 1)
			results = convertRelObjectsToStrings(relations, lemma2);
		else
			results = convertRelObjectsToStrings(relations, null);
	}
	
	/**
	 * Returns a list of relations (as strings) having {@code lemma} as their left item and
	 * {@code relation} as their relation type.
	 */
	public List<String> getRelationsHavingLeft (String lemma, String relation) {
		List<String> results = new ArrayList<String>();
		RelationType relType = new RedisRelationType(RelationTypeMap.NAME_ID_MAP.get(relation), relation, relation);
		List<RelationObject> relations = relDict.getRelationsHavingLeft(lemma, relType);
		if (relations.isEmpty()) {
			results.add("No relations found having '" + lemma + "' as left element and '" + relation + "' as relation");
		}
		else {
			results = convertRelObjectsToStrings(relations, lemma);
		}
		
		return results;
	}
	/**
	 * Returns a list of relations (as strings) having {@code lemma} as their right item and
	 * {@code relation} as their relation type.
	 */
	public List<String> getRelationsHavingRight (String lemma, String relation) {
		List<String> results = new ArrayList<String>();
		RelationType relType = new RedisRelationType(RelationTypeMap.NAME_ID_MAP.get(relation), relation, relation);
		List<RelationObject> relations = relDict.getRelationsHavingRight(lemma, relType);
		
		if (relations.isEmpty()) {
			results.add("No relations found having '" + lemma + "' as right element and '" + relation + "' as relation");
		}
		else {
			results = convertRelObjectsToStrings(relations, lemma);
		}
		return results;
	}
	
	/**
	 * Returns a list of the {@code number} top relations (as strings)
	 * of the type {@code relationType}.
	 */
	public List<String> getTopRelations (String relationType, String number) {
		List<String> results = new ArrayList<String>();
	
		Long type = RelationTypeMap.NAME_ID_MAP.get(relationType);
		if (null != type) {
			try {
				Integer nrOfRelations = Integer.parseInt(number);
				RelationType relType = new RedisRelationType(type, relationType, relationType);
				List<Relation> relations = relDict.getMostRelated(relType, nrOfRelations);
				for (Relation rel : relations) 
					results.add(rel.toString());
			} catch (NumberFormatException e) {
				System.err.println("Argument of -n must be an integer!" + e.getMessage());
			}
		}
		return results;
	}
	
	private List<String> convertRelObjectsToStrings (List<RelationObject> relationObjects, String lemma) {
		results = new ArrayList<String>();
		if (lemma != null)
			results.add(relationObjects.size() + " items found for '" + lemma + "': ");
		results.add("****************************************************");
		for (int i = 0; i < relationObjects.size(); i++) {
			results.add("Item " + (i+1) + ":");
			results.add(relationObjects.get(i).getItem().toString());
			results.add("Relations:");
			for (Relation rel : relationObjects.get(i).getRelation()) {
				results.add(rel.toString());
			}
			results.add("****************************************************");
		}
		return results;
	}
	/**
	 * Returns a list of all cluster names
	 */
	public List<String> getAllClusterNames() {
		return clusterDict.getAllClusterNames();
	}
	/**
	 * Returns a list containing the cluster (as string) having {@code clusterId}
	 * as its id.
	 */
	public List<String> getClusterById (String clusterId) {
		List<String> results = new ArrayList<String>();
		try {
			Long id = Long.parseLong(clusterId);
			Cluster cl = clusterDict.getClusterById(id);
			if (null != cl)
				results.add(cl.toString());
		} catch (NumberFormatException e) {
			System.out.println("argument of '-cluster_id' has to be an integer! " + e.getMessage());
		}
		return results;
	}
	/**
	 * Returns a list of clusters (as strings) where cluster names match {@code regex}.
	 */
	public List<String> getClustersMatching(String regex) {
		List<String> results = new ArrayList<String>();
		List<RedisCluster> list = (List<RedisCluster>)clusterDict.getClustersMatching(regex);
		for (Cluster cl : list) {
			results.add(cl.toString());
		}
		return results;
	}
	/**
	 * Returns a list of clusters (as strings) containing {@code lemma}.
	 */
	public List<String> getAllClustersFor(String lemma) {
		List<String> results = new ArrayList<String>();
		List<ClusterMember> clusters = clusterMembDict.getClustersFor(lemma);
		for (ClusterMember cl : clusters)
			results.add(cl.toString());
		
		return results;
	}
	/**
	 * Returns a list of domain names to which {@code lemma} belongs.
	 */
	public List<String> getAllDomainsFor (String lemma) {
		List<String> results = new ArrayList<String>();
		Map<String, Item> domainMap = clusterMembDict.getDomainsFor(lemma);
		
		for (String str : domainMap.keySet()) {
			results.add("domain: " + str + ", " + domainMap.get(str));
		}
		return results;
	}
	/**
	 * Returns a list of items (as strings) belonging to {@code cluster}.
	 */
	public List<String> getItemsForCluster(String cluster) {
		List<String> results = new ArrayList<String>();
		Long id = parseClusterId(cluster);
		List<RedisClusterMember> items = (List<RedisClusterMember>)clusterMembDict.getClusterItemsForCluster(id);
		results.add(items.size() + " items for " + clusterDict.getClusterById(id).toString() + ":");
		for (ClusterMember item : items) {
			results.add(item.toString());
		}
		return results;
	}
	/**
	 * Returns a list of items (as strings) belonging to {@code domain}.
	 */
	public List<String> getItemsForDomain(String domain) {
		List<String> results = new ArrayList<String>();
		List<Item> items = clusterMembDict.getItemsForDomain(domain);
		results.add(items.size() + " items for domain " + domain.toUpperCase() + ":");
		for (Item item : items) {
			results.add(item.toString());
		}
		return results;
	}
	private Long parseClusterId (String cluster) {
		try {
			return Long.parseLong(cluster);
		} catch (NumberFormatException e) {
			System.err.println("Argument of -items_for_cluster must be an integer!" + e.getMessage());
		}
		return 0L;
	}
	
}
