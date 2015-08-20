package de.skaliant.wax.core.model.impl;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.skaliant.wax.core.model.ActionInfo;
import de.skaliant.wax.core.model.Call;
import de.skaliant.wax.core.model.ControllerInfo;
import de.skaliant.wax.core.model.DispatcherInfo;
import de.skaliant.wax.core.model.RouterResult;
import de.skaliant.wax.core.model.Scope;
import de.skaliant.wax.upload.MultipartParser;
import de.skaliant.wax.util.MiscUtils;


/**
 * 
 * 
 * @author Udo Kastilan
 */
public class DefaultCall
	implements Call
{
	private ServletContext application = null;
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private DispatcherInfo disp = null;
	private RouterResult resu = null;

	
	public DefaultCall(DispatcherInfo disp, RouterResult resu, ServletContext application, HttpServletRequest request, HttpServletResponse response)
	{
		this.disp = disp;
		this.resu = resu;
		this.application = application;
		this.request = request;
		this.response = response;
	}
	
	
	public boolean isUpload()
	{
		return MultipartParser.isUpload(request.getContentType());
	}
	
	
	public ActionInfo getAction()
	{
		return resu.getAction();
	}
	
	
	public ControllerInfo getController()
	{
		return resu.getController();
	}
	
	
	public DispatcherInfo getDispatcherInfo()
	{
		return disp;
	}


	public List<String> getPathInfoParts()
	{
		return resu.getPathInfo();
	}
	

	public HttpServletResponse getResponse()
	{
		return response;
	}

	
	public String getContentType()
	{
		return request.getContentType();
	}
	
	
	public String getRealPath(String webPath)
	{
		return application.getRealPath(webPath);
	}
	
	
	public void dispatch(String path)
		throws ServletException, IOException
	{
		request.getRequestDispatcher(path).forward(request, response);
	}
	

	public String getScheme()
	{
		return request.getScheme();
	}


	public String getHost()
	{
		String host = request.getHeader("Host");
		int colon = 0;
		
		if (host == null)
		{
			host = request.getServerName();
		}
		if ((colon = host.indexOf(':')) != -1)
		{
			host = host.substring(0, colon);
		}
		return host;
	}


	public int getPort()
	{
		return request.getServerPort();
	}


	public String getContextPath()
	{
		return request.getContextPath();
	}


	public String getPathInfo()
	{
		return MiscUtils.joinPath(resu.getPathInfo());
	}


	public String getDispatcherPath()
	{
		return request.getServletPath();
	}


	public String getPath()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(request.getContextPath()).append(request.getServletPath());
		for (String s : resu.getPath())
		{
			sb.append('/').append(s);
		}
		return sb.toString();
	}


	public String getHeader(String name)
	{
		return request.getHeader(name);
	}

	
	public List<String> getHeaderNames()
	{
		return MiscUtils.list(request.getHeaderNames(), String.class);
	}
	
	
	public List<String> getHeaderValues(String name)
	{
		return MiscUtils.list(request.getHeaders(name), String.class);
	}
	

	public List<String> getParameterNames()
	{
		return MiscUtils.list(request.getParameterNames(), String.class);
	}
	
	
	public boolean isParameterPresent(String name)
	{
		return getParameter(name) != null;
	}


	public String getParameter(String name)
	{
		return request.getParameter(name);
	}


	public String[] getParameterValues(String name)
	{
		return request.getParameterValues(name);
	}


	public Map<String, String[]> getParameters()
	{
		Map<String, String[]> params = new HashMap<String, String[]>();
		
		for (Enumeration<?> e = request.getParameterNames(); e.hasMoreElements(); )
		{
			String n = (String) e.nextElement();
			
			params.put(n, request.getParameterValues(n));
		}
		return params;
	}


	public Scope<ServletContext> getApplicationScope()
	{
		return new ApplicationScope(application);
	}


	public Scope<HttpSession> getSessionScope()
	{
		return new SessionScope(request.getSession());
	}


	public Scope<HttpServletRequest> getRequestScope()
	{
		return new RequestScope(request);
	}


	public HttpServletRequest getRequest()
	{
		return request;
	}
}
