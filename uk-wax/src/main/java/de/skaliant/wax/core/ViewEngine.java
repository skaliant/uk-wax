package de.skaliant.wax.core;

import java.io.IOException;

import javax.servlet.ServletException;

import de.skaliant.wax.core.model.Call;
import de.skaliant.wax.core.model.Configurable;
import de.skaliant.wax.core.model.ViewEngineConfig;


/**
 * Interface for possibly different implementations of view engines. The default
 * engine is JSP based, naturally. Each implementation is obliged to create an
 * instance of ViewEngineInfo for configuration purposes and provide access to
 * that object after instantiation. The dispatcher will set configuration data
 * on this on initialisation phase, at least before the first call to
 * {@link #render(Call)} is made.
 *
 * @author Udo Kastilan
 */
public interface ViewEngine extends Configurable<ViewEngineConfig> {
	/**
	 * Get the configuration object.
	 * 
	 * @return Configuration object; must not be null
	 */
	@Override
	ViewEngineConfig getConfig();


	/**
	 * Render a view.
	 * 
	 * @param view
	 *          Name of the view
	 * @param call
	 *          Information on the current call including parameters and data
	 *          scopes
	 */
	void render(String view, Call call)
		throws ServletException, IOException;
}
