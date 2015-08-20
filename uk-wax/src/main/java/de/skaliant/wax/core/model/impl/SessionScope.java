package de.skaliant.wax.core.model.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpSession;

import de.skaliant.wax.core.model.Scope;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class SessionScope
	implements Scope<HttpSession>
{
	private HttpSession sess = null;
	
	
	public SessionScope(HttpSession sess)
	{
		this.sess = sess;
	}

	
	public HttpSession getSource()
	{
		return sess;
	}
	

	public Object get(String name)
	{
		return sess.getAttribute(name);
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
			sess.setAttribute(name, value);
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
			sess.removeAttribute(name);
		}
	}


	public List<String> getNames()
	{
		List<String> ls = new ArrayList<String>();
		
		for (Enumeration<?> e = sess.getAttributeNames(); e.hasMoreElements(); )
		{
			ls.add((String) e.nextElement());
		}
		return ls;
	}
}
