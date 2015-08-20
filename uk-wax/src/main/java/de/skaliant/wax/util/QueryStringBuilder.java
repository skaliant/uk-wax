package de.skaliant.wax.util;

import java.net.URLEncoder;
import java.nio.charset.Charset;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class QueryStringBuilder
{
	private StringBuilder sb = new StringBuilder();
	private Charset encoding = Charset.forName("utf-8");
	private char sep = '?';
	
	
	public QueryStringBuilder(String query, Charset encoding)
	{
		this.encoding = encoding;
		for (String s : MiscUtils.split(query, "?&"))
		{
			sb.append(sep).append(s);
			sep = '&';
		}
	}
	
	
	public QueryStringBuilder(Charset encoding)
	{
		this.encoding = encoding;
	}
	
	
	public QueryStringBuilder(String query)
	{
		this(query, Charset.forName("utf-8"));
	}
	
	
	public QueryStringBuilder()
	{
		//
	}

	
	public QueryStringBuilder append(String name, Object... values)
	{
		try
		{
			if (values.length != 0)
			{
				for (Object v : values)
				{
					sb.append(URLEncoder.encode(name, encoding.name())).append('=');
					if (v != null)
					{
						sb.append(URLEncoder.encode(v.toString(), encoding.name()));
					}
					sep = '&';
				}
			}
			else
			{
				sb.append(sep).append(URLEncoder.encode(name, encoding.name()));
				sep = '&';
			}
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
		return this;
	}
	
	
	@Override
	public String toString()
	{
		return sb.toString();
	}
}
