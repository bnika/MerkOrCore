package test.core.util;


import static org.junit.Assert.*;
import is.merkor.core.util.RelationTypeMap;

import org.junit.Before;
import org.junit.Test;

public class RelationTypeMapTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testRelationTypeMap () {
		long id = RelationTypeMap.NAME_ID_MAP.get("og");
		assertEquals(7L, id);
		
		id = RelationTypeMap.NAME_ID_MAP.get("er eiginleiki");
		assertEquals(3L, id);
		
		id = RelationTypeMap.NAME_ID_MAP.get("er_eiginleiki");
		assertEquals(3L, id);
	}

}
