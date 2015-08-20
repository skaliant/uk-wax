package de.skaliant.wax.util;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class BeanTest
{
	@Test
	public void crawlNotExistingDirect()
	{
		Assert.assertNull(Bean.resolve(new CrawlerBean(), "nope"));
	}

	
	@Test
	public void crawlNotExistingIndirectEnding()
	{
		Assert.assertNull(Bean.resolve(new CrawlerBean(), "item.nope"));
	}

	
	@Test
	public void crawlNotExistingIndirectStarting()
	{
		Assert.assertNull(Bean.resolve(new CrawlerBean(), "nope.item"));
	}
	
	
	@Test
	public void crawlNullDirect()
	{
		Assert.assertNull(Bean.resolve(new CrawlerBean(), "null"));
	}

	
	@Test
	public void crawlNullBetween()
	{
		Assert.assertNull(Bean.resolve(new CrawlerBean(), "item.null.item"));
	}

	
	@Test
	public void crawlNullEnding()
	{
		Assert.assertNull(Bean.resolve(new CrawlerBean(), "item.item.null"));
	}

	
	@Test
	public void crawlMapNull()
	{
		Assert.assertNull(Bean.resolve(new CrawlerBean(), "item.map.item.null"));
	}

	
	@Test
	public void crawlMapNotNull()
	{
		Assert.assertEquals(CrawlerBean.class, Bean.resolve(new CrawlerBean(), "item.map.item").getClass());
	}

	
	@Test
	public void crawlOne()
	{
		Assert.assertEquals(CrawlerBean.class, Bean.resolve(new CrawlerBean(), "item").getClass());
	}

	
	@Test
	public void crawlTwo()
	{
		Assert.assertEquals(CrawlerBean.class, Bean.resolve(new CrawlerBean(), "item.item").getClass());
	}

	
	@Test
	public void crawlThree()
	{
		Assert.assertEquals(CrawlerBean.class, Bean.resolve(new CrawlerBean(), "item.item.item").getClass());
	}
	
	
	@Test
	public void findAny()
	{
		Assert.assertEquals(
				MiscUtils.createSet("readonly", "writeonly", "readwrite", "class"),
				Bean.map(Bean.getProperties(AccessibleBean.class, Bean.Accessibility.ANY))
						.keySet());
	}


	@Test
	public void findReadOnly()
	{
		Assert.assertEquals(
				MiscUtils.createSet("readonly", "class"),
				Bean.map(
						Bean.getProperties(AccessibleBean.class, Bean.Accessibility.READ_ONLY))
						.keySet());
	}


	@Test
	public void findReadable()
	{
		Assert.assertEquals(
				MiscUtils.createSet("readonly", "readwrite", "class"),
				Bean.map(
						Bean.getProperties(AccessibleBean.class, Bean.Accessibility.READABLE))
						.keySet());
	}


	@Test
	public void findWriteable()
	{
		Assert.assertEquals(
				MiscUtils.createSet("writeonly", "readwrite"),
				Bean.map(
						Bean.getProperties(AccessibleBean.class, Bean.Accessibility.WRITEABLE))
						.keySet());
	}


	@Test
	public void findReadWrite()
	{
		Assert
				.assertEquals(
						MiscUtils.createSet("readwrite"),
						Bean.map(
								Bean.getProperties(AccessibleBean.class,
										Bean.Accessibility.READ_WRITE)).keySet());
	}


	@Test
	public void findWriteOnly()
	{
		Assert
				.assertEquals(
						MiscUtils.createSet("writeonly"),
						Bean.map(
								Bean.getProperties(AccessibleBean.class,
										Bean.Accessibility.WRITE_ONLY)).keySet());
	}
}
