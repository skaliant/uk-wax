package de.skaliant.wax.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.skaliant.wax.app.Config;
import de.skaliant.wax.app.Guardian;
import de.skaliant.wax.app.results.Result;
import de.skaliant.wax.core.model.Call;
import de.skaliant.wax.core.model.DispatcherInfo;
import de.skaliant.wax.core.model.MapBasedParameterProvider;
import de.skaliant.wax.core.model.ParameterProvider;
import de.skaliant.wax.core.model.RequestAttributeRedirectStore;
import de.skaliant.wax.core.model.RouterResult;
import de.skaliant.wax.core.model.impl.DefaultCall;
import de.skaliant.wax.upload.MultipartParser;
import de.skaliant.wax.upload.UploadInjector;
import de.skaliant.wax.util.Injector;
import de.skaliant.wax.util.Pair;
import de.skaliant.wax.util.logging.Log;


/**
 * A neutral form of the dispatcher. May be activated by a servlet or a filter.
 * 
 * @author Udo Kastilan
 */
public class Dispatcher {
	private final Log LOG = Log.get(Dispatcher.class);
	private UploadInjector uploadInjector = new UploadInjector();
	private Injector injector = new Injector();
	/**
	 * Information on this dispatcher
	 */
	private DispatcherInfo info = null;


	/**
	 * Create the instance, give information.
	 * 
	 * @param info
	 *          Dispatcher information
	 */
	public Dispatcher(DispatcherInfo info) {
		this.info = info;
	}


	/**
	 * Returns the info object.
	 * 
	 * @return
	 */
	public DispatcherInfo getInfo() {
		return info;
	}


	/**
	 * Handle a call. Typically, a servlet or a filter will be activated and
	 * forward to this method. Servlet instances should give
	 * <code>request.getPathInfo()</code> as path, while Filter instances should
	 * give <code>request.getServletPath()</code>. This extra path information
	 * will be examined by the router in order to find a controller/action
	 * responsible for the request.
	 * 
	 * @param app
	 *          ServletContext (application scope)
	 * @param req
	 *          Request
	 * @param resp
	 *          Response
	 * @param dispatcherPath
	 *          The URI path which triggered the dispatcher
	 * @param path
	 *          Extra path information (any rest after whatever has triggered the
	 *          servlet/filter)
	 * @throws ServletException
	 * @throws IOException
	 */
	public void handle(ServletContext app, HttpServletRequest req,
			HttpServletResponse resp, String dispatcherPath, String pathInfo)
		throws ServletException, IOException {
		RouterResult rr = info.getRouter().route(info, dispatcherPath, pathInfo);

		if (rr == null) {
			// Nothing found, not even the default controller? 404!
			LOG.info("Path [" + req.getRequestURI()
					+ "]: no controller found, sending 404");
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			Call call = new DefaultCall(info, rr, app, req, resp);
			/*
			 * Combine remaining path parts and handle action
			 */
			LOG.info("Path [" + req.getRequestURI()
					+ "]: will be handled by controller [" + rr.getController().getName()
					+ "], action [" + rr.getAction().getName() + "] - "
					+ rr.getController().getType().getName() + ":"
					+ rr.getAction().getMethod().getName() + "(); path info is "
					+ rr.getPathInfo());
			try {
				handle(call);
			}
			catch (Exception ex) {
				LOG.error("Error handling call", ex);
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						ex.getMessage());
			}
		}
	}


	/**
	 * Handles a controller action.
	 * 
	 * @param call
	 *          Call information
	 * @throws IOException
	 *           Error when redirecting/responding
	 */
	private void handle(Call call)
		throws Exception {
		Class<? extends Guardian> guardian = call.getAction().getGuardian();
		ParameterProvider params = call;
		MultipartParser mup = null;
		Object[] args = null;
		Object ctrl = null;
		Object result = null;
		long time = 0;

		if (guardian == null) {
			guardian = call.getController().getGuardian();
		}
		try {
			/*
			 * Check access privilege if any
			 */
			if (guardian != null) {
				Guardian guard = guardian.newInstance();
				Result guardResult = guard.admit(call);

				if (guardResult != null) {
					/*
					 * Default response; real Guardians however are expected to redirect
					 * to i.e. a login page via RedirectException
					 */
					guardResult.handle(call);
					return;
				}
			}
			/*
			 * Try to restore any request attributes which might have been stored into
			 * the session to survive a redirect
			 */
			RequestAttributeRedirectStore.restore(call.getRequest());
			/*
			 * Create an instance of the controller and the Injector responsible for
			 * setting bean properties and call arguments for HTTP parameters and
			 * pathInfo
			 */
			ctrl = call.getController().newInstance();
			/*
			 * 1.) Set magic parameters: Request, Response, Application
			 * (ServletContext), and Config; also: set path info elements and
			 * parameters
			 */
			List<Pair<Class<?>, ?>> spex = new ArrayList<>();

			spex.add(new Pair<Class<?>, Object>(ServletContext.class,
					call.getApplicationScope().getSource()));
			spex.add(new Pair<Class<?>, Object>(HttpServletRequest.class,
					call.getRequestScope().getSource()));
			spex.add(new Pair<Class<?>, Object>(HttpServletResponse.class,
					call.getResponse()));
			spex.add(new Pair<Class<?>, Object>(Config.class, info.findConfig(call)));
			/*
			 * 2.) Check for an upload; in this case, we need to handle parameters a
			 * different way and inject uploaded blobs
			 */
			if (call.isUpload()) {
				mup = MultipartParser.create(call.getContentType(),
						call.getRequest().getInputStream());
				// TODO make encoding configurable
				params = MapBasedParameterProvider.create(call, mup, "utf-8");
				uploadInjector.injectUploads(ctrl, mup);
			}
			/*
			 * 3.) Inject parameters and magic values to bean properties
			 */
			injector.injectBeanProperties(ctrl, params, spex);
			/*
			 * 4.) Inject parameters, magic values, and path information into method
			 * arguments
			 */
			args = injector.injectMethodArguments(call.getAction().getMethod(),
					params, call.getPathInfoParts(), spex);
			/*
			 * 5.) Init method(s)
			 */
			for (Method meth : call.getController().getInitMethods()) {
				meth.invoke(ctrl);
			}
			/*
			 * 6.) Done preparing, now call action method
			 */
			time = System.currentTimeMillis();
			result = call.getAction().getMethod().invoke(ctrl, args);
			time = System.currentTimeMillis() - time;
			LOG.info("Handled in [" + time + "] ms");
			/*
			 * 7.) Exit method(s)
			 */
			for (Method meth : call.getController().getExitMethods()) {
				meth.invoke(ctrl);
			}
			/*
			 * 8.) Use the result or bail out
			 */
			if (result instanceof Result) {
				Result.class.cast(result).handle(call);
			} else if (result != null) {
				Result.view(result.toString()).handle(call);
			} else {
				throw new Exception("No result defined");
			}
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			/*
			 * Clean up upload handling, if necessary (delete temporary files)
			 */
			if (mup != null) {
				mup.cleanup();
			}
		}
	}
}
