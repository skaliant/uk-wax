package de.skaliant.wax.results;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import de.skaliant.wax.core.model.ActionInfo;
import de.skaliant.wax.core.model.Call;
import de.skaliant.wax.core.model.ControllerInfo;
import de.skaliant.wax.core.model.DispatcherInfo;
import de.skaliant.wax.core.model.RequestAttributeRedirectStore;
import de.skaliant.wax.util.URLBuilder;


/**
 * 
 *
 * @author Udo Kastilan
 */
class ControllerRedirectResult
	extends Result
	implements Result.RedirectBuilder
{
	private String protocol = null;
	private URLBuilder ub = new URLBuilder();
	private Class<?> ctrl = null;
	private String actionMethod = null;
	
	
	ControllerRedirectResult(Class<?> ctrl)
	{
		this.ctrl = ctrl;
	}
	
	
	ControllerRedirectResult(Class<?> ctrl, String actionMethod)
	{
		this.ctrl = ctrl;
		this.actionMethod = actionMethod;
	}
	
	
	@Override
	public void handle(Call call)
		throws ServletException, IOException
	{
		HttpServletResponse resp = call.getResponse();
		DispatcherInfo disp = call.getDispatcherInfo();
		ControllerInfo ci = disp.getControllerManager().findForType(ctrl);
		ActionInfo ai = null;
		URLBuilder url = new URLBuilder();
		
		if (ci == null)
		{
			throw new ServletException("Controller class not found: " + ctrl.getName());
		}
		if (actionMethod != null)
		{
			ai = ci.findByMethodName(actionMethod);
		}
		if ((protocol != null) && !call.getScheme().equalsIgnoreCase(protocol.toString()))
		{
			url.setScheme(protocol.toString().toLowerCase());
			url.setHost(call.getHost());
			if (ub.getPort() != null)
			{
				url.setPort(ub.getPort());
			}
		}
		url.appendPath(call.getContextPath()).appendPath(disp.getId()).appendPath(ci.getName());
		if ((ai != null) && (!ai.isDefaultAction()))
		{
			url.appendPath(ai.getName());
		}
		url.appendPath(ub.getPath());
		url.setQueryString(ub.getQueryString());
		url.setAnchor(ub.getAnchor());
		
		RequestAttributeRedirectStore.save(call.getRequest());
		resp.sendRedirect(resp.encodeRedirectURL(url.toString()));
	}


	public RedirectBuilder appendPath(String path)
	{
		ub.appendPath(path);
		return this;
	}


	public RedirectBuilder usingScheme(String protocol)
	{
		this.protocol = protocol;
		return this;
	}
	
	
	public RedirectBuilder atPort(int port)
	{
		ub.setPort(port);
		return this;
	}


	public RedirectBuilder addParam(String name, Object... values)
	{
		ub.addParam(name, values);
		return this;
	}


	public RedirectBuilder anchor(String name)
	{
		ub.setAnchor(name);
		return this;
	}
	
	
	public Result build()
	{
		return this;
	}
}
