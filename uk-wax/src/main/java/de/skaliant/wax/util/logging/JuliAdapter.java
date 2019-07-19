package de.skaliant.wax.util.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import de.skaliant.wax.core.Environment;
import de.skaliant.wax.util.MiscUtils;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class JuliAdapter implements Log.Provider {
	@Override
	public void init(File configFolder) {
		String env = Environment.getInstance().getHint();
		String[] test = { "logging-" + env + ".properties", "logging.properties" };
		boolean found = false;

		for (String s : test) {
			File f = new File(configFolder, s);

			if (f.exists()) {
				InputStream in = null;

				try {
					System.out.println("LOGGING INITIALIZATION: Configuring logging from " + f.getAbsolutePath());
					in = new FileInputStream(f);
					LogManager.getLogManager().readConfiguration(in);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				finally {
					MiscUtils.close(in);
				}
				found = true;
				break;
			}
		}
		if (!found) {
			System.out.println("LOGGING INITIALIZATION: No explicit log configuration found, java.util.logging will use default behaviour");
		}
	}


	@Override
	public Log create(String section) {
		return new Bridge(section);
	}


	@Override
	public String toString() {
		return "java.util.Logging";
	}

	private static class Bridge extends Log {
		private Logger logger = null;


		private Bridge(String section) {
			logger = Logger.getLogger(section);
		}


		@Override
		public boolean isDebugEnabled() {
			return logger.isLoggable(Level.FINE);
		}


		@Override
		public boolean isInfoEnabled() {
			return logger.isLoggable(Level.INFO);
		}


		@Override
		public void debug(Object msg) {
			log(Level.FINE, msg, null);
		}


		@Override
		public void debug(Object msg, Throwable t) {
			log(Level.FINE, msg, t);
		}


		@Override
		public void info(Object msg) {
			log(Level.INFO, msg, null);
		}


		@Override
		public void info(Object msg, Throwable t) {
			log(Level.INFO, msg, t);
		}


		@Override
		public void warn(Object msg) {
			log(Level.WARNING, msg, null);
		}


		@Override
		public void warn(Object msg, Throwable t) {
			log(Level.WARNING, msg, t);
		}


		@Override
		public void error(Object msg) {
			log(Level.SEVERE, msg, null);
		}


		@Override
		public void error(Object msg, Throwable t) {
			log(Level.SEVERE, msg, t);
		}


		@Override
		public void fatal(Object msg) {
			log(Level.SEVERE, msg, null);
		}


		@Override
		public void fatal(Object msg, Throwable t) {
			log(Level.SEVERE, msg, t);
		}


		private void log(Level level, Object msg, Throwable t) {
			Caller c = caller();

			if (t == null) {
				logger.logp(level, c.className, c.methodName, String.valueOf(msg));
			} else {
				logger.logp(level, c.className, c.methodName, String.valueOf(msg), t);
			}
		}


		private Caller caller() {
			StackTraceElement[] stes = new Throwable().getStackTrace();
			String me = getClass().getName();

			for (StackTraceElement ste : stes) {
				if (!ste.getClassName().equals(me)) {
					return new Caller(ste);
				}
			}
			return null;
		}
	}

	private static class Caller {
		private String className = null;
		private String methodName = null;


		private Caller(StackTraceElement ste) {
			this.className = ste.getClassName();
			this.methodName = ste.getMethodName();
		}
	}
}
