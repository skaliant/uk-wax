package de.skaliant.wax.core.model;

import java.lang.reflect.Method;

import de.skaliant.wax.app.Guardian;


/**
 * 
 * 
 * @author Udo Kastilan
 */
public class ActionInfo
{
	private Class<? extends Guardian> guardian = null;
	private String name = null;
	private Method method = null;
	private boolean defaultAction = false;
	
	
	public boolean isGuarded()
	{
		return guardian != null;
	}
	
	
	public Class<? extends Guardian> getGuardian()
	{
		return guardian;
	}


	public void setGuardian(Class<? extends Guardian> guardian)
	{
		this.guardian = guardian;
	}


	public boolean isDefaultAction()
	{
		return defaultAction;
	}


	public void setDefaultAction(boolean defaultAction)
	{
		this.defaultAction = defaultAction;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public Method getMethod()
	{
		return method;
	}


	public void setMethod(Method method)
	{
		this.method = method;
	}
}
