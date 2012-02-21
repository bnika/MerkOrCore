/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core.redis.dictionaries;

import is.merkor.core.Item;
import is.merkor.core.Relation;
import is.merkor.core.RelationObject;
import is.merkor.core.RelationType;
import is.merkor.core.redis.data.RedisItem;
import is.merkor.core.redis.data.RedisPair;
import is.merkor.core.redis.data.RedisRelation;
import is.merkor.core.redis.data.RedisRelationType;
import is.merkor.core.util.MerkorLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

/**
 * Provides methods to parse keys and values from MerkOr data on a Redis server,
 * and instantiates {@link RedisItem}s from the parsed information.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */

public class RedisRelationParser {

	private Jedis jedis;
	private RedisItemParser itemParser;
	
	private static Logger logger;
	
	/*
	 * Constructs a new dictionary using default settings for
	 * Jedis (localhost).
	 */
	protected RedisRelationParser () throws Exception {
		jedis = new Jedis("localhost");
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
		if (!RedisConnectionCheck.checkConnection(jedis)) {
			Exception e = new Exception();
			logger.error("Jedis connection for \"localhost\" failed!");
			throw e;
		}
		itemParser = new RedisItemParser();
	}
	/*
	 * Constructs a new parser using {@code host} and {@code port}
	 * to instantiate the contained Jedis object.
	 */
	protected RedisRelationParser (final String host, final int port) throws Exception {
		jedis = new Jedis(host, port);
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
		if (!RedisConnectionCheck.checkConnection(jedis)) {
			Exception e = new Exception();
			logger.error("Jedis connection for host " + host + " and port " + port + " failed!");
			throw e;
		}
		itemParser = new RedisItemParser(host, port);
	}
	/*
	 * Constructs a new parser using {@code jedis}.
	 */
	protected RedisRelationParser (final Jedis jedis) throws Exception {
		this.jedis = jedis;
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
		if (!RedisConnectionCheck.checkConnection(jedis)) {
			Exception e = new Exception();
			logger.error("Jedis connection with given Jedis object failed!");
			throw e;
		}
		itemParser = new RedisItemParser(jedis);
		
	}
	
	/*
	 * Returns a list of relationObjects for the item(s) parameter lemma
	 * belongs to. A relationObject is created for each item.
	 * Therefore, in most cases, there will only be one element in the returned list.
	 */
	protected List<RelationObject> getRelationsFor (final String lemma) {
		List<RelationObject> list = new ArrayList<RelationObject>();
		List<? extends Item> itemList = itemParser.getItemsFor(lemma);
		for (Item item : itemList) {
			RelationObject relObj = createRelationObjectForItem(item);
			if (null != relObj)
				list.add(relObj);
		}
		return list;
	}
	/*
	 * Returns a list of relationObjects containing a relation between items
	 * lemma1 and lemma2 belong to. 
	 */
	protected List<RelationObject> getRelationsFor (final String lemma1, final String lemma2) {
		List<RelationObject> list = new ArrayList<RelationObject>();
		
		List<? extends Item> itemList1 = itemParser.getItemsFor(lemma1);
		List<? extends Item> itemList2 = itemParser.getItemsFor(lemma2);
		
		for (Item item1 : itemList1) {
			List<? extends Relation> rels1 = getRelationsFor(item1);
			for (Item item2 : itemList2) {
				List<? extends Relation> rels2 = getRelationsFor(item2);
				rels2.retainAll(rels1);
				if (!rels2.isEmpty()) {
					// to be able to show the item for the lemma with more than one
					// item, this item is choosen as the item of the relationObject
					if (itemList1.size() > 1 && itemList2.size() == 1)
						list.add(new RelationObject(item1, rels2));
					else 
						list.add(new RelationObject(item2, rels2));
				}	
			}
		}
		return list;
	}
	/*
	 * Returns a list of relations that contain parameter item, sorted by
	 * confidence score: the top elements in the list contain the relations
	 * with the most related words of the item.
	 */
	protected List<Relation> getRelationsFor (final Item item) {
		Set<String> relIds = jedis.zrevrange(MerkorRedisConstants.SORTED_REL_SET_ID + MerkorRedisConstants.ID_KEY_PREFIX_IS + item.getId(), 0, -1);
		List<Relation> relations = new ArrayList<Relation>();
		
		for (String relId : relIds) {
			Relation rel = createRelation(relId);
			if (null != rel)
				relations.add(rel);
		}
		return relations;
	}
	/*
	 * Returns a list of relations containing 'nrOfWords' top relations for 
	 * parameter item.
	 */
	protected List<Relation> getMostRelatedWords (final Item item, final int nrOfWords) {
		List<Relation> relations = new ArrayList<Relation>();
		int max = 1;
		// counting in redis starts by zero
		if (nrOfWords > 1)
			max = nrOfWords - 1;
		// get the reversed sorted set of relations for 'item'
		Set<String> relIds = jedis.zrevrange(
				MerkorRedisConstants.SORTED_REL_SET_ID + MerkorRedisConstants.ID_KEY_PREFIX_IS + item.getId(), 0, max);
		
		for (String relId : relIds) {
			Relation rel = createRelation(relId);
			if (null != rel)
				relations.add(rel);
		}
		return relations;
	}
	/*
	 * Returns a list of relationObjects, each containing the 'nrOfWords' most related
	 * relations for the corresponding item of parameter lemma.
	 */
	protected List<RelationObject> getMostRelatedWords (final String lemma, final int nrOfWords) {
		List<RelationObject> resultObjects = new ArrayList<RelationObject>();
		List<RedisItem> itemList = (List<RedisItem>)itemParser.getItemsFor(lemma);
		
		for (Item item : itemList) {
			List<Relation> rels = getMostRelatedWords(item, nrOfWords);
			if (!rels.isEmpty()) {
				RelationObject relObj = new RelationObject(item, rels);
				resultObjects.add(relObj);
			}
		}
		return resultObjects;
	}
	/*
	 * Returns a list of relations (should only be one!) containing
	 * item1 and item2.
	 */
	protected List<Relation> getRelationsFor (final Item item1, final Item item2) {
		List<Relation> rels1 = getRelationsFor(item1);
		List<Relation> rels2 = getRelationsFor(item2);
		rels1.retainAll(rels2);
		
		return rels1;
	}
	/*
	 * Returns a list of relationObjects, each containing an item for parameter lemma and 
	 * a list of relations where this item is the left element.
	 */
	protected List<RelationObject> getRelationsHavingLeft (final String lemma, final RelationType type) {
		return getOneSidedRelations(lemma, type, "LEFT");
	}
	/*
	 * Returns a list of relationObjects, each containing an item for parameter lemma and 
	 * a list of relations where this item is the right element and the relationType is 'type'.
	 */
	protected List<RelationObject> getRelationsHavingRight (final String lemma, final RelationType type) {
		return getOneSidedRelations(lemma, type, "RIGHT");
	}
	
