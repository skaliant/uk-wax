package de.skaliant.wax.app;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.skaliant.wax.app.Config;


/**
 * 
 *
 * @author Udo Kastilan
 */
@RunWith(JUnit4.class)
public class ConfigTest
{
	@Test
	public void testFile()
	{
		try
		{
			Set<String> ids = new HashSet<String>(Arrays.asList("a", "b", "c", "d"));
			Config conf = Config.load(getClass().getResourceAsStream("/config.xml"));
			
			Assert.assertTrue(conf.hasEntry("a"));
			Assert.assertTrue(conf.hasEntry("b"));
			Assert.assertTrue(conf.hasEntry("c"));
			Assert.assertTrue(conf.hasEntry("d"));
			Assert.assertTrue(!conf.hasEntry("e"));
			
			Assert.assertEquals("Value", conf.getValue("a"));
			Assert.assertEquals(1, conf.getValueCount("a"));
			
			Assert.assertEquals("Value", conf.getValue("b"));
			Assert.assertEquals(1, conf.getValueCount("b"));
			
			Assert.assertEquals("Value", conf.getValue("c"));
			Assert.assertEquals(1, conf.getValueCount("c"));
			
			Assert.assertEquals("Value 1", conf.getValue("d"));
			Assert.assertEquals(2, conf.getValueCount("d"));
			
			Assert.assertEquals(0, conf.getValueCount("e"));
			
			Assert.assertEquals(ids, conf.getIds());
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
