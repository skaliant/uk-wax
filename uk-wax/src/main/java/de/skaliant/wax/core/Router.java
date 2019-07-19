package de.skaliant.wax.core;

import de.skaliant.wax.core.model.ActionInfo;
import de.skaliant.wax.core.model.Configurable;
import de.skaliant.wax.core.model.ControllerInfo;
import de.skaliant.wax.core.model.DispatcherInfo;
import de.skaliant.wax.core.model.RouterConfig;
import de.skaliant.wax.core.model.RouterResult;


/**
 * Interface for classes which will be used to translate from a path info to a
 * controller and action.
 *
 * @author Udo Kastilan
 */
public interface Router extends Configurable<RouterConfig> {
	/**
	 * Provide a configuration bean pre-filled with default values. The
	 * bootstrapper will try to inject init params on this instance before any
	 * other method is called.
	 * 
	 * @return Config bean
	 */
	@Override
	RouterConfig getConfig();


	/**
	 * Analyze the given call data and return an object containing a) the
	 * controller, b) the action, c) what path parts have been used to find these
	 * two, and d) the rest of the path info not used for resolving.
	 * 
	 * @param disp
	 *          Info on the dispatcher handling the request
	 * @param dispatcherPath
	 *          The path which has triggered the dispatcher
	 * @param pathInfo
	 *          The additional path after the dispatcher path
	 * @return RouterResult instance
	 */
	RouterResult route(DispatcherInfo disp, String dispatcherPath, String pathInfo);


	/**
	 * Create a path directing to a controller (and its method).
	 * 
	 * @param disp
	 * @param controller
	 * @param action
	 * @return
	 */
	String createPath(DispatcherInfo disp, ControllerInfo controller, ActionInfo action);
}
