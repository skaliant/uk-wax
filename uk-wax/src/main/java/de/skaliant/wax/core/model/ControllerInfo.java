package de.skaliant.wax.core.model;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.skaliant.wax.app.Guardian;


/**
 * Information on a controller.
 *
 * @author Udo Kastilan
 */
public class ControllerInfo {
	private Map<String, ActionInfo> actions = Collections.emptyMap();
	private List<Method> initMethods = Collections.emptyList();
	private List<Method> exitMethods = Collections.emptyList();
	private Class<? extends Guardian> guardian = null;
	private Class<?> type = null;
	private String name = null;
	private boolean defaultController = false;


	/**
	 * Constructor. Both name and class type are essential informations and
	 * therefore must be provided.
	 * 
	 * @param type
	 *          Controller class
	 * @param name
	 *          Name
	 */
	public ControllerInfo(Class<?> type, String name) {
		this.type = type;
		this.name = name;
	}


	/**
	 * Create a new instance of this controller. Shortcut for
	 * {@link Class#newInstance()}.
	 * 
	 * @return Controller instance
	 * @throws Exception
	 */
	public Object newInstance()
		throws Exception {
		return type.newInstance();
	}


	/**
	 * Get all actions available in this controller.
	 * 
	 * @return Actions
	 */
	public Collection<ActionInfo> getAllActions() {
		return actions.values();
	}


	/**
	 * Sets init methods; these methods will be called before the action method.
	 * 
	 * @param initMethods
	 */
	public void setInitMethods(List<Method> initMethods) {
		this.initMethods = initMethods;
	}


	/**
	 * Get a list of all init methods. Though there is no way of giving a certain
	 * order right now, this might be added later on.
	 * 
	 * @return List of init methods
	 */
	public List<Method> getInitMethods() {
		return initMethods;
	}


	/**
	 * Sets exit methods; these methods will be called after the action method.
	 * 
	 * @param exitMethods
	 */
	public void setExitMethods(List<Method> exitMethods) {
		this.exitMethods = exitMethods;
	}


	/**
	 * Get a list of all exit methods. Though there is no way of giving a certain
	 * order right now, this might be added later on.
	 * 
	 * @return List of exit methods
	 */
	public List<Method> getExitMethods() {
		return exitMethods;
	}


	/**
	 * Sets all action methods mapped by their names.
	 * 
	 * @param actions
	 */
	public void setActions(Map<String, ActionInfo> actions) {
		this.actions = actions;
	}


	/**
	 * Find an action by method name (case-sensitive).
	 * 
	 * @param name
	 *          Action method name
	 * @return ActionInfo or null
	 */
	public ActionInfo findByMethodName(String name) {
		return actions.values().stream()
				.filter(a -> name.equals(a.getMethod().getName())).findFirst()
				.orElse(null);
	}


	/**
	 * Find an action by its name (case-insensitive). This name is typically the
	 * name given via annotation, but it might also be (derived from) the method
	 * name.
	 * 
	 * @param name
	 *          Action name
	 * @return ActionInfo or null
	 */
	public ActionInfo findAction(String name) {
		return actions.get(name.toLowerCase());
	}


	/**
	 * Find the default action, if any.
	 * 
	 * @return Default action or null
	 */
	public ActionInfo getDefaultAction() {
		return actions.values().stream().filter(ActionInfo::isDefaultAction)
				.findFirst().orElse(null);
	}


	/**
	 * Is this controller guarded, i.e. is access restricted?
	 * 
	 * @return Is this controller guarded?
	 */
	public boolean isGuarded() {
		return guardian != null;
	}


	/**
	 * Is this the default controller of the dispatcher?
	 * 
	 * @return Default controller?
	 */
	public boolean isDefaultController() {
		return defaultController;
	}


	/**
	 * Is this the default controller of the dispatcher?
	 * 
	 * @param defaultController
	 *          Default controller?
	 */
	public void setDefaultController(boolean defaultController) {
		this.defaultController = defaultController;
	}


	/**
	 * Get the guardian type, if guarded.
	 * 
	 * @return Guardian type or null
	 */
	public Class<? extends Guardian> getGuardian() {
		return guardian;
	}


	/**
	 * Set the guardian type.
	 * 
	 * @param guardian
	 *          Guardian type
	 */
	public void setGuardian(Class<? extends Guardian> guardian) {
		this.guardian = guardian;
	}


	/**
	 * Get the controller's class type.
	 * 
	 * @return Controller class
	 */
	public Class<?> getType() {
		return type;
	}


	/**
	 * Get the name of this controller.
	 * 
	 * @return Controller name
	 */
	public String getName() {
		return name;
	}
}
