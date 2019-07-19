package de.skaliant.wax.core.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Result of the resolution process. Contains info on the controller, the
 * action, the path that led to the action, and optionally any additional path
 * information not used in the process.
 * 
 * @author Udo Kastilan
 */
public class RouterResult {
	private List<String> pathInfo = new ArrayList<>(0);
	private List<String> path = new ArrayList<>(0);
	private ControllerInfo controller = null;
	private ActionInfo action = null;


	/**
	 * Constructor.
	 * 
	 * @param controller
	 *          Controller information
	 * @param action
	 *          Action information
	 * @param path
	 *          Path info parts that were used for resolving
	 * @param pathInfo
	 *          Path info parts not used for resolving
	 */
	public RouterResult(ControllerInfo controller, ActionInfo action, List<String> path, List<String> pathInfo) {
		this.controller = controller;
		this.action = action;
		this.path = path;
		this.pathInfo = pathInfo;
	}


	public List<String> getPath() {
		return path;
	}


	public List<String> getPathInfo() {
		return pathInfo;
	}


	public ControllerInfo getController() {
		return controller;
	}


	public ActionInfo getAction() {
		return action;
	}
}
