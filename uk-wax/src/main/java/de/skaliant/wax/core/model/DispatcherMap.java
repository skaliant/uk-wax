package de.skaliant.wax.core.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.skaliant.wax.util.logging.Log;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class DispatcherMap
{
	private final static Log LOG = Log.get(DispatcherMap.class);
	
	private Map<String, DispatcherInfo> dispatchers = new HashMap<String, DispatcherInfo>(2);
	
	
	public void register(DispatcherInfo di)
	{
		if (dispatchers.containsKey(di.getId()))
		{
			LOG.warn("Dispatcher \"" + di.getId() + "\" already registered, ignoring call");
			return;
		}
		LOG.info("Registering dispatcher \"" + di.getId() + "\"");
		dispatchers.put(di.getId(), di);
	}
	
	
	public DispatcherInfo getDispatcherInfo(String id)
	{
		return dispatchers.get(id);
	}
	
	
	public Collection<DispatcherInfo> getDispatchers()
	{
		return dispatchers.values();
	}
}
