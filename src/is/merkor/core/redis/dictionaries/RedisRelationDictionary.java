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

import is.merkor.core.Item;
import is.merkor.core.Relation;
import is.merkor.core.RelationDictionary;
import is.merkor.core.RelationObject;
import is.merkor.core.RelationType;
import is.merkor.core.util.MerkorLogger;

/**
 * An implementation of the {@link RelationDictionary} interface using 
 * {@link RedisRelationParser} to connect to a Redis server containing MerkOr data.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 * 
 */
public class RedisRelationDictionary implements RelationDictionary {
	
	private RedisRelationParser parser;
	
	private static Logger logger;
	
	/**
	 * Constructs a new dictionary using default settings for
	 * Jedis 
	 */
	public RedisRelationDictionary () throws Exception {
		parser = new RedisRelationParser();
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
	}
	/**
	 * Constructs a new dictionary using {@code host} and {@code port}
	 * to instantiate the contained Jedis object.
	 */
	public RedisRelationDictionary (final String host, final int port) throws Exception {
		parser = new RedisRelationParser(host, port);
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
	}
	/**
	 * Constructs a new dictionary using {@code jedis}.
	 */
	public RedisRelationDictionary (final Jedis jedis) throws Exception {
		parser = new RedisRelationParser(jedis);
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisRelationParser.class);
	}
	
	/* (non-Javadoc)
	 * @see is.merkor.core.RelationDictionary#getRelationsFor(java.lang.String)
	 */
	@Override
	public List<RelationObject> getRelationsFor (final String lemma) {
		validateLemma(lemma);
		return parser.getRelationsFor(lemma);
	}

	/* (non-Javadoc)
	 * @see is.merkor.core.RelationDictionary#getRelationsFor(is.merkor.core.Item)
	 */
	@Override
	public List<? extends Relation> getRelationsFor (final Item item) {
		validateObject(item, "item");
		return parser.getRelationsFor(item);
	}
	
	public List<Relation> getMostRelatedWords (final Item item, final int nrOfWords) {
		validateObject(item, "item");
		validatePositiveNumber(nrOfWords, "nrOfWords");
		return parser.getMostRelatedWords (item, nrOfWords);
	}
	
	public List<RelationObject> getMostRelatedWords (final String lemma, final int nrOfWords) {
		validateLemma(lemma);
		validatePositiveNumber(nrOfWords, "nrOfWords");
		return parser.getMostRelatedWords (lemma, nrOfWords);
	}

	/* (non-Javadoc)
	 * @see is.merkor.core.RelationDictionary#getRelationsFor(java.lang.String, java.lang.String)
	 */
	@Override
	public List<RelationObject> getRelationsFor (final String lemma1, final String lemma2) {
		validateLemma(lemma1);
		validateLemma(lemma2);
		return parser.getRelationsFor(lemma1, lemma2);
	}

	/* (non-Javadoc)
	 * @see is.merkor.core.RelationDictionary#getRelationsFor(is.merkor.core.Item, is.merkor.core.Item)
	 */
	@Override
	public List<Relation> getRelationsFor (final Item item1, final Item item2) {
		validateObject(item1, "item1");
		validateObject(item2, "item2");
		return parser.getRelationsFor(item1, item2);
	}

	/* (non-Javadoc)
	 * @see is.merkor.core.RelationDictionary#getRelationsHavingLeft(java.lang.String, is.merkor.core.RelationType)
	 */
	@Override
	public List<RelationObject> getRelationsHavingLeft (final String lemma, final RelationType type) {
		validateLemma(lemma);
		validateObject(type, "type");
		return parser.getRelationsHavingLeft(lemma, type);
	}

	/* (non-Javadoc)
	 * @see is.merkor.core.RelationDictionary#getRelationsHavingLeft(is.merkor.core.Item, is.merkor.core.RelationType)
	 */
	@Override
	public List<Relation> getRelationsHavingLeft (final Item item, final RelationType type) {
		validateObject(item, "item");
		validateObject(type, "type");
		return parser.getRelationsHavingLeft(item, type);
	}

	/* (non-Javadoc)
	 * @see is.merkor.core.RelationDictionary#getRelationsHavingRight(java.lang.String, is.merkor.core.RelationType)
	 */
	@Override
	public List<RelationObject> getRelationsHavingRight (final String lemma, final RelationType type) {
		validateLemma(lemma);
		validateObject(type, "type");
		return parser.getRelationsHavingRight(lemma, type);
	}

	/* (non-Javadoc)
	 * @see is.merkor.core.RelationDictionary#getRelationsHavingRight(is.merkor.core.Item, is.merkor.core.RelationType)
	 */
	@Override
	public List<Relation> getRelationsHavingRight(final Item item, final RelationType type) {
		return parser.getRelationsHavingRight(item, type);
	}
	/**
	 * Returns a list of the 'nrOfRelations' top relations having the relationType type.
	 * @throws IllegalArgumentException if param 'type' is {@code null} or param 'nrOfRelations' 
	 * is not positive.
	 */
	public List<Relation> getMostRelated (RelationType type, int nrOfRelations) {
		validatePositiveNumber(nrOfRelations, "nrOfRelations");
		validateObject(type, "type");
		return parser.getMostRelated(type, nrOfRelations);
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
	private void validatePositiveNumber (final int number, final String paramName) {
		if (number <= 0) {
			IllegalArgumentException e = new IllegalArgumentException();
			logger.error("param " + paramName + " must be positive!", e);
			throw e;
		}
	}

}
