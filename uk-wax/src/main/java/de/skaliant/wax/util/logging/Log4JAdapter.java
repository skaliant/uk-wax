package de.skaliant.wax.util.logging;

import org.apache.log4j.Logger;


/**
 * 
 * 
 * @author Udo Kastilan
 */
public class Log4JAdapter
	implements Log.Provider
{
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
