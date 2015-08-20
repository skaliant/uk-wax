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
 * 
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
	
	
	public Object newInstance()
		throws Exception
	{
		return type.newInstance();
	}
	
	
	public Collection<ActionInfo> getActions()
	{
		return actions.values();
	}
	
	
	public void addInitMethod(Method meth)
	{
		if (initMethods.isEmpty())
		{
			initMethods = new ArrayList<Method>(2);
		}
		initMethods.add(meth);
	}
	
	
	public List<Method> getInitMethods()
	{
		return initMethods;
	}


	public void addExitMethod(Method meth)
	{
		if (exitMethods.isEmpty())
		{
			exitMethods = new ArrayList<Method>(2);
		}
		exitMethods.add(meth);
	}


	public List<Method> getExitMethods()
	{
		return exitMethods;
	}
	
	
	public void add(ActionInfo action)
	{
		actions.put(action.getName().toLowerCase(), action);
	}
	
	
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
	
	
	public ActionInfo findAction(String name)
	{
		return actions.get(name.toLowerCase());
	}
	
	
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
	
	
	public boolean isGuarded()
	{
		return guardian != null;
	}
	
	
	public boolean isDefaultController()
	{
		return defaultController;
	}
	
	
	public Class<? extends Guardian> getGuardian()
	{
		return guardian;
	}


	public void setGuardian(Class<? extends Guardian> guardian)
	{
		this.guardian = guardian;
	}


	public Class<?> getType()
	{
		return type;
	}


	public void setType(Class<?> type)
	{
		this.type = type;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public void setDefaultController(boolean defaultController)
	{
		this.defaultController = defaultController;
	}
}
