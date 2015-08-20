package de.skaliant.wax.core.model;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import de.skaliant.wax.util.MiscUtils;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class RequestAttributeRedirectStore
{
	private Map<String, Object> attrs = null;
	
	
	public RequestAttributeRedirectStore()
	{
		this.attrs = new HashMap<String, Object>(0);
	}
	
	
	public RequestAttributeRedirectStore(Map<String, Object> attrs)
	{
		this.attrs = attrs;
	}
	
	
	public Map<String, Object> getAttrs()
	{
		return attrs;
	}


	public void setAttrs(Map<String, Object> attrs)
	{
		this.attrs = attrs;
	}


	/**
	 * 
	 * @param req
	 */
	public static void save(HttpServletRequest req)
	{
		Map<String, Object> attrs = new HashMap<String, Object>();
		
		for (String n : MiscUtils.list(req.getAttributeNames(), String.class))
		{
			attrs.put(n, req.getAttribute(n));
		}
		if (!attrs.isEmpty())
		{
			req.getSession().setAttribute(name(), new RequestAttributeRedirectStore(attrs));
		}
	}
	

	/**
	 * Restore the attribute state.
	 * 
	 * @param req
	 */
	public static void restore(HttpServletRequest req)
	{
		HttpSession sess = req.getSession(false);
		String name = name();
		
		if ((sess != null) && (sess.getAttribute(name) != null))
		{
			RequestAttributeRedirectStore rads = (RequestAttributeRedirectStore) sess.getAttribute(name);
			
			for (Map.Entry<String, Object> me : rads.getAttrs().entrySet())
			{
				req.setAttribute(me.getKey(), me.getValue());
			}
			sess.removeAttribute(name);
		}
	}
	
	
	private static String name()
	{
		return RequestAttributeRedirectStore.class.getSimpleName();
	}
}
