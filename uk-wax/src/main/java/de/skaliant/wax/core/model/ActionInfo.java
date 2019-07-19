package de.skaliant.wax.core.model;

import java.lang.reflect.Method;

import de.skaliant.wax.app.Guardian;


/**
 * Information on an action.
 * 
 * @author Udo Kastilan
 */
public class ActionInfo {
	private Class<? extends Guardian> guardian = null;
	private String name;
	private Method method;
	private boolean defaultAction = false;


	public ActionInfo(Method method, String name) {
		this.method = method;
		this.name = name;
	}


	/**
	 * Is this action guarded?
	 * 
	 * @return Is it guarded?
	 */
	public boolean isGuarded() {
		return guardian != null;
	}


	/**
	 * Get the guardian class type, if guarded.
	 * 
	 * @return Guardian class type or null
	 */
	public Class<? extends Guardian> getGuardian() {
		return guardian;
	}


	/**
	 * Set the guardian class type.
	 * 
	 * @param guardian
	 *          Guardian class type
	 */
	public void setGuardian(Class<? extends Guardian> guardian) {
		this.guardian = guardian;
	}


	/**
	 * Is this the default action of the controller?
	 * 
	 * @return Default action?
	 */
	public boolean isDefaultAction() {
		return defaultAction;
	}


	/**
	 * Is this the default action of the controller?
	 * 
	 * @param defaultAction
	 *          Default action?
	 */
	public void setDefaultAction(boolean defaultAction) {
		this.defaultAction = defaultAction;
	}


	/**
	 * Get the action's name.
	 * 
	 * @return Action name
	 */
	public String getName() {
		return name;
	}


	/**
	 * The method implementing the action.
	 * 
	 * @return Action method
	 */
	public Method getMethod() {
		return method;
	}
}
