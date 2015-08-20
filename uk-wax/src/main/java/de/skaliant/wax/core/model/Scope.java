package de.skaliant.wax.core.model;

import java.util.List;


/**
 * 
 *
 * @author Udo Kastilan
 */
public interface Scope<S>
{
	Object get(String name);
	
	<T> T get(String name, Class<T> type);
	
	void set(String name, Object value);
	
	boolean has(String name);
	
	void remove(String name);
	
	List<String> getNames();
	
	S getSource();
}
