package de.skaliant.wax.core.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.skaliant.wax.app.Guardian;


/**
 * Information on a controller.
 *
 * @author Udo Kastilan
 */
public class ControllerInfo
{
	private Map<String, ActionInfo> actions = new HashMap<String, ActionInfo>();
	private List<Method> initMethods = Collections.emptyList();
	private List<Method> exitMethods = Collections.emptyList();
	private Class<? extends Guardian> guardian = null;
	private Class<?> type = null;
	private String name = null;
	private boolean defaultController = false;
	

	/**
	 * Create a new instance of this controller. Shortcut for {@link Class#newInstance()}. 
	 * 
	 * @return Controller instance
	 * @throws Exception
	 */
	public Object newInstance()
		throws Exception
	{
		return type.newInstance();
	}
	
	
	/**
	 * Get all actions available in this controller.
	 * 
	 * @return Actions
	 */
	public Collection<ActionInfo> getActions()
	{
		return actions.values();
	}
	

	/**
	 * Add an init method.
	 * 
	 * @param meth Init method
	 */
	public void addInitMethod(Method meth)
	{
		if (initMethods.isEmpty())
		{
			initMethods = new ArrayList<Method>(2);
		}
		initMethods.add(meth);
	}
	

	/**
	 * Get a list of all init methods. Though there is no way of giving a certain order right now, this
	 * might be added later on.
	 * 
	 * @return List of init methods
	 */
	public List<Method> getInitMethods()
	{
		return initMethods;
	}


	/**
	 * Add an exit method.
	 * 
	 * @param meth Exit method
	 */
	public void addExitMethod(Method meth)
	{
		if (exitMethods.isEmpty())
		{
			exitMethods = new ArrayList<Method>(2);
		}
		exitMethods.add(meth);
	}


	/**
	 * Get a list of all exit methods. Though there is no way of giving a certain order right now, this
	 * might be added later on.
	 * 
	 * @return List of exit methods
	 */
	public List<Method> getExitMethods()
	{
		return exitMethods;
	}
	
	
	/**
	 * Add information on an action.
	 * 
	 * @param action Action
	 */
	public void add(ActionInfo action)
	{
		actions.put(action.getName().toLowerCase(), action);
	}
	
	
	/**
	 * Find an action by method name (case-sensitive).
	 * 
	 * @param name Action method name
	 * @return ActionInfo or null
	 */
	public ActionInfo findByMethodName(String name)
	{
		for (ActionInfo ai : actions.values())
		{
			if (name.equals(ai.getMethod().getName()))
			{
				return ai;
			}
		}
		return null;
	}
	
	
	/**
	 * Find an action by its name (case-insensitive). This name is typically the name given
	 * via annotation, but it might also be (derived from) the method name.
	 * 
	 * @param name Action name
	 * @return ActionInfo or null
	 */
	public ActionInfo findAction(String name)
	{
		return actions.get(name.toLowerCase());
	}
	
	
	/**
	 * Find the default action, if any.
	 * 
	 * @return Default action or null
	 */
	public ActionInfo findDefaultAction()
	{
		for (ActionInfo a : actions.values())
		{
			if (a.isDefaultAction())
			{
				return a;
			}
		}
		return null;
	}
	
	
	/**
	 * Is this controller guarded, i.e. is access restricted?
	 * 
	 * @return Is this controller guarded?
	 */
	public boolean isGuarded()
	{
		return guardian != null;
	}
	

	/**
	 * Is this the default controller of the dispatcher?
	 * 
	 * @return Default controller?
	 */
	public boolean isDefaultController()
	{
		return defaultController;
	}


	/**
	 * Is this the default controller of the dispatcher?
	 * 
	 * @param defaultController Default controller?
	 */
	public void setDefaultController(boolean defaultController)
	{
		this.defaultController = defaultController;
	}
	

	/**
	 * Get the guardian type, if guarded.
	 * 
	 * @return Guardian type or null
	 */
	public Class<? extends Guardian> getGuardian()
	{
		return guardian;
	}


	/**
	 * Set the guardian type.
	 * 
	 * @param guardian Guardian type
	 */
	public void setGuardian(Class<? extends Guardian> guardian)
	{
		this.guardian = guardian;
	}


	/**
	 * Get the controller's class type.
	 * 
	 * @return Controller class
	 */
	public Class<?> getType()
	{
		return type;
	}


	/**
	 * Set the controller class type.
	 * 
	 * @param type Controller class
	 */
	public void setType(Class<?> type)
	{
		this.type = type;
	}


	/**
	 * Get the name of this controller.
	 * 
	 * @return Controller name
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * Set the name of this controller. This name should be normalized (e.g. all lower case).
	 * 
	 * @param name Controller name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}
