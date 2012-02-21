package test.core.redis.parsers;

import static org.junit.Assert.*;

import java.util.List;

import is.merkor.core.Relation;
import is.merkor.core.RelationObject;
import is.merkor.core.redis.data.RedisItem;
import is.merkor.core.redis.data.RedisRelationType;
import is.merkor.core.redis.dictionaries.RedisRelationDictionary;
import is.merkor.core.redis.dictionaries.RedisRelationParser;

import org.junit.Before;
import org.junit.Test;

public class RedisRelationDictionaryTest {
	
	RedisRelationDictionary dict;
	
	@Before
	public void setUp() throws Exception {
		dict = new RedisRelationDictionary();
	}

	@Test
	public void testGetRelationsFor() {
		List<RelationObject> list = dict.getRelationsFor("skerpa");
		assertTrue(list.size() == 2);
//		for (RelationObject relObj : list) {
//			System.out.println(relObj.getItem());
//			for(Relation rel: relObj.getRelation())
//				System.out.println(rel);
//		}
	}
	@Test (expected = IllegalArgumentException.class)
	public void testGetRelationsForEmpty() {
		List<RelationObject> list = dict.getRelationsFor("");
	}
	@Test
	public void testGetRelationsForNonsense () {
		List<RelationObject> list = dict.getRelationsFor("sksektj");
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testGetRelationsForTwoLemma() {
		List<RelationObject> list = dict.getRelationsFor("skerpa", "birta");
		assertTrue(list.size() == 1);
//		for (RelationObject relObj : list) {
//			System.out.println(relObj.getItem());
//			for(Relation rel: relObj.getRelation())
//				System.out.println(rel);
//		}
	}
	
	@Test
	public void testGetRelationsForItem() {
		RedisItem item = new RedisItem(81340L, "skerpa", "noun");
		List<? extends Relation> list = dict.getRelationsFor(item);
		assertTrue(list.size() > 0);
	
//		for(Relation rel: list)
//			System.out.println(rel);
		
	}
	@Test
	public void testGetRelationsForTwoItems() {
		RedisItem item1 = new RedisItem(81340L, "skerpa", "noun");
		RedisItem item2 = new RedisItem(109936L, "birta", "noun");
		List<? extends Relation> list = dict.getRelationsFor(item1, item2);
		assertTrue(list.size() > 0);
	
//		for(Relation rel: list)
//			System.out.println(rel);
		
	}
	
	@Test
	public void testGetRelationsHavingLeft() {
		RedisRelationType type = new RedisRelationType(7L, "og", "og");
		
		List<RelationObject> list = dict.getRelationsHavingLeft("birta", type);
		assertTrue(list.size() > 0);
//		for (RelationObject relObj : list) {
//			System.out.println(relObj.getItem());
//			for(Relation rel: relObj.getRelation())
//				System.out.println(rel);
//		}
		
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetRelationsHavingLeftEmpty() {
		RedisRelationType type = new RedisRelationType(7L, "og", "og");
		
		List<RelationObject> list = dict.getRelationsHavingLeft("", type);
	}
	@Test 
	public void testGetRelationsHavingLeftItem () {
		RedisRelationType type = new RedisRelationType(7L, "og", "og");
		RedisItem item = new RedisItem(109936L, "birta", "noun");
		List<Relation> list = dict.getRelationsHavingLeft(item, type);
		
//		for(Relation rel: list)
//			System.out.println(rel);
	}
	
	@Test
	public void testGetRelationsHavingRight() {
		RedisRelationType type = new RedisRelationType(7L, "og", "og");
		
		List<RelationObject> list = dict.getRelationsHavingRight("birta", type);
		assertTrue(list.size() > 0);
		
//		for (RelationObject relObj : list) {
//			System.out.println(relObj.getItem());
//			for(Relation rel: relObj.getRelation())
//				System.out.println(rel);
//		}
		
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetRelationsHavingRightEmpty() {
		RedisRelationType type = new RedisRelationType(7L, "og", "og");
		
		List<RelationObject> list = dict.getRelationsHavingRight("", type);
	}
	@Test 
	public void testGetRelationsHavingRightItem () {
		RedisRelationType type = new RedisRelationType(7L, "og", "og");
		RedisItem item = new RedisItem(109936L, "birta", "noun");
		List<Relation> list = dict.getRelationsHavingRight(item, type);
		assertTrue(list.size() > 0);
		
//		for(Relation rel: list)
//			System.out.println(rel);
	}
	@Test
	public void testGetMostRelatedForLemma () {
		List<RelationObject> list = dict.getMostRelatedWords("skúr", 20);
		
		assertTrue(!list.isEmpty());
		for (RelationObject obj : list) {
//			System.out.println(obj.getItem());
			assertTrue(obj.getRelation().size() <= 20);
		}
	}
	@Test (expected = IllegalArgumentException.class)
	public void testGetMostRelatedForLemmaIllArg () {
		List<RelationObject> list = dict.getMostRelatedWords("skúr", 0);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testGetMostRelatedForLemmaEmpty () {
		List<RelationObject> list = dict.getMostRelatedWords("", 10);
	}
	@Test 
	public void testGetMostRelated () {
		RedisRelationType type = new RedisRelationType(7L, "og", "og");
		List<Relation> list = dict.getMostRelated(type, 10);
		assertTrue(list.size() == 10);
		
//		for(Relation rel: list)
//			System.out.println(rel);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testGetMostRelatedNull () {
		RedisRelationType type = new RedisRelationType(7L, "og", "og");
		List<Relation> list = dict.getMostRelated(null, 10);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testGetMostRelatedNegative () {
		RedisRelationType type = new RedisRelationType(7L, "og", "og");
		List<Relation> list = dict.getMostRelated(type, -10);
	}


}
