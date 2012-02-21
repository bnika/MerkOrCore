/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core.redis.dictionaries;

/**
 * Provides methods to parse keys and values from MerkOr data on a Redis server,
 * and instantiates {@link RedisItem}s from the parsed information.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */
import is.merkor.core.Item;
import is.merkor.core.redis.data.RedisItem;
import is.merkor.core.util.MerkorLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

public class RedisItemParser {
	// connection to Redis
	private Jedis jedis;
	private static Logger logger;
	
	/*
	 * Constructs a new parser using default settings for
	 * Jedis (localhost).
	 */
	protected RedisItemParser () throws Exception {
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisItemParser.class);
		jedis = new Jedis("localhost");
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
	protected RedisItemParser (final String host, final int port) throws Exception {
		jedis = new Jedis(host, port);
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisItemParser.class);
		if (!RedisConnectionCheck.checkConnection(jedis)) {
			Exception e = new Exception();
			logger.error("Jedis connection for host " + host + " and port " + port + " failed!");
			throw e;
		}
	}
	/*
	 * Constructs a new parser using {@code jedis}.
	 */
	protected RedisItemParser (final Jedis jedis) throws Exception {
		this.jedis = jedis;
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisItemParser.class);
		if (!RedisConnectionCheck.checkConnection(jedis)) {
			Exception e = new Exception();
			logger.error("Jedis connection with given Jedis object failed!");
			throw e;
		}
	}
	
	/*
	 * Parses all lexical item keys from Redis, creates a {@link RedisItem} for each of
	 * them and returns a list of those items.
	 * 
	 * @return a list of redisItems, or an empty list if nothing is found
	 */
	protected List<? extends Item> collectAllItems () {
		
		Set<String> keys = jedis.keys(MerkorRedisConstants.ID_KEY_PREFIX_IS + "[0123456789]*");
		List<RedisItem> itemList = new ArrayList<RedisItem>();
		for (String key : keys) {
			itemList.add(createRedisItemFromIdKey(key));
		}
		return itemList;
	}
	
	/*
	 * Gets all items from Redis having {@code lemma} as its lemma, parses them and 
	 * creates {@link RedisItem}s from the found items.
	 * 
	 * @param lemma the lemma to search items for
	 * @return a list of redisItems, or an empty list if nothing is found
	 */
	protected List<? extends Item> getItemsFor (final String lemma) {
		List<RedisItem> itemList = new ArrayList<RedisItem>();
		Set<String> lemma_ids = jedis.smembers(MerkorRedisConstants.LEMMA_KEY_PREFIX_IS + lemma);
		
		for (String id : lemma_ids) {
			itemList.add(createRedisItemFromIdKey(id));
		}
		return itemList;
	}
	
	/*
	 * Gets all items from Redis having {@code lemma} as its lemma and {@code wordclass} 
	 * as its wordclass, parses them and creates {@link RedisItem}s from the found items.
	 * 
	 * @param lemma the lemma to search items for
	 * @param wordclass the wordclass of the item to search for
	 * @return a list of redisItems, or an empty list if nothing is found
	 */
	protected List<? extends Item> getItemsFor (final String lemma, final String wordclass) {
		List<RedisItem> itemList = new ArrayList<RedisItem>();
		Set<String> lemma_ids = jedis.smembers(MerkorRedisConstants.LEMMA_KEY_PREFIX_IS + lemma);
		
		for (String id : lemma_ids) {
			if (jedis.hget(id, "wordclass").equals(wordclass))
				itemList.add(createRedisItemFromIdKey(id));
		}
		return itemList;
	}
	
	/*
	 * Gets all items from Redis having {@code regex} matching their lemma. Parses them
	 * and creates {@link RedisItem}s from the found items.
	 * 
	 * @param regex a regular expression to match a lemma
	 * @return a list of redisItems, or an empty list if nothing is found
	 */
	protected List<? extends Item> getItemsMatching (final String regex) {
		Set<String> keys = jedis.keys(MerkorRedisConstants.LEMMA_KEY_PREFIX_IS + regex);
		List<RedisItem> itemList = new ArrayList<RedisItem>();
		for (String key : keys) {
			Set<String> id_keys = jedis.smembers(key);
			for (String id : id_keys)
				itemList.add(createRedisItemFromIdKey(id));
		}
		return itemList;	
	}

	protected RedisItem createRedisItemFromIdKey (final String id_key) {
		String item_id = id_key.substring(id_key.lastIndexOf('_') + 1, id_key.length());
		String item_lemma = jedis.hget(id_key, "lemma");
		String item_wordclass = jedis.hget(id_key, "wordclass");
		Long long_id = Long.parseLong(item_id);
		
		return new RedisItem(long_id, item_lemma, item_wordclass);
	}

	protected Item getItemFor (final Long id) {
		Item item = null;
		Set<String> keys = jedis.keys(MerkorRedisConstants.ID_KEY_PREFIX_IS + id);
		if (!keys.isEmpty()) {
			for (String key : keys)
				item = createRedisItemFromIdKey(key);		
		}
		return item;
	}
	
	protected String getRedisKeyForItem (final Item item) {
		return MerkorRedisConstants.ID_KEY_PREFIX_IS + item.getId();
	}
}
