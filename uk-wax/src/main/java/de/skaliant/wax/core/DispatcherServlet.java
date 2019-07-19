package de.skaliant.wax.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.skaliant.wax.core.boot.Bootstrapper;
import de.skaliant.wax.util.logging.Log;


/**
 * A servlet implementing the dispatcher.
 *
 * @author Udo Kastilan
 */
public class DispatcherServlet extends HttpServlet {
	private Dispatcher dispatcher = null;


	@Override
	public void init()
		throws ServletException {
		Log.init(Environment.getInstance().getConfigLocation(getServletContext()));
		Log.get(DispatcherServlet.class).info("Environment is \"" + Environment.getInstance().getHint() + '"');
		dispatcher = new Dispatcher(Bootstrapper.configure(getServletContext(), getServletConfig()));
	}


	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		doGet(req, resp);
	}


	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		dispatcher.handle(getServletContext(), req, resp, req.getServletPath(), req.getPathInfo());
	}
}
