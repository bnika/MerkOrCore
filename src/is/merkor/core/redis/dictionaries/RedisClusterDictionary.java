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

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import is.merkor.core.Cluster;
import is.merkor.core.ClusterDictionary;
import is.merkor.core.util.MerkorLogger;

/**
 * An implementation of the {@link ClusterDictionary} interface using a Redis
 * representation of the MerkOr data.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */
public class RedisClusterDictionary implements ClusterDictionary {
	
	private final RedisClusterParser parser;
	private static Logger logger;
	
	/**
	 * Constructs a new dictionary using default settings for
	 * Jedis 
	 */
	public RedisClusterDictionary () throws Exception {
		parser = new RedisClusterParser();
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
	}
	/**
	 * Constructs a new dictionary using {@code host} and {@code port}
	 * to instantiate the contained Jedis object.
	 */
	public RedisClusterDictionary (final String host, final int port) throws Exception {
		parser = new RedisClusterParser(host, port);
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
	}
	/**
	 * Constructs a new dictionary using {@code jedis}.
	 * @param jedis
	 */
	public RedisClusterDictionary (final Jedis jedis) throws Exception {
		parser = new RedisClusterParser(jedis);
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ClusterDictionary#getClustersMatching(java.lang.String)
	 */
	@Override
	public List<? extends Cluster> getClustersMatching (final String domainRegex) {
		validateString(domainRegex, "domainRegex");
		return parser.getClustersMatching(domainRegex);
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ClusterDictionary#getClusterById(java.lang.Long)
	 */
	@Override
	public Cluster getClusterById (final Long id) {
		validateObject(id, "id");
		return parser.getClusterById(id);
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ClusterDictionary#getAllClusterNames()
	 */
	@Override
	public List<String> getAllClusterNames () {
		return parser.getAllClusterNames();
	}
	
	private void validateString (final String lemma, final String paramName) {
		if (null == lemma || lemma.isEmpty()) {
			IllegalArgumentException e = new IllegalArgumentException();
			logger.error("param " + paramName + " must not be null or empty!", e);
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
