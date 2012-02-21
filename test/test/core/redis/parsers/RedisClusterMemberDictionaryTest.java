package test.core.redis.parsers;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import is.merkor.core.Cluster;
import is.merkor.core.ClusterMember;
import is.merkor.core.Item;
import is.merkor.core.redis.data.RedisItem;
import is.merkor.core.redis.dictionaries.RedisClusterMemberDictionary;

import org.junit.Before;
import org.junit.Test;

public class RedisClusterMemberDictionaryTest {
	
	RedisClusterMemberDictionary dict;
	
	
	@Before
	public void setUp() throws Exception {
		dict = new RedisClusterMemberDictionary();
	}

	@Test
	public void testGetClustersFor () {
		List<ClusterMember> list = dict.getClustersFor("bátur");
		
		assertTrue(list.size() > 0);
//		for (ClusterObject clObj : list) {
//			System.out.println(clObj.getItem());
//			System.out.println(clObj.getCluster().getName());
//		}
		list = dict.getClustersFor("maður");
		assertTrue(list.size() == 0);
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetClustersForEmpty () {
		List<ClusterMember> list = dict.getClustersFor("");
	}
	
	@Test
	public void testGetClustersForId () {
		List<Cluster> list = dict.getClustersFor(35273L);
		
		assertTrue(list.size() > 0);
		
//		for (Cluster cl : list) {
//			System.out.println(cl.getName());
//		}
	}
	@Test
	public void testGetClustersForZeroId () {
		List<Cluster> list = dict.getClustersFor(0L);
		assertTrue(list.size() == 0);
	}
	
	@Test
	public void testGetClustersMembersForClusterId () {
		List<? extends ClusterMember> list = dict.getClusterItemsForCluster(35L);
		
		assertTrue(list.size() > 0);
		
//		for (ClusterMember cl : list) {
//			System.out.println(cl.getValue() + " - " + cl.retrieveItem() + " - " + cl.retrieveCluster().getName());
//		}
	}
	@Test
	public void testGetClustersMembersForClusterIdZero () {
		List<? extends ClusterMember> list = dict.getClusterItemsForCluster(0L);
		
		assertTrue(list.size() == 0);
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetClustersMembersForClusterIdNull () {
		List<? extends ClusterMember> list = dict.getClusterItemsForCluster(null);
	}
	@Test
	public void testGetClustersMembersForItem () {
		List<? extends ClusterMember> list = dict.getClusterItemsForItem(new RedisItem(35273L, "bátur", "noun"));
		
		assertTrue(list.size() > 0);
		
//		for (ClusterMember cl : list) {
//			System.out.println(cl.getValue() + " - " + cl.retrieveItem() + " - " + cl.retrieveCluster().getName());
//		}
	}
	@Test
	public void testGetClustersMembersForItemNothingFound () {
		List<? extends ClusterMember> list = dict.getClusterItemsForItem(new RedisItem(0L, "bátur", "noun"));
		
		assertTrue(list.size() == 0);
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetClustersMembersForItemNull () {
		List<? extends ClusterMember> list = dict.getClusterItemsForItem(null);
	}
	
	@Test
	public void testGetDomainsFor () {
		Map<String, Item> map = dict.getDomainsFor("bátur");
		assertTrue(map.size() > 0);
//		for (String k : map.keySet())
//			System.out.println(k + " " + map.get(k));
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetDomainsForEmpty () {
		Map<String, Item> map = dict.getDomainsFor("");
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetDomainsForNull () {
		Map<String, Item> map = dict.getDomainsFor(null);
	}
	@Test
	public void testGetItemsForDomain () {
		List<Item> items = dict.getItemsForDomain("íþróttir_*");
		assertTrue(items.size() > 0);
//		for (Item it : items)
//			System.out.println(it);
//		
//		System.out.println("nr of items: " + items.size());
	}
	@Test
	public void testGetItemsForDomainEmpty () {
		List<Item> items = dict.getItemsForDomain("");
		assertTrue(items.size() == 0);
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetItemsForDomainNull () {
		List<Item> items = dict.getItemsForDomain(null);
	}
}
