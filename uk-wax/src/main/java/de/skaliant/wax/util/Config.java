package de.skaliant.wax.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class Config
{
	private Map<String, List<String>> entries = new HashMap<String, List<String>>();


	public static Config load(File file)
		throws Exception
	{
		Config conf = null;

		if (file.isFile())
		{
			InputStream in = null;
			
			try
			{
				in = new BufferedInputStream(new FileInputStream(file));
				conf = load(in);
			}
			finally
			{
				MiscUtils.close(in);
			}
		}
		else
		{
			conf = new Config();
		}
		return conf;
	}


	public static Config load(InputStream source)
		throws Exception
	{
		Config conf = new Config();
		Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(source);

		for (Element e : elements(dom.getElementsByTagName("entry")))
		{
			String id = e.getAttribute("id");

			if ((id != null) && (id.length() != 0))
			{
				if (e.hasAttribute("value"))
				{
					conf.setValue(id, e.getAttribute("value"));
				}
				else if (e.hasChildNodes())
				{
					List<String> ls = new ArrayList<String>(3);

					for (Element v : elements(e.getElementsByTagName("value")))
					{
						ls.add(v.getTextContent());
					}
					switch (ls.size())
					{
						case 0:
							conf.setValue(id, e.getTextContent());
							break;
						case 1:
							conf.setValue(id, ls.get(0));
							break;
						default:
							conf.setValues(id, ls);
							break;
					}
				}
			}
		}
		return conf;
	}


	private static List<Element> elements(NodeList nl)
	{
		List<Element> ls = Collections.emptyList();
		int len = nl.getLength();

		if (len > 0)
		{
			ls = new ArrayList<Element>(len);
			for (int i = 0; i < len; i++)
			{
				Node n = nl.item(i);

				if (n.getNodeType() == Node.ELEMENT_NODE)
				{
					ls.add((Element) n);
				}
			}
		}
		return ls;
	}
	
	
	public Set<String> getIds()
	{
		return entries.keySet();
	}
	
	
	public int getValueCount(String id)
	{
		List<String> ls = entries.get(id);
		int count = 0;
		
		if (ls != null)
		{
			count = ls.size();
		}
		return count;
	}


	private void setValues(String id, List<String> values)
	{
		entries.put(id, values);
	}


	private void setValue(String id, String value)
	{
		entries.put(id, Collections.singletonList(value));
	}

	
	public boolean hasEntry(String id)
	{
		return entries.containsKey(id);
	}


	public List<String> getValues(String id)
	{
		return entries.get(id);
	}


	public String getValue(String id)
	{
		return getValue(id, null);
	}


	public boolean getBooleanValue(String id, boolean def)
	{
		String s = getValue(id, null);
		
		if (s != null)
		{
			return Boolean.valueOf(s);
		}
		return def;
	}


	public boolean getBooleanValue(String id)
	{
		String s = getValue(id, null);
		
		if (s != null)
		{
			return Boolean.valueOf(s);
		}
		return false;
	}


	public int getIntValue(String id, int def)
	{
		String s = getValue(id, null);
		int v = def;
		
		if (s != null)
		{
			try
			{
				v = Integer.valueOf(s);
			}
			catch (NumberFormatException ex)
			{
				//
			}
		}
		return v;
	}


	public Integer getIntValue(String id)
	{
		String s = getValue(id, null);
		
		if (s != null)
		{
			return Integer.valueOf(s);
		}
		return null;
	}


	public String getValue(String id, String def)
	{
		List<String> ls = entries.get(id);
		String v = def;

		if ((ls != null) && !ls.isEmpty())
		{
			v = ls.get(0);
		}
		return v;
	}

}
