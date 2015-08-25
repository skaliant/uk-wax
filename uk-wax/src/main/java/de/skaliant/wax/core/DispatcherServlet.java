package de.skaliant.wax.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.skaliant.wax.core.model.Bootstrapper;


/**
 * A servlet implementing the dispatcher.
 *
 * @author Udo Kastilan
 */
public class DispatcherServlet
	extends HttpServlet
{
	private Dispatcher dispatcher = null;


	@Override
	public void init()
		throws ServletException
	{
		dispatcher = new Dispatcher(Bootstrapper.configure(getServletContext(), getServletConfig()));
	}


	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		doGet(req, resp);
	}


	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		dispatcher.handle(getServletContext(), req, resp, req.getPathInfo());
	}
}
