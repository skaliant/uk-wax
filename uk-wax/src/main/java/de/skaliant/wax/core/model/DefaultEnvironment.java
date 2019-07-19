package de.skaliant.wax.core.model;

import java.io.File;

import javax.servlet.ServletContext;

import de.skaliant.wax.core.Environment;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class DefaultEnvironment extends Environment {
	private final static String SYS_ARG = "wax.env";

	private String hint = null;
	private boolean initialized = false;


	@Override
	public File getConfigLocation(ServletContext sc) {
		return new File(sc.getRealPath("/WEB-INF/"));
	}


	@Override
	public String getHint() {
		if (!initialized) {
			String s = System.getProperty(SYS_ARG);

			if (s != null) {
				s = s.toLowerCase();
			} else {
				s = "dev";
			}
			hint = s;
			initialized = true;
		}
		return hint;
	}
}
