package test.core.redis.parsers;

import static org.junit.Assert.*;

import java.util.List;

import is.merkor.core.Cluster;
import is.merkor.core.redis.dictionaries.RedisClusterDictionary;

import org.junit.Before;
import org.junit.Test;

public class RedisClusterDictionaryTest {
	
	RedisClusterDictionary dict;
	
	@Before
	public void setUp() throws Exception {
		dict = new RedisClusterDictionary();
	}

	@Test
	public void testGetClustersMatching() {
		List<? extends Cluster> list = dict.getClustersMatching("efna*");
		assertTrue(list.size() > 0);
		
//		for (Cluster c : list)
//			System.out.println(c.getName());
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetClustersMatchingNull() {
		List<? extends Cluster> list = dict.getClustersMatching(null);
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetClustersMatchingEmpty() {
		List<? extends Cluster> list = dict.getClustersMatching("");
	}
	
	@Test
	public void testGetClusterById() {
		Cluster cluster = dict.getClusterById(276L);
		assertTrue(cluster != null);
		//System.out.println(cluster.getName());
	}
	@Test (expected=IllegalArgumentException.class)
	public void testGetClusterByNullId() {
		Cluster cluster = dict.getClusterById(null);
		assertTrue(cluster != null);
		//System.out.println(cluster.getName());
	}
	
	@Test
	public void testGetAllClusterNames() {
		List<String> list = dict.getAllClusterNames();
		assertTrue(list.size() > 10);
//		for (String s : list)
//			System.out.println(s);
	}

}
