package de.skaliant.wax.core.model;

import java.util.List;
import java.util.Map;


/**
 * Interface for an object able to provide named parameters.
 *
 * @author Udo Kastilan
 */
public interface ParameterProvider
{
	/**
	 * Checks whether a certain name is present.
	 * 
	 * @param name Parameter name
	 * @return Is it present?
	 */
	boolean isParameterPresent(String name);


	/**
	 * Get all parameter names. Order might be important, so the original order is preserved.
	 * 
	 * @return Parameter names
	 */
	List<String> getParameterNames();


	/**
	 * Gets the first (or only) value of a parameter.
	 * 
	 * @param name Name
	 * @return First (or only) value, or null if not present/without value
	 */
	String getParameter(String name);


	/**
	 * Get all the values of a parameter.
	 * 
	 * @param name Name
	 * @return All values; may be null in case the parameter is not present/has no values at all
	 */
	String[] getParameterValues(String name);


	/**
	 * Get all parameter and their values as a map.
	 * 
	 * @return All parameters and values
	 */
	Map<String, String[]> getParameters();

}