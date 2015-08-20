package de.skaliant.wax.util.logging;

/**
 * 
 * 
 * @author Udo Kastilan
 */
public abstract class Log
{
	private static Provider provider = null;

	static
	{
		try
		{
			Class.forName("org.apache.log4j.Logger");
			provider = Provider.class.cast(Class.forName(
					"de.skaliant.wax.util.Log4JAdapter").newInstance());
		}
		catch (Exception ex)
		{
			provider = new JuliAdapter();
		}
		provider.create(Log.class.getName()).info(
				"Logging is proudly presented by " + provider);
	}


	public static Log get(String section)
	{
		return provider.create(section);
	}


	public static Log get(Class<?> clazz)
	{
		return get(clazz.getName());
	}


	public abstract boolean isDebugEnabled();


	public abstract boolean isInfoEnabled();


	public abstract void debug(Object msg);


	public abstract void debug(Object msg, Throwable t);


	public abstract void info(Object msg);


	public abstract void info(Object msg, Throwable t);


	public abstract void warn(Object msg);


	public abstract void warn(Object msg, Throwable t);


	public abstract void error(Object msg);


	public abstract void error(Object msg, Throwable t);


	public abstract void fatal(Object msg);


	public abstract void fatal(Object msg, Throwable t);

	public static interface Provider
	{
		Log create(String section);
	}
}
