package de.skaliant.wax.core.model;



/**
 * 
 *
 * @author Udo Kastilan
 */
public class ControllerManagerConfig
{
	private ResolutionMode mode = ResolutionMode.EXPLICIT;
	private String packages = null;
	private String classes = null;


	public ResolutionMode getMode()
	{
		return mode;
	}


	public void setMode(ResolutionMode mode)
	{
		this.mode = mode;
	}


	public String getPackages()
	{
		return packages;
	}


	public void setPackages(String packages)
	{
		this.packages = packages;
	}


	public String getClasses()
	{
		return classes;
	}


	public void setClasses(String classes)
	{
		this.classes = classes;
	}
}
