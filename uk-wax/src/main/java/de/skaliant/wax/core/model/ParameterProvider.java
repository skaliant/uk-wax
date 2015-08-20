package de.skaliant.wax.core.model;

import java.util.List;
import java.util.Map;


/**
 * Interface for an object able to provide named parameters and extra path information.
 *
 * @author Udo Kastilan
 */
public interface ParameterProvider
{
	/**
	 * 
	 * @param name
	 * @return
	 */
	boolean isParameterPresent(String name);


	/**
	 * 
	 * @return
	 */
	List<String> getParameterNames();


	/**
	 * 
	 * @param name
	 * @return
	 */
	String getParameter(String name);


	/**
	 * 
	 * @param name
	 * @return
	 */
	String[] getParameterValues(String name);


	/**
	 * 
	 * @return
	 */
	Map<String, String[]> getParameters();

}