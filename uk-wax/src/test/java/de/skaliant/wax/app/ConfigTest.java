package de.skaliant.wax.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class ConfigTest {
	@Test
	public void testFile() {
		try {
			Set<String> ids = new HashSet<String>(Arrays.asList("a", "b", "c", "d"));
			Config conf = Config.load(getClass().getResourceAsStream("/config.xml"));

			assertTrue(conf.hasEntry("a"));
			assertTrue(conf.hasEntry("b"));
			assertTrue(conf.hasEntry("c"));
			assertTrue(conf.hasEntry("d"));
			assertTrue(!conf.hasEntry("e"));

			assertEquals("Value", conf.getValue("a"));
			assertEquals(1, conf.getValueCount("a"));

			assertEquals("Value", conf.getValue("b"));
			assertEquals(1, conf.getValueCount("b"));

			assertEquals("Value", conf.getValue("c"));
			assertEquals(1, conf.getValueCount("c"));

			assertEquals("Value 1", conf.getValue("d"));
			assertEquals(2, conf.getValueCount("d"));

			assertEquals(0, conf.getValueCount("e"));

			assertEquals(ids, conf.getIds());
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
