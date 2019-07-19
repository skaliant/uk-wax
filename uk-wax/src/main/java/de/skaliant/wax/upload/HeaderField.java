package de.skaliant.wax.upload;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Represents an HTTP header field consisting of a name, a value, and a
 * potential list or parameters.
 * 
 * @author Udo Kastilan
 */
public class HeaderField
{
	private Map<String, String> params = Collections.emptyMap();
	private String value = null;
	private String name = null;


	/**
	 * Adds a parameter.
	 * 
	 * @param name
	 * @param value
	 */
	public void addParam(String name, String value)
	{
		if (name == null)
		{
			throw new IllegalArgumentException("Parameter name must not be null");
		}
		if (params.isEmpty())
		{
			params = new HashMap<>(2);
		}
		params.put(name, value);
	}


	/**
	 * Checks whether a certain parameter is present, ignores (upper/lower) case.
	 * 
	 * @param name
	 *          Parameter name
	 * @return Present?
	 */
	public boolean hasParam(String name)
	{
		return getParam(name) != null;
	}


	/**
	 * Gets the value of a certain parameter, ignores (upper/lower) case. Returns
	 * null if the parameter is null or not present at all.
	 * 
	 * @param name
	 *          Parameter name
	 * @return Value, possibly null
	 */
	public String getParam(String name)
	{
		name = name.toLowerCase();
		for (Map.Entry<String, String> me : params.entrySet())
		{
			if (name.equals(me.getKey().toLowerCase()))
			{
				return me.getValue();
			}
		}
		return null;
	}


	/**
	 * Returns all parameters as a map. Warning: returns the original container
	 * map, however you should not rely on that.
	 * 
	 * @return All parameters as a map
	 */
	public Map<String, String> getParams()
	{
		return params;
	}


	/**
	 * Sets the map of parameters. Currently, it doesn't make a copy of the map
	 * but assigns the reference. Take this into consideration, but do not rely on
	 * this behaviour.
	 * 
	 * @param params
	 */
	public void setParams(Map<String, String> params)
	{
		this.params = params;
	}


	/**
	 * Returns the name of the header field. Original case is conserved, e.g.
	 * "Content-Type" or "content-type".
	 * 
	 * @return Header field name
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * Sets the name of the header field. Original case is conserved, e.g.
	 * "Content-Type" or "content-type".
	 * 
	 * @param name
	 *          Header field name
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 * Gets the header field value. Original case is conserved, e.g. "image/png".
	 * 
	 * @return Value
	 */
	public String getValue()
	{
		return value;
	}


	/**
	 * Sets the header field value. Original case is conserved, e.g. "image/png".
	 * 
	 * @param value
	 *          Value
	 */
	public void setValue(String value)
	{
		this.value = value;
	}


	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(name).append(": ").append(value);
		if (!params.isEmpty())
		{
			for (Map.Entry<String, String> me : params.entrySet())
			{
				String v = me.getValue();

				sb.append("; ").append(me.getKey()).append("=\"");
				for (int i = 0, len = v.length(); i < len; i++)
				{
					if (v.charAt(i) == '"')
					{
						sb.append('\\');
					}
					sb.append(v.charAt(i));
				}
				sb.append('"');
			}
		}
		return sb.toString();
	}
}
