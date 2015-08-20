package de.skaliant.wax.util;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class CrawlerBean
{
	public CrawlerBean getItem()
	{
		return new CrawlerBean();
	}

	public CrawlerBean getNull()
	{
		return null;
	}

	
	public Map<?, ?> getMap()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("item", new CrawlerBean());
		return map;
	}
}
