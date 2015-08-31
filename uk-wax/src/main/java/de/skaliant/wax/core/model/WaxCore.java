package de.skaliant.wax.core.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.skaliant.wax.util.logging.Log;


/**
 * Global information hub, available once per application scope level. Provides information
 * on all the dispatchers registered (in typically applications, there is only one).
 * 
 * @author Udo Kastilan
 */
public class WaxCore
{
	private final Log LOG = Log.get(WaxCore.class);
	/**
	 * Dispatcher map
	 */
	private Map<String, DispatcherInfo> dispatchers = new HashMap<String, DispatcherInfo>(2);
	
	
	/**
	 * Register a dispatcher.
	 * 
	 * @param di Dispatcher
	 */
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
	
	
	/**
	 * Get a dispatcher by its id.
	 * 
	 * @param id Dispatcher id
	 * @return DispatcherInfo object or null, if not registered
	 */
	public DispatcherInfo getDispatcherInfo(String id)
	{
		return dispatchers.get(id);
	}
	
	
	/**
	 * Get a collection of all dispatchers registered. 
	 * 
	 * @return All dispatchers as a collection
	 */
	public Collection<DispatcherInfo> getDispatchers()
	{
		return new HashSet<DispatcherInfo>(dispatchers.values());
	}
}
