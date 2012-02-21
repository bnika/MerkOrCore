package test.core.lemmatizer;


import static org.junit.Assert.*;
import is.merkor.core.lemmatizer.MerkorLemmatizer;

import org.junit.Before;
import org.junit.Test;

public class TestLemmatizer {
	
	MerkorLemmatizer lemmatizer;
	@Before
	public void setUp() throws Exception {
		lemmatizer = new MerkorLemmatizer();
	}
	
	@Test
	public void testStripString() {
		String str = "„Atli";
		String stripped = lemmatizer.stripWordform(str);
		assertEquals("Atli", stripped);
		
		str = "ævi.“";
		stripped = lemmatizer.stripWordform(str);
		assertEquals("ævi", stripped);
	}
	
	@Test
	public void testLemmatize () {
		lemmatizer.lemmatizeToFile("release/domainTest_18b_tagged.txt", "domainTest_18b_lemmatized.txt");
	}

}
