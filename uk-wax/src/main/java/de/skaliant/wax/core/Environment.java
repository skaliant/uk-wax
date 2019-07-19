package de.skaliant.wax.core;

import java.io.File;

import javax.servlet.ServletContext;

import de.skaliant.wax.core.model.DefaultEnvironment;


/**
 * 
 *
 * @author Udo Kastilan
 */
public abstract class Environment {
	private final static String ENV_IMPL_ARG = "wax.env.impl";
	private static Environment env = null;

	static {
		if (System.getProperty(ENV_IMPL_ARG) != null) {
			try {
				env = Environment.class.cast(Class.forName(System.getProperty(ENV_IMPL_ARG)));
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (env == null) {
			env = new DefaultEnvironment();
		}
	}


	/**
	 * 
	 * @return
	 */
	public abstract String getHint();


	/**
	 * 
	 * @param sc
	 * @return
	 */
	public abstract File getConfigLocation(ServletContext sc);


	public static Environment getInstance() {
		return env;
	}
}
