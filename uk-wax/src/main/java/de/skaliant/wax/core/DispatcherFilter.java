package de.skaliant.wax.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.skaliant.wax.core.boot.Bootstrapper;
import de.skaliant.wax.util.logging.Log;


/**
 * A servlet filter implementing a dispatcher.
 *
 * @author Udo Kastilan
 */
public class DispatcherFilter implements Filter {
	private FilterConfig filterConfig = null;
	private Dispatcher dispatcher = null;


	@Override
	public void init(FilterConfig filterConfig)
		throws ServletException {
		this.filterConfig = filterConfig;
		Log.init(Environment.getInstance().getConfigLocation(filterConfig.getServletContext()));
		Log.get(DispatcherFilter.class).info("Environment is \"" + Environment.getInstance().getHint() + '"');
		dispatcher = new Dispatcher(Bootstrapper.configure(filterConfig.getServletContext(), filterConfig));
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String servletPath = req.getServletPath();
		String pathInfo = req.getPathInfo();

		/*
		 * For Filters, servlet path and path info seem to be handled in a different
		 * way compared to Servlets. For instance, if the URL mapping is "/de/*", a
		 * path like "/de/more/path" is interpreted for a Servlet as servlet path
		 * "/de" and path info "/more/path", whereas the Filter gets "/de/more/path"
		 * as servlet path and null as the path info. The following lines try to
		 * normalize this behaviour.
		 */
		if (!dispatcher.getInfo().isSuffixPattern()) {
			String mappedPath = dispatcher.getInfo().getPattern().replace("*", "");

			if ((mappedPath.length() > 1) && mappedPath.endsWith("/")) {
				mappedPath = mappedPath.substring(0, mappedPath.length() - 1);
			}
			pathInfo = servletPath.substring(mappedPath.length());
			servletPath = mappedPath;
		}
		dispatcher.handle(filterConfig.getServletContext(), req, resp, servletPath, pathInfo);
	}


	@Override
	public void destroy() {
		//
	}
}
