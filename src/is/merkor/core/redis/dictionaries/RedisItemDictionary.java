/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core.redis.dictionaries;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import is.merkor.core.Item;
import is.merkor.core.ItemDictionary;
import is.merkor.core.redis.data.RedisItem;
import is.merkor.core.util.MerkorLogger;
import is.merkor.core.util.Wordclass;

/**
 * An implementation of the {@link ItemDictionary} interface using {@link RedisItemParser}
 * to connect to a Redis server containing MerkOr data.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */
public class RedisItemDictionary implements ItemDictionary {
	private RedisItemParser parser;
	
	private static Logger logger;
	
	/**
	 * Constructs a new dictionary using default settings for
	 * Jedis 
	 */
	public RedisItemDictionary () throws Exception {
		parser = new RedisItemParser();
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
	}
	/**
	 * Constructs a new dictionary using {@code host} and {@code port}
	 * to instantiate the contained Jedis object.
	 */
	public RedisItemDictionary (String host, int port) throws Exception {
		parser = new RedisItemParser(host, port);
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
	}
	/**
	 * Constructs a new dictionary using {@code jedis}.
	 * @param jedis
	 */
	public RedisItemDictionary (Jedis jedis) throws Exception {
		parser = new RedisItemParser(jedis);
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ItemDictionary#getAllItems()
	 */
	@Override
	public List<? extends Item> getAllItems() {
		List<RedisItem> items = (List<RedisItem>)parser.collectAllItems();
		Collections.sort(items);
		return items;
	}

	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ItemDictionary#getItemsFor(java.lang.String)
	 */
	@Override
	public List<? extends Item> getItemsFor(final String lemma) {
		validateLemma(lemma);
		List<RedisItem> items = (List<RedisItem>)parser.getItemsFor(lemma);
		Collections.sort(items);
		return items;
	}

	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ItemDictionary#getItemsFor(java.lang.String, java.lang.String)
	 */
	@Override
	public List<? extends Item> getItemsFor(final String lemma, final String wordclass) {
		validateLemma(lemma);
		validateWordclass(wordclass);
		List<RedisItem> items = (List<RedisItem>)parser.getItemsFor(lemma, wordclass);
		Collections.sort(items);
		return items;
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ItemDictionary#getItemsMatching(java.lang.String)
	 */
	@Override
	public List<? extends Item> getItemsMatching (final String regex) {
		validateLemma(regex);
		List<RedisItem> items = (List<RedisItem>)parser.getItemsMatching(regex);
		Collections.sort(items);
		return items;
	}
	
	public Item getItemFor (final Long id) {
		validateObject(id, "id");
		return parser.getItemFor(id);
	}
	
	private void validateLemma (final String lemma) {
		if (null == lemma || lemma.isEmpty()) {
			IllegalArgumentException e = new IllegalArgumentException();
			logger.error("param 'lemma' must not be empty!", e);
			throw e;
		}
	}
	private void validateWordclass (final String wordclass) {
		if (null == wordclass || !Wordclass.VALUES.contains(wordclass)) {
			IllegalArgumentException e = new IllegalArgumentException();
			logger.error("param 'wordclass' must not be null " +
					"and it has to match one of " + Wordclass.VALUES.toString() + "!", e);
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
