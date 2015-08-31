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

import de.skaliant.wax.core.model.Bootstrapper;
import de.skaliant.wax.util.logging.Log;


/**
 * A servlet filter implementing a dispatcher.
 *
 * @author Udo Kastilan
 */
public class DispatcherFilter
	implements Filter
{
	private FilterConfig filterConfig = null;
	private Dispatcher dispatcher = null;
	
	
	public void init(FilterConfig filterConfig)
		throws ServletException
	{
		this.filterConfig = filterConfig;
		Log.init(Environment.getInstance().getConfigLocation(filterConfig.getServletContext()));
		Log.get(DispatcherFilter.class).info("Environment is \"" + Environment.getInstance().getHint() + '"');
		dispatcher = new Dispatcher(Bootstrapper.configure(filterConfig.getServletContext(), filterConfig));
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		dispatcher.handle(filterConfig.getServletContext(), req, resp, req.getServletPath());
		/*
		 * TODO pass through non-handled calls
		 */
	}

	
	public void destroy()
	{
		//
	}
}
