package de.skaliant.wax.app;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.skaliant.wax.util.Config;
import de.skaliant.wax.util.logging.Log;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class ControllerBase
{
	protected HttpServletRequest request = null;
	protected HttpServletResponse response = null;
	protected ServletContext application = null;
	protected Config config = null;
	private Log log = Log.get(getClass().getName());

	
	public void setConfig(Config config)
	{
		this.config = config;
	}


	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}


	public void setResponse(HttpServletResponse response)
	{
		this.response = response;
	}


	public void setApplication(ServletContext application)
	{
		this.application = application;
	}

	
	protected void debug(String msg)
	{
		log.debug(msg);
	}

	
	protected void fatal(String msg)
	{
		log.fatal(msg);
	}

	
	protected void fatal(String msg, Throwable t)
	{
		log.fatal(msg, t);
	}

	
	protected void warn(String msg, Throwable t)
	{
		log.warn(msg, t);
	}

	
	protected void warn(String msg)
	{
		log.warn(msg);
	}

	
	protected void error(String msg, Throwable t)
	{
		log.error(msg, t);
	}

	
	protected void error(String msg)
	{
		log.error(msg);
	}

	
	protected void info(String msg)
	{
		log.info(msg);
	}
}
