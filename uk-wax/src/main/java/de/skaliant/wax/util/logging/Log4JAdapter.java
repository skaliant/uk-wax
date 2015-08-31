package de.skaliant.wax.util.logging;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import de.skaliant.wax.core.Environment;


/**
 * 
 * 
 * @author Udo Kastilan
 */
public class Log4JAdapter
	implements Log.Provider
{
	public void init(File configFolder)
	{
		String env = Environment.getInstance().getHint();
		String[] test = {
			"log4j-" + env + ".properties",
			"log4j-" + env + ".xml",
			"log4j.properties",
			"log4j.xml"
		};
		boolean found = false;
		
		for (String s : test)
		{
			File f = new File(configFolder, s);
			
			if (f.exists())
			{
				System.out.println("Configuring logging from " + f.getAbsolutePath());
				if (f.getName().endsWith(".xml"))
				{
					DOMConfigurator.configure(f.getAbsolutePath());
				}
				else
				{
					PropertyConfigurator.configure(f.getAbsolutePath());
				}
				found = true;
				break;
			}
		}
		if (!found)
		{
			System.out.println("No explicit log configuration found; please use log4j default schematics for configuration (e.g. a log4j.properties in the class path)");
		}
	}
	
	
	public Log create(String section)
	{
		return new Bridge(section);
	}

	
	@Override
	public String toString()
	{
		return "log4j";
	}
	

	private static class Bridge
		extends Log
	{
		private Logger logger = null;

		
		private Bridge(String section)
		{
			logger = Logger.getLogger(section);
		}
		

		@Override
		public boolean isDebugEnabled()
		{
			return logger.isDebugEnabled();
		}


		@Override
		public boolean isInfoEnabled()
		{
			return logger.isInfoEnabled();
		}


		@Override
		public void debug(Object msg)
		{
			logger.debug(msg);
		}


		@Override
		public void debug(Object msg, Throwable t)
		{
			logger.debug(msg, t);
		}


		@Override
		public void info(Object msg)
		{
			logger.info(msg);
		}


		@Override
		public void info(Object msg, Throwable t)
		{
			logger.info(msg, t);
		}


		@Override
		public void warn(Object msg)
		{
			logger.warn(msg);
		}


		@Override
		public void warn(Object msg, Throwable t)
		{
			logger.warn(msg, t);
		}


		@Override
		public void error(Object msg)
		{
			logger.error(msg);
		}


		@Override
		public void error(Object msg, Throwable t)
		{
			logger.error(msg, t);
		}


		@Override
		public void fatal(Object msg)
		{
			logger.fatal(msg);
		}


		@Override
		public void fatal(Object msg, Throwable t)
		{
			logger.fatal(msg, t);
		}
	}
}
