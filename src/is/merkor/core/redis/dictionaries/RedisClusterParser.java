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
import is.merkor.core.redis.data.RedisCluster;
import is.merkor.core.util.MerkorLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

/**
 * Parses data from Redis and provides methods to retrieve {@link Cluster}s. 
 * Used by {@link RedisClusterDictionary}.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */
public class RedisClusterParser {
	
	private Jedis jedis;
	
	private static Logger logger;
	
	/*
	 * Constructs a new parser using default settings for
	 * Jedis (localhost).
	 */
	protected RedisClusterParser () throws Exception {
		jedis = new Jedis("localhost");
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisClusterParser.class);
		if (!RedisConnectionCheck.checkConnection(jedis)) {
			Exception e = new Exception();
			logger.error("Jedis connection for \"localhost\" failed!");
			throw e;
		}
	}
	/*
	 * Constructs a new parser using {@code host} and {@code port}
	 * to instantiate the contained Jedis object.
	 */
	protected RedisClusterParser (final String host, final int port) throws Exception {
		jedis = new Jedis(host, port);
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisClusterParser.class);
		if (!RedisConnectionCheck.checkConnection(jedis)) {
			Exception e = new Exception();
			logger.error("Jedis connection for host " + host + " and port " + port + " failed!");
			throw e;
		}
	}
	/*
	 * Constructs a new parser using {@code jedis}.
	 */
	protected RedisClusterParser (final Jedis jedis) throws Exception {
		this.jedis = jedis;
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisClusterParser.class);
		if (!RedisConnectionCheck.checkConnection(jedis)) {
			Exception e = new Exception();
			logger.error("Jedis connection with given Jedis object failed!");
			throw e;
		}
	}
	
	/*
	 * Return a list of clusters having names matching parameter domainRegex
	 */
	protected List<Cluster> getClustersMatching (final String domainRegex) {
		String lowerRegex = domainRegex.toLowerCase();
		String upperRegex = domainRegex.toUpperCase();
		lowerRegex = makeRedisConform(lowerRegex);
		upperRegex = makeRedisConform(upperRegex);
		// get both upper and lower case versions of cluster names
		Set<String> clusterKeys = jedis.keys(MerkorRedisConstants.CLUSTER_ID_IS + lowerRegex + "_[0123456789]*");
		clusterKeys.addAll(jedis.keys(MerkorRedisConstants.CLUSTER_ID_IS + upperRegex + "_[0123456789]*"));
		
		return extractClusters(clusterKeys);
	}
	/*
	 * Return the cluster having param id as its id, returns null if no cluster is found.
	 */
	protected Cluster getClusterById (final Long id) {
		Set<String> clusterKeys = jedis.keys(MerkorRedisConstants.CLUSTER_ID_IS + "*_" + id);
		RedisCluster cluster = null;
		String[] keyArr;

		for (String key : clusterKeys) {
			keyArr = key.split("_");
			if (keyArr.length == 5) {
				cluster = new RedisCluster(id, keyArr[3]);
			}
			// cluster name contains '_'
			else if (keyArr.length == 6) {
				String name = keyArr[3] + "_" + keyArr[4];
				cluster = new RedisCluster(id, name);
			}
		}
		return cluster;
	}
	/*
	 * Returns a list of all cluster names as strings.
	 */
	protected List<String> getAllClusterNames() {
		Set<String> clusterKeys = jedis.keys(MerkorRedisConstants.CLUSTER_ID_IS + "*");
		Set<String> uniqueNames = new TreeSet<String>();
		
		String[] keyArr;
		for (String key : clusterKeys) {
			keyArr = key.split("_");
			if (keyArr.length == 5) {
				String name = keyArr[3];
				uniqueNames.add(name);
			}
			// cluster name contains '_'
			else if (keyArr.length == 6) {
				String name = keyArr[3] + "_" + keyArr[4];
				uniqueNames.add(name);
			}
		}
		List<String> namesAsList = new ArrayList<String>(uniqueNames);
		return namesAsList;
	}
	
	private String makeRedisConform (String regex) {
		regex = regex.replaceAll("\\.\\*", "\\*");
		regex = regex.replaceAll("\\.\\?", "\\?");
		return regex;
	}
	private String makeJavaConform (String regex) {
		regex = regex.replaceAll("\\*", "\\.\\*");
		regex = regex.replaceAll("\\?", "\\.\\?");
		return regex;
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
	
	private List<Cluster> extractClusters (final Set<String> clusterKeys) {
		List<Cluster> clusterList = new ArrayList<Cluster>();
		String[] keyArr;
		for (String key : clusterKeys) {
			keyArr = key.split("_");
			if (keyArr.length == 5) {
				String name = keyArr[3];
				Long id = parseClusterId(keyArr[4]);
				clusterList.add(new RedisCluster(id, name));
			}
			// cluster name contains '_'
			else if (keyArr.length == 6) {
				String name = keyArr[3] + "_" + keyArr[4];
				Long id = parseClusterId(keyArr[5]);
				clusterList.add(new RedisCluster(id, name));
			}
		}
		return clusterList;
	}
}
