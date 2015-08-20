package de.skaliant.wax.core.model;



/**
 * Interface for classes which will be used to find (and manage) controller
 * classes and their actions.
 *
 * @author Udo Kastilan
 */
public interface ControllerManager
{
	/**
	 * Get a config bean filled with default values. The Bootstrapper will try to apply init parameters
	 * on this instance before any other call to this ControllerManager will be done.
	 */
	ControllerManagerConfig getConfig();
	
	/**
	 * Find a controller for a given class.
	 * 
	 * @param type Controller class
	 * @return ControllerInfo instance, or null if not found
	 */
	ControllerInfo findForType(Class<?> type);

	/**
	 * Find the default controller.
	 * 
	 * @return ControllerInfo instance, or null if no default controller available
	 */
	ControllerInfo findDefaultController();
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	ControllerInfo findForName(String name);
}
