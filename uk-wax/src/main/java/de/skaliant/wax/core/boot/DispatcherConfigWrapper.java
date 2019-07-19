package de.skaliant.wax.core.boot;

import java.util.List;


/**
 * Unification of configuration data for servlets and servlet filters.
 *
 * @author Udo Kastilan
 */
interface DispatcherConfigWrapper {
	/**
	 * Get all parameter names.
	 * 
	 * @return
	 */
	List<String> getParamNames();


	/**
	 * Get a parameter value by name.
	 * 
	 * @param name
	 * @return
	 */
	String getParam(String name);


	/**
	 * Return the name of the dispatcher; this is either the servlet name or the
	 * filter name.
	 * 
	 * @return
	 */
	String getDispatcherName();


	/**
	 * What kind of dispatcher are we dealing with?
	 * 
	 * @return
	 */
	DispatcherType getType();
}