package de.skaliant.wax.core.model.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import de.skaliant.wax.core.model.Scope;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class RequestScope
	implements Scope<HttpServletRequest>
{
	private HttpServletRequest request = null;
	
	
	public RequestScope(HttpServletRequest request)
	{
		this.request = request;
	}
	
	
	public HttpServletRequest getSource()
	{
		return request;
	}
	

	public Object get(String name)
	{
		return request.getAttribute(name);
	}

	
	public <T> T get(String name, Class<T> type)
	{
		return type.cast(get(name));
	}
	

	public void set(String name, Object value)
	{
		if (value == null)
		{
			remove(name);
		}
		else
		{
			request.setAttribute(name, value);
		}
	}

	
	public boolean has(String name)
	{
		return get(name) != null;
	}

	
	public void remove(String name)
	{
		if (has(name))
		{
			request.removeAttribute(name);
		}
	}


	public List<String> getNames()
	{
		List<String> ls = new ArrayList<String>();
		
		for (Enumeration<?> e = request.getAttributeNames(); e.hasMoreElements(); )
		{
			ls.add((String) e.nextElement());
		}
		return ls;
	}
}
