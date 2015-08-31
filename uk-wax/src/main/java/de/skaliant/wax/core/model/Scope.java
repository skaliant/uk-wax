package de.skaliant.wax.core.model;

import java.util.List;


/**
 * Generalization of a scope as in Application, Session, and Request. I wish
 * Sun would've done this.
 *
 * @author Udo Kastilan
 */
public interface Scope<S>
{
	/**
	 * Retrieve an object from the scope.
	 * 
	 * @param name Name
	 * @return Object or null
	 */
	Object get(String name);

	/**
	 * Retrieve an object from the scope and cast it.
	 * 
	 * @param name Name
	 * @param type Target type
	 * @return Object or null
	 */
	<T> T get(String name, Class<T> type);

	/**
	 * Set an object into the scope. As <code>null</code> values cannot be set into the
	 * scopes, setting a value of <code>null</code> will be treated the same way
	 * as calling {@link #remove(String)}.
	 * 
	 * @param name Name
	 * @param value Value
	 */
	void set(String name, Object value);

	/**
	 * Check whether an object is present in the scope.
	 * 
	 * @param name Name
	 * @return Is there an object with this name?
	 */
	boolean has(String name);
	
	/**
	 * Remove an object from the scope.
	 * 
	 * @param name Name
	 */
	void remove(String name);

	/**
	 * Get all names which currently are registered for objects in this scope.
	 * 
	 * @return Names
	 */
	List<String> getNames();

	/**
	 * Get the actual raw scope object.
	 * 
	 * @return Scope
	 */
	S getSource();
}
