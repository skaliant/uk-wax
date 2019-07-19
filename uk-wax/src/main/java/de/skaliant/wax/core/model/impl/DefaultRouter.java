package de.skaliant.wax.core.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.skaliant.wax.core.ControllerManager;
import de.skaliant.wax.core.Router;
import de.skaliant.wax.core.model.ActionInfo;
import de.skaliant.wax.core.model.ControllerInfo;
import de.skaliant.wax.core.model.DispatcherInfo;
import de.skaliant.wax.core.model.RouterConfig;
import de.skaliant.wax.core.model.RouterResult;
import de.skaliant.wax.util.MiscUtils;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class DefaultRouter implements Router {
	private RouterConfig config = new RouterConfig();


	@Override
	public RouterConfig getConfig() {
		return config;
	}


	@Override
	public RouterResult route(DispatcherInfo disp, String servletPath, String pathInfo) {
		ControllerManager ctrlMan = disp.getControllerManager();
		List<String> parts = Collections.emptyList();
		List<String> usedPath = new ArrayList<>(2);
		List<String> remainingPathInfo = new ArrayList<>(5);
		RouterResult rr = null;
		ControllerInfo ci = null;
		ActionInfo ai = null;

		if (disp.isSuffixPattern()) {
			/*
			 * From the servlet path (e.g. "/path/to/matching/my.pattern.suffix"),
			 * extract the last part (the suffix pattern) and split it up at dots,
			 * ignoring the last part (the suffix). So, from the input string
			 * "/path/to/matching/my.pattern.suffix" we'll get "my" and "pattern" as
			 * parts.
			 */
			String fileName = MiscUtils.getLastElementOf(MiscUtils.splitAtChar(servletPath, '/'));

			parts = MiscUtils.splitAtChar(fileName, '.');
			if (!parts.isEmpty()) {
				parts.remove(parts.size() - 1);
			}
			/*
			 * For suffix patterns, the used path rests empty, and the remaining path
			 * info remains untouched
			 */
			remainingPathInfo = new ArrayList<>(MiscUtils.splitAtChar(pathInfo, '/'));
			if (!parts.isEmpty()) {
				ci = ctrlMan.findForName(parts.get(0));
				if ((ci != null) && (parts.size() > 1)) {
					ai = ci.findAction(parts.get(1));
				}
			}
		} else {
			parts = MiscUtils.splitAtChar(pathInfo, '/');
			remainingPathInfo = new ArrayList<>(parts);
			if (!parts.isEmpty()) {
				ci = ctrlMan.findForName(parts.get(0));
				if (ci != null) {
					usedPath.add(remainingPathInfo.remove(0));
					if (parts.size() > 1) {
						ai = ci.findAction(parts.get(1));
						if (ai != null) {
							usedPath.add(remainingPathInfo.remove(0));
						}
					}
				}
			}
		}
		/*
		 * Fallback to defaults
		 */
		if (ci == null) {
			ci = ctrlMan.findDefaultController();
		}
		if ((ci != null) && (ai == null)) {
			ai = ci.getDefaultAction();
		}
		if ((ci != null) && (ai != null)) {
			rr = new RouterResult(ci, ai, usedPath, remainingPathInfo);
		}
		return rr;
	}


	@Override
	public String createPath(DispatcherInfo disp, ControllerInfo controller, ActionInfo action) {
		StringBuilder path = new StringBuilder();
		String pattern = disp.getPattern();

		if (disp.isSuffixPattern()) // suffix-based pattern, e.g. "*.ctrl"
		{
			String suffix = pattern.substring(pattern.lastIndexOf('.'));

			path.append('/').append(controller.getName());
			if ((action != null) && !action.isDefaultAction()) {
				path.append('.').append(action.getName());
			}
			path.append(suffix);
		} else // path-based pattern, e.g. "/de/*"
		{
			String withoutAsterisk = pattern.replace("*", "");

			if (!withoutAsterisk.startsWith("/")) {
				path.append('/');
			}
			path.append(withoutAsterisk);
			if (!withoutAsterisk.endsWith("/")) {
				path.append('/');
			}
			path.append(controller.getName());
			if ((action != null) && !action.isDefaultAction()) {
				path.append(action.getName());
			}
		}
		return path.toString();
	}
}
