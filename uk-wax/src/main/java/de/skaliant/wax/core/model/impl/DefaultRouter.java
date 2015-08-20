package de.skaliant.wax.core.model.impl;

import java.util.ArrayList;
import java.util.List;

import de.skaliant.wax.core.Router;
import de.skaliant.wax.core.model.ActionInfo;
import de.skaliant.wax.core.model.ControllerInfo;
import de.skaliant.wax.core.model.ControllerManager;
import de.skaliant.wax.core.model.RouterConfig;
import de.skaliant.wax.core.model.RouterResult;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class DefaultRouter
	implements Router
{
	private RouterConfig config = new RouterConfig();
	
	
	public RouterConfig getConfig()
	{
		return config;
	}
	
	
	public RouterResult route(ControllerManager ctrlMan, List<String> path)
	{
		List<String> usedPath = new ArrayList<String>(2);
		List<String> pathInfo = new ArrayList<String>(path);
		RouterResult rr = null;
		ControllerInfo ci = null;
		ActionInfo ai = null;

		if (!path.isEmpty())
		{
			ci = ctrlMan.findForName(path.get(0));
			if (ci != null)
			{
				usedPath.add(pathInfo.remove(0));
			}
			if ((ci != null) && (path.size() > 1))
			{
				ai = ci.findAction(path.get(1));
				if (ai != null)
				{
					usedPath.add(pathInfo.remove(0));
				}
			}
		}
		if (ci == null)
		{
			ci = ctrlMan.findDefaultController();
		}
		if ((ci != null) && (ai == null))
		{
			ai = ci.findDefaultAction();
		}
		if ((ci != null) && (ai != null))
		{
			rr = new RouterResult(ci, ai, usedPath, pathInfo);
		}
		return rr;
	}
}
