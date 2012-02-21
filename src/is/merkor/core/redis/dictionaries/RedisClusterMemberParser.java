/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core.redis.dictionaries;

import is.merkor.core.Cluster;
import is.merkor.core.ClusterMember;
import is.merkor.core.Item;
import is.merkor.core.redis.data.RedisCluster;
import is.merkor.core.redis.data.RedisClusterMember;
import is.merkor.core.redis.data.RedisItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

/**
 * Parses data from Redis and provides methods to retrieve {@link Cluster}s and 
 * {@link ClusterMember}s. Used by {@link is.merkor.core.ClusterMemberDictionary}.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */
public class RedisClusterMemberParser {
	
	private Jedis jedis;
	private RedisItemDictionary itemDict;
	private RedisItemParser itemParser;
	private RedisClusterDictionary clusterDict;
	
	private static Logger logger;
	
	/*
	 * Constructs a new parser using default settings for
	 * Jedis (localhost).
	 */
	protected RedisClusterMemberParser () throws Exception {
		jedis = new Jedis("localhost");
		if (!RedisConnectionCheck.checkConnection(jedis)) {
			Exception e = new Exception();
			logger.error("Jedis connection for \"localhost\" failed!");
			throw e;
		}
		itemDict = new RedisItemDictionary();
		itemParser = new RedisItemParser();
		clusterDict = new RedisClusterDictionary();
	}
	/*
	 * Constructs a new parser using {@code host} and {@code port}
	 * to instantiate the contained Jedis object.
	 */
	protected RedisClusterMemberParser (final String host, final int port) throws Exception {
		jedis = new Jedis(host, port);
		if (!RedisConnectionCheck.checkConnection(jedis)) {
			Exception e = new Exception();
			logger.error("Jedis connection for host " + host + " and port " + port + " failed!");
			throw e;
		}
		itemDict = new RedisItemDictionary(host, port);
		itemParser = new RedisItemParser(host, port);
		clusterDict = new RedisClusterDictionary(host, port);
	}
	/*
	 * Constructs a new parser using {@code jedis}.
	 */
	protected RedisClusterMemberParser (final Jedis jedis) throws Exception {
		this.jedis = jedis;
		if (!RedisConnectionCheck.checkConnection(jedis)) {
			Exception e = new Exception();
			logger.error("Jedis connection with given Jedis object failed!");
			throw e;
		}
		itemDict = new RedisItemDictionary(jedis);
		itemParser = new RedisItemParser(jedis);
		clusterDict = new RedisClusterDictionary(jedis);
		
	}
	/*
	 * Get all clusterMembers having {@code lemma} as the lemma of its item
	 */
	protected List<ClusterMember> getClustersFor (final String lemma) {
		
		List<ClusterMember> clusterList = new ArrayList<ClusterMember>();
		List<RedisItem> itemList = (List<RedisItem>)itemDict.getItemsFor(lemma);
		
		for (RedisItem item : itemList) {
			List<ClusterMember> clusterItemList = getClusterItemsForItem(item);
			clusterList.addAll(clusterItemList);
		}
		return clusterList;
	}
	
	/*
	 * Get all clusters a lexical item having {@code item_id} as its id belongs to.
	 */
	protected List<Cluster> getClustersFor (final Long item_id) {
		List<Cluster> clusterList = new ArrayList<Cluster>();
		// get all cluster names + ids as strings for the key (prefix +) item_id
		Set<String> clusterSet = jedis.smembers(MerkorRedisConstants.IN_CLUSTER_IS + MerkorRedisConstants.ID_KEY_PREFIX_IS + item_id);
		for (String cluster : clusterSet) {
			Cluster c = parseClusterString(cluster);
			if (null != c)
				clusterList.add(c);
		}
		return clusterList;
	}
	
	private Long parseClusterId (final String string) {
		try {
			Long id = Long.parseLong(string);
			return id;
		} catch (NumberFormatException e) {
			logger.warn(string + " should be a cluster id, parsing failed!", e);
		}
		return 0L;
	}
	/*
	 * Returns a list of all clusterMembers belonging to the cluster with 
	 * id clusterId
	 */
	protected List<ClusterMember> getClusterItemsForCluster (final Long clusterId) {
		List<ClusterMember> itemList = new ArrayList<ClusterMember>();
		Cluster cluster = clusterDict.getClusterById(clusterId);
		String clusterKey = getClusterKeyForId(clusterId);
		
		// collect clusterMembers as tuples containing clusterMember id 
		// and its score in the cluster, sorted by score descending
		Set<Tuple> clusterMembers = jedis.zrevrangeWithScores(clusterKey, 0, -1);
		
		for (Tuple tup : clusterMembers) {
			String item_key = tup.getElement();
			Double value = tup.getScore();
			Item item = itemParser.createRedisItemFromIdKey(item_key);
			itemList.add(new RedisClusterMember(item, cluster, value));
		}
		
		return itemList;
	}
	
