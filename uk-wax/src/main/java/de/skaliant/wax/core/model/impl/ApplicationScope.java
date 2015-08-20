package de.skaliant.wax.core.model.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;

import de.skaliant.wax.core.model.Scope;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class ApplicationScope
	implements Scope<ServletContext>
{
	private ServletContext app = null;
	
	
	public ApplicationScope(ServletContext app)
	{
		this.app = app;
	}

	
	public ServletContext getSource()
	{
		return app;
	}
	

	public Object get(String name)
	{
		return app.getAttribute(name);
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
			app.setAttribute(name, value);
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
			app.removeAttribute(name);
		}
	}


	public List<String> getNames()
	{
		List<String> ls = new ArrayList<String>();
		
		for (Enumeration<?> e = app.getAttributeNames(); e.hasMoreElements(); )
		{
			ls.add((String) e.nextElement());
		}
		return ls;
	}
}