	protected List<Relation> getRelationsHavingLeft (final Item item, final RelationType type) {
		return getOneSidedRelations(item, type, "LEFT");	
	}
	protected List<Relation> getRelationsHavingRight (final Item item, final RelationType type) {
		return getOneSidedRelations(item, type, "RIGHT");
	}
	/*
	 * Returns a list of 'nrOfRels' relations of the certain type 'type'
	 */
	protected List<Relation> getMostRelated (final RelationType type, final int nrOfRels) {
		List<Relation> relations = new ArrayList<Relation>();
		int max = 1;
		if (nrOfRels > 1)
			max = nrOfRels - 1;
		Set<String> relIds = jedis.zrevrange(MerkorRedisConstants.REL_TYPE_TOP_RELATIONS + type.getId(), 0, max);
		for (String relId : relIds) {
			Relation rel = createRelation(relId);
			if (null != rel)
				relations.add(rel);
		}
		return relations;
	}
	
	private RelationObject createRelationObjectForItem (final Item item) {
		if (getRelationsFor(item).size() > 0)
			return new RelationObject(item, getRelationsFor(item));
		else
			return null;
	}
	
	private Relation createRelation (final String relId) {
		// parse
		String fromString = jedis.hget(relId, "from_item");
		String toString = jedis.hget(relId, "to_item");
		String relation = jedis.hget(relId, "relation");
		if (null == relation || null == fromString || null == toString) {
			logger.warn("parsing for relation '" + relId + "' failed, null values!");
			return null;
		}
		Double confidence = Double.parseDouble(jedis.hget(relId, "score"));
		// create RedisPair
		RedisItem fromItem = itemParser.createRedisItemFromIdKey(fromString);
		RedisItem toItem = itemParser.createRedisItemFromIdKey(toString);
		RedisPair pair = new RedisPair(fromItem, toItem);
		// create relationType
		Long typeId = getTypeId(relation, fromItem);
		RelationType type = new RedisRelationType(typeId, relation, relation);
		
		return new RedisRelation(pair, type, confidence);
		
	}
	private Long getTypeId (final String relation, final Item item) {
		Long id = 0L;
		// both coord_noun (id 7) and coord_adj (id 8) have
		// the description "og"
		if (relation.equals("og")) {
			if (item.getWordclass().equals("noun"))
				id = 7L;
			else
				id = 8L;
		}
		else {
			try {
				id = Long.parseLong(jedis.get(MerkorRedisConstants.REL_TYPE_PREFIX + relation));
			} catch (NumberFormatException e) {
				logger.warn(e.getMessage());
			}
		}
		return id;
	}
	/*
	 * Returns a list of relationObjects, each containing an item for parameter lemma and 
	 * a list of relations where this item is the left or right element (depending on 'side')
	 * and the relationType is 'type'.
	 */
	private List<RelationObject> getOneSidedRelations (final String lemma, final RelationType type, final String side) {
		List<RelationObject> resultObjects = new ArrayList<RelationObject>();
		List<RelationObject> relObjects = getRelationsFor (lemma);
		
		for (RelationObject relObj : relObjects) {
			List<Relation> relations = new ArrayList<Relation>();
			for (Relation rel : relObj.getRelation()) {
				if (side.equals("LEFT")) {
					//compare 'from'
					if (rel.getPair().getFrom().getLemma().equals(lemma) && rel.getType().getId().equals(type.getId()))
						relations.add(rel);
				}
				else {
					//compare 'to'
					if (rel.getPair().getTo().getLemma().equals(lemma) && rel.getType().getId().equals(type.getId()))
						relations.add(rel);
				}
			}
			if (!relations.isEmpty())
				resultObjects.add(new RelationObject(relObj.getItem(), relations));
		}
		return resultObjects;
	}
	/*
	 * Returns a list of relations, each containing 'item' and a list of relations 
	 * where this item is the left or right element (depending on 'side')
	 * and the relationType is 'type'.
	 */
	private List<Relation> getOneSidedRelations (final Item item, final RelationType type, final String side) {
		List<Relation> relList = getRelationsFor(item);
		List<Relation> relations = new ArrayList<Relation>();
		for (Relation rel : relList) {
			if (side.equals("LEFT")) {
				//compare 'from' item and type
				if (rel.getPair().getFrom().equals(item) && rel.getType().getId().equals(type.getId()))
					relations.add(rel);
			}
			else {
				//compare 'to' item and type
				if (rel.getPair().getTo().equals(item) && rel.getType().getId().equals(type.getId()))
					relations.add(rel);
			}	
		}
		return relations;
	}	
}