	/*
	 * Returns a list of all clusterMembers containing item, which shows
	 * the clusters item belongs to.
	 */
	protected List<ClusterMember> getClusterItemsForItem (final Item item) {
		List<ClusterMember> itemList = new ArrayList<ClusterMember>();
		String item_key = itemParser.getRedisKeyForItem(item);
		Set<String> clusterKeys = jedis.smembers(MerkorRedisConstants.IN_CLUSTER_IS + item_key);
		for (String cluster : clusterKeys) {
			ClusterMember member = parseClusterMemberString(cluster, item);
			if (null != member)
				itemList.add(member);
		}
		return itemList;
	}
	/*
	 * a cluster string has the format:
	 * merkor_is_cluster_CLUSTERNAME_ID
	 * or:
	 * merkor_is_cluster_CLUSTER_NAME_ID
	 */
	private Cluster parseClusterString (final String cluster) {
		Cluster resultCluster = null;
		String name = "";
		Long cluster_id = 0L;
		String[] idArr = cluster.split("_");
		// cluster name is only one word
		if (idArr.length == 5) {
			name = idArr[3];
			cluster_id = parseClusterId(idArr[4]);
			if (cluster_id > 0)
				resultCluster = new RedisCluster(cluster_id, name);
		}
		// cluster name is a two-word expression connected with '_'
		else if (idArr.length == 6) {
			name = idArr[3] + "_" + idArr[4];
			cluster_id = parseClusterId(idArr[5]);
			resultCluster = new RedisCluster(cluster_id, name);
		}
		return resultCluster;
	}
	
	private ClusterMember parseClusterMemberString (final String cluster, final Item item) {
		Cluster resCluster = parseClusterString(cluster);
		if (null != resCluster)
			return new RedisClusterMember(item, resCluster, 0.0);
		else 
			return null;
	}
	/*
	 * Returns a map of domains parameter lemma belongs to, the map
	 * having the structure <domainName, item>.
	 */
	protected Map<String, Item> getDomainsFor (final String lemma) {
		List<ClusterMember> clusterList = getClustersFor(lemma);
		String domain;
		Map<String, Item> domainMap = new HashMap<String, Item>();
		for (ClusterMember clusterObj : clusterList) {
			domain = clusterObj.getCluster().getName();
			// not all clusters have been named - if not, use lemma of the center item
			if (domain.isEmpty())
				domain = clusterObj.getCluster().getCenter().getLemma();
			domainMap.put(domain, clusterObj.getItem());
		}
		return domainMap;
	}
	
	private String getClusterKeyForId (final Long id) {
		String clusterKey = "";
		Set<String> possibleClusters = jedis.keys(MerkorRedisConstants.CLUSTER_ID_IS + "*" + id);
		for (String s : possibleClusters) {
			Long cluster_id = 0L;
			String[] idArr = s.split("_");
			if (idArr.length == 5) {
				String name = idArr[3];
				cluster_id = Long.parseLong(idArr[4]);
				if (cluster_id.equals(id)) {
					clusterKey = MerkorRedisConstants.CLUSTER_ID_IS + name + "_" + id;
				}
					
			}
			else if (idArr.length == 6) {
				String name = idArr[3] + "_" + idArr[4];
				cluster_id = Long.parseLong(idArr[5]);
				if (cluster_id.equals(id))
					clusterKey = MerkorRedisConstants.CLUSTER_ID_IS + name + "_" + id;
			}
		}
		return clusterKey;
	}
	
	/*
	 * Returns a list of items belonging to the parameter domain.
	 */
	protected List<Item> getItemsForDomain (final String domain) {
		List<Item> itemList = new ArrayList<Item>();
		String redisValidLower = makeRedisConform(domain.toLowerCase());
		String redisValidUpper = makeRedisConform(domain.toUpperCase());
		
		Set<String> clusterKeys = jedis.keys(MerkorRedisConstants.CLUSTER_ID_IS + redisValidLower + "_[0123456789]*");
		clusterKeys.addAll(jedis.keys(MerkorRedisConstants.CLUSTER_ID_IS + redisValidUpper + "_[0123456789]*"));
		
		for (String key : clusterKeys) {
			Set<String> itemKeys = jedis.zrevrange(key, 0, -1);
			for (String it_key : itemKeys)
				itemList.add(itemParser.createRedisItemFromIdKey(it_key));
		}
		Collections.sort(itemList);
		return itemList;
	}
	
	private String makeRedisConform (String regex) {
		regex = regex.replaceAll("\\.\\*", "\\*");
		regex = regex.replaceAll("\\.\\?", "\\?");
		return regex;
	}
}

