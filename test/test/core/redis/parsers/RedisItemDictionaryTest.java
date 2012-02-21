package test.core.redis.parsers;

import static org.junit.Assert.*;

import java.util.List;

import is.merkor.core.Item;
import is.merkor.core.redis.data.RedisItem;
import is.merkor.core.redis.dictionaries.RedisItemDictionary;

import org.junit.Before;
import org.junit.Test;

public class RedisItemDictionaryTest {
	RedisItemDictionary dict;
	
	@Before
	public void setUp() throws Exception {
		dict = new RedisItemDictionary();
	}

//	@Test
//	public void testCollectAllItems() {
//		List<? extends Item> list = parser.collectAllItems();
//		
//		assertTrue(list.size() > 0);
//		//System.out.println(list.size());
//		for (Item it : list) {
//			if (it.getId().equals(87571)) {
//				assertTrue(it.getLemma().equals("maður"));
//				break;
//			}
//		}
//	}
	@Test
	public void testGetItemsFor () {
		List<? extends Item> list = dict.getItemsFor("dýr");
		assertTrue(list.size() == 2);
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetItemsForEmpty () {
		List<? extends Item> list = dict.getItemsFor("");
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetItemsForNull () {
		List<? extends Item> list = dict.getItemsFor(null);
	}
	@Test
	public void testGetItemsForStringAndWordclass () {
		List<? extends Item> list = dict.getItemsFor("dýr", "noun");
		assertTrue(list.size() == 1);
		list = dict.getItemsFor("dýr", "adjective");
		assertTrue(list.size() == 1);
		list = dict.getItemsFor("kaupa", "verb");
		assertTrue(list.size() == 1);
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetItemsForStringAndWordclassEmpty () {
		List<? extends Item> list = dict.getItemsFor("", "noun");
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetItemsForStringAndWordclassNonvalid () {
		List<? extends Item> list = dict.getItemsFor("dýr", "nouns");
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetItemsForStringAndWordclassNull () {
		List<? extends Item> list = dict.getItemsFor("dýr", null);
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetItemsForStringAndWordclassNull2 () {
		List<? extends Item> list = dict.getItemsFor(null, "noun");
	}
	@Test
	public void testGetItemsMatching () {
		List<? extends Item> list = dict.getItemsMatching("mamm?");
		assertTrue(list.size() == 1);
		//System.out.println(list);
		
		list = dict.getItemsMatching("brauð*");
		assertTrue(list.size() > 1);
		//System.out.println(list);
		
		list = dict.getItemsMatching("quatsch*");
		assertTrue(list.isEmpty());
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetItemsMatchingEmpty () {
		List<? extends Item> list = dict.getItemsMatching("");
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetItemsMatchingNull () {
		List<? extends Item> list = dict.getItemsMatching(null);
	}

}
