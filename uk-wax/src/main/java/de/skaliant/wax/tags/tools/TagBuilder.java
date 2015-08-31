package de.skaliant.wax.tags.tools;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class TagBuilder
{
	private Map<String, Object> attrs = new HashMap<String, Object>(5);
	private String name = null;
	private boolean xhtml = true;
	
	
	public TagBuilder(String name)
	{
		this(name, true);
	}
	
	
	public TagBuilder(String name, boolean xhtml)
	{
		this.xhtml = xhtml;
		this.name = name;
	}
	
	
	public void clear()
	{
		attrs.clear();
	}
	
	
	public boolean getBooleanNotNull(String name)
	{
		Boolean b = getBoolean(name);
		
		return b == null ? false : b.booleanValue();
	}
	
	
	public Boolean getBoolean(String name)
	{
		Object o = attrs.get(name);
		Boolean b = null;
		
		if (o instanceof Flag)
		{
			b = Boolean.TRUE;
		}
		else if (o instanceof BooleanAltValue)
		{
			b = ((BooleanAltValue) o).value;
		}
		else if (o instanceof Boolean)
		{
			b = (Boolean) o;
		}
		return b;
	}
	
	
	public String getString(String name)
	{
		Object o = attrs.get(name);
		String s = null;
		
		if (o instanceof String)
		{
			s = (String) o;
		}
		return s;
	}
	
	
	public int getIntegerNotNull(String name)
	{
		Integer i = getInteger(name);
		
		return i == null ? 0 : i.intValue();
	}
	
	
	public Integer getInteger(String name)
	{
		Object o = attrs.get(name);
		Integer i = null;
		
		if (o instanceof Integer)
		{
			i = (Integer) o;
		}
		return i;
	}
	
	
	public TagBuilder set(String name, Integer value)
	{
		if (value != null)
		{
			attrs.put(name, value);
		}
		return this;
	}
	
	
	public TagBuilder set(String name, String value)
	{
		if (value != null)
		{
			attrs.put(name, value);
		}
		return this;
	}
	
	
	public TagBuilder set(String name, Boolean value)
	{
		if (value != null)
		{
			attrs.put(name, value);
		}
		return this;
	}
	
	
	public TagBuilder set(String name, Boolean value, String ifTrue, String ifFalse)
	{
		if (value != null)
		{
			attrs.put(name, new BooleanAltValue(value.booleanValue() ? ifTrue : ifFalse, value.booleanValue()));
		}
		return this;
	}
	
	
	public TagBuilder set(String name)
	{
		attrs.put(name, new Flag());
		return this;
	}

	
	public String build()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append('<').append(name);
		for (Map.Entry<String, Object> me : attrs.entrySet())
		{
			Object v = me.getValue();
			String strValue = null;
			
			sb.append(' ').append(me.getKey());
			if (v instanceof Flag)
			{
				if (xhtml)
				{
					strValue = me.getKey();
				}
			}
			else if (v instanceof BooleanAltValue)
			{
				strValue = ((BooleanAltValue) v).alt;
			}
			else
			{
				strValue = v.toString();
			}
			if (strValue != null)
			{
				sb.append("=\"").append(enc(strValue)).append('"');
			}
		}
		sb.append('>');
		return sb.toString();
	}
	
	
	@Override
	public String toString()
	{
		return build();
	}
	
	
	private static String enc(String s)
	{
		if ((s != null) && (s.length() != 0))
		{
			StringBuilder sb = new StringBuilder();
			
			for (char c : s.toCharArray())
			{
				switch (c)
				{
					case '&':
						sb.append("&amp;");
						break;
					case '"':
						sb.append("&quot;");
						break;
					case '\'':
						sb.append("&apos;");
						break;
					case '<':
						sb.append("&lt;");
						break;
					case '>':
						sb.append("&gt;");
						break;
					default:
						sb.append(c);
				}
			}
			s = sb.toString();
		}
		return s;
	}

	
	private static class BooleanAltValue
	{
		private String alt = null;
		private boolean value = false;
		
		
		private BooleanAltValue(String alt, boolean value)
		{
			this.alt = alt;
			this.value = value;
		}
	}
	
	
	private static class Flag
	{
		//
	}
}
