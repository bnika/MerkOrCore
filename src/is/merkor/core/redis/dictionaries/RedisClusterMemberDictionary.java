/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core.redis.dictionaries;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import is.merkor.core.Cluster;
import is.merkor.core.ClusterMember;
import is.merkor.core.ClusterMemberDictionary;
import is.merkor.core.Item;
import is.merkor.core.util.MerkorLogger;

/**
 * An implementation of the {@link ClusterMemberDictionary} interface to access
 * {@link ClusterMember}s stored in Redis format.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */
public class RedisClusterMemberDictionary implements ClusterMemberDictionary {
	
	private RedisClusterMemberParser parser = null;
	private static Logger logger;
	
	/**
	 * Constructs a new dictionary using default settings for
	 * Jedis 
	 */
	public RedisClusterMemberDictionary () throws Exception {
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
		parser = new RedisClusterMemberParser();
	}
	/**
	 * Constructs a new dictionary using {@code host} and {@code port}
	 * to instantiate the contained Jedis object.
	 */
	public RedisClusterMemberDictionary (final String host, final int port) throws Exception {
		parser = new RedisClusterMemberParser(host, port);
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
	}
	/**
	 * Constructs a new dictionary using {@code jedis}.
	 * @param jedis
	 */
	public RedisClusterMemberDictionary (final Jedis jedis) throws Exception {
		parser = new RedisClusterMemberParser(jedis);
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ClusterMemberDictionary#getClustersFor(java.lang.String)
	 */
	@Override
	public List<ClusterMember> getClustersFor (final String lemma) {
		validateLemma(lemma);
		return parser.getClustersFor(lemma);
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ClusterMemberDictionary#getClustersFor(java.lang.Long)
	 */
	@Override
	public List<Cluster> getClustersFor (final Long itemId) {
		validateObject(itemId, "id");
		return parser.getClustersFor(itemId);
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ClusterMemberDictionary#getClusterItemsForCluster(java.lang.Long)
	 */
	@Override
	public List<? extends ClusterMember> getClusterItemsForCluster (final Long clusterId) {
		validateObject(clusterId, "clusterId");
		return parser.getClusterItemsForCluster(clusterId);
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ClusterMemberDictionary#getClusterItemsForItem(is.merkor.core.Item)
	 */
	@Override
	public List<? extends ClusterMember> getClusterItemsForItem (final Item item) {
		validateObject(item, "item");
		return parser.getClusterItemsForItem(item);
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ClusterMemberDictionary#getDomainsFor(java.lang.String)
	 */
	@Override
	public Map<String, Item> getDomainsFor (final String lemma) {
		validateLemma(lemma);
		return parser.getDomainsFor(lemma);
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ClusterMemberDictionary#getItemsForDomain(java.lang.String)
	 */
	@Override
	public List<Item> getItemsForDomain (final String domain) {
		validateObject(domain, "domain");
		return parser.getItemsForDomain(domain);
	}
	
	private void validateLemma (final String lemma) {
		if (null == lemma || lemma.isEmpty()) {
			IllegalArgumentException e = new IllegalArgumentException();
			logger.error("param 'lemma' must not be empty!", e);
			throw e;
		}
	}
	private void validateObject (final Object obj, final String paramName) {
		if (null == obj) {
			IllegalArgumentException e = new IllegalArgumentException();
			logger.error("param " + paramName + " must not be null!", e);
			throw e;
		}
	}
}
