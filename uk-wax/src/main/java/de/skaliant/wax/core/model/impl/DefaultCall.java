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
public class DefaultCall implements Call {
	private ServletContext application = null;
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private DispatcherInfo disp = null;
	private RouterResult resu = null;


	public DefaultCall(DispatcherInfo disp, RouterResult resu,
			ServletContext application, HttpServletRequest request,
			HttpServletResponse response) {
		this.disp = disp;
		this.resu = resu;
		this.application = application;
		this.request = request;
		this.response = response;
	}


	@Override
	public boolean isUpload() {
		return MultipartParser.isUpload(request.getContentType());
	}


	@Override
	public ActionInfo getAction() {
		return resu.getAction();
	}


	@Override
	public ControllerInfo getController() {
		return resu.getController();
	}


	@Override
	public DispatcherInfo getDispatcherInfo() {
		return disp;
	}


	@Override
	public List<String> getPathInfoParts() {
		return resu.getPathInfo();
	}


	@Override
	public HttpServletResponse getResponse() {
		return response;
	}


	@Override
	public String getContentType() {
		return request.getContentType();
	}


	@Override
	public String getRealPath(String webPath) {
		return application.getRealPath(webPath);
	}


	@Override
	public void dispatch(String path)
		throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}


	@Override
	public String getScheme() {
		return request.getScheme();
	}


	@Override
	public String getHost() {
		String host = request.getHeader("Host");
		int colon = 0;

		if (host == null) {
			host = request.getServerName();
		}
		if ((colon = host.indexOf(':')) != -1) {
			host = host.substring(0, colon);
		}
		return host;
	}


	@Override
	public int getPort() {
		return request.getServerPort();
	}


	@Override
	public String getContextPath() {
		return request.getContextPath();
	}


	@Override
	public String getPathInfo() {
		return joinPath(resu.getPathInfo());
	}


	@Override
	public String getDispatcherPath() {
		return request.getServletPath();
	}


	@Override
	public String getPath() {
		StringBuilder sb = new StringBuilder();

		sb.append(request.getContextPath()).append(request.getServletPath());
		for (String s : resu.getPath()) {
			sb.append('/').append(s);
		}
		return sb.toString();
	}


	@Override
	public String getHeader(String name) {
		return request.getHeader(name);
	}


	@Override
	public List<String> getHeaderNames() {
		return MiscUtils.list(request.getHeaderNames(), String.class);
	}


	@Override
	public List<String> getHeaderValues(String name) {
		return MiscUtils.list(request.getHeaders(name), String.class);
	}


	@Override
	public List<String> getParameterNames() {
		return MiscUtils.list(request.getParameterNames(), String.class);
	}


	@Override
	public boolean isParameterPresent(String name) {
		return getParameter(name) != null;
	}


	@Override
	public String getParameter(String name) {
		return request.getParameter(name);
	}


	@Override
	public String[] getParameterValues(String name) {
		return request.getParameterValues(name);
	}


	@Override
	public Map<String, String[]> getParameters() {
		Map<String, String[]> params = new HashMap<>();

		for (Enumeration<?> e = request.getParameterNames(); e.hasMoreElements();) {
			String n = (String) e.nextElement();

			params.put(n, request.getParameterValues(n));
		}
		return params;
	}


	@Override
	public Scope<ServletContext> getApplicationScope() {
		return new ApplicationScope(application);
	}


	@Override
	public Scope<HttpSession> getSessionScope() {
		return new SessionScope(request.getSession());
	}


	@Override
	public Scope<HttpServletRequest> getRequestScope() {
		return new RequestScope(request);
	}


	@Override
	public HttpServletRequest getRequest() {
		return request;
	}


	private static String joinPath(List<String> parts) {
		if ((parts == null) || parts.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();

		for (String s : parts) {
			sb.append('/').append(s);
		}

		return sb.toString();
	}
}
