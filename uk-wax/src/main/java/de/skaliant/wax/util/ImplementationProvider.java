package de.skaliant.wax.util;

import de.skaliant.wax.util.logging.Log;


/**
 * Simple provider class looking for an implementation class by name; will
 * return the default implementation if no such class was found or no instance
 * could be created. Implementation classes must behave like bean classes: they
 * must contain a default constructor without parameters.
 *
 * @author Udo Kastilan
 */
public class ImplementationProvider {
	/**
	 * Return an instance of class &lt;C&gt;, preferably one of type
	 * <code>name</code>. If the named class was not found or could not be
	 * instantiated, return an instance of class <code>fallback</code>.
	 * 
	 * @param target
	 * @param name
	 * @param fallback
	 * @return
	 */
	public static <C> C provide(Class<? extends C> target, String name,
			Class<? extends C> fallback) {
		C c = null;

		if (name != null) {
			Log.get(ImplementationProvider.class)
					.info("Trying to load and create instance of \"" + name
							+ "\" as replacement for " + target.getSimpleName());
			try {
				c = target.cast(Class.forName(name).newInstance());
			}
			catch (Exception ex) {
				Log.get(ImplementationProvider.class)
						.warn("Could not load \"" + name + '"', ex);
			}
		}
		if (c == null) {
			try {
				c = fallback.newInstance();
			}
			catch (Exception ex) {
				Log.get(ImplementationProvider.class).fatal(
						"Could not load default implementation " + fallback.getSimpleName(),
						ex);
			}
		}

		return c;
	}
}
