package de.skaliant.wax.core.model;



/**
 * Configuration bean of the ControllerManager. The controller manager is the class responsible
 * for finding and managing controllers.
 *
 * @author Udo Kastilan
 */
public class ControllerManagerConfig
{
	private ResolutionMode mode = ResolutionMode.EXPLICIT;
	private String packages = null;
	private String classes = null;


	/**
	 * ControllerManager working mode. <code>ResolutionMode.EXPLICIT</code> is default.
	 * 
	 * @return Working mode
	 */
	public ResolutionMode getMode()
	{
		return mode;
	}


	/**
	 * See {@link #getMode()}.
	 * 
	 * @param mode Working mode
	 */
	public void setMode(ResolutionMode mode)
	{
		this.mode = mode;
	}


	/**
	 * The names of all declared packages which may contain controller classes, separated by whitespace,
	 * comma, or semicolon.
	 * 
	 * @return Controller packages
	 */
	public String getPackages()
	{
		return packages;
	}


	/**
	 * See {@link #getPackages()}.
	 * 
	 * @param packages Controller packages
	 */
	public void setPackages(String packages)
	{
		this.packages = packages;
	}


	/**
	 * The names of all declared controller classes, separated by whitespace, comma, or semicolon.
	 * The class names may be either fully qualified or simple names to be expanded by the controller
	 * packages.
	 * 
	 * @return Class names
	 */
	public String getClasses()
	{
		return classes;
	}


	/**
	 * See {@link #getClasses()}.
	 * 
	 * @param classes Class names
	 */
	public void setClasses(String classes)
	{
		this.classes = classes;
	}
}
