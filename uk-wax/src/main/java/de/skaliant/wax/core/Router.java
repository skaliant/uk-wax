package de.skaliant.wax.core;

import java.util.List;

import de.skaliant.wax.core.model.ControllerManager;
import de.skaliant.wax.core.model.RouterConfig;
import de.skaliant.wax.core.model.RouterResult;


/**
 * Interface for classes which will be used to translate from a path info to
 * a controller and action. 
 *
 * @author Udo Kastilan
 */
public interface Router
{
	/**
	 * Provide a configuration bean pre-filled with default values. The bootstrapper will
	 * try to inject init params on this instance before any other method is called.
	 * 
	 * @return Config bean
	 */
	RouterConfig getConfig();
	
	/**
	 * Analyze the given path info split at "/" into parts, and return an object
	 * containing a) the controller, b) the action, c) what path parts have been
	 * used to find these two, and d) the rest of the path info not used for 
	 * resolving.
	 * 
	 * @param ctrlMan ControllerManager
	 * @param path Path info parts
	 * @return RouterResult instance
	 */
	RouterResult route(ControllerManager ctrlMan, List<String> path);
}
