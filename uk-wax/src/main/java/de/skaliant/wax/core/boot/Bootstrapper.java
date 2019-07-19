package de.skaliant.wax.core.boot;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import de.skaliant.wax.core.ControllerManager;
import de.skaliant.wax.core.Router;
import de.skaliant.wax.core.ViewEngine;
import de.skaliant.wax.core.model.Configurable;
import de.skaliant.wax.core.model.DispatcherInfo;
import de.skaliant.wax.core.model.WaxCore;
import de.skaliant.wax.core.model.impl.DefaultControllerManager;
import de.skaliant.wax.core.model.impl.DefaultRouter;
import de.skaliant.wax.core.model.impl.JspViewEngine;
import de.skaliant.wax.util.ImplementationProvider;
import de.skaliant.wax.util.Injector;
import de.skaliant.wax.util.logging.Log;


/**
 * Initializes the framework plumbing including the following core components:
 * <ul>
 * <li>Router - by default implemented in
 * <code>de.skaliant.wax.core.model.impl.DefaultRouter</code>, but this may be
 * overridden by providing a fully qualified class name as setting
 * "core.controllerManager"</li>
 * <li>ViewEngine - by default implemented in
 * <code>de.skaliant.wax.core.model.impl.JspViewEngine</code>, but this may be
 * overridden by providing a fully qualified class name as setting
 * "core.viewEngine"</li>
 * <li>ControllerManager - by default implemented in
 * <code>de.skaliant.wax.core.model.impl.DefaultControllerManager</code>, but
 * this may be overridden by providing a fully qualified class name as setting
 * "core.controllerManager"</li>
 * </ul>
 * The bootstrap process is intialized through either the DispatcherServlet or
 * the DispatcherFilter. Both call the matching <code>configure()</code> method
 * in their <code>init()</code> methods. The dispatcher is finally registered in
 * an instance of <code>WaxCore</code>, which will be put in the application
 * context under the parameter name "__wax.core__". Avoid using this detail
 * directly, as it may easily change in the future.
 *
 * @author Udo Kastilan
 */
public class Bootstrapper {
	private final static String CORE_PARAM_CONTROLLER_MANAGER = "core.controllerManager";
	private final static String CORE_PARAM_ROUTER = "core.router";
	private final static String CORE_PARAM_VIEW_ENGINE = "core.viewEngine";

	private final static String ATTR_CORE = "__wax.core__";
	private final static String PREFIX_VIEWS = "views.";
	private final static String PREFIX_ROUTER = "router.";
	private final static String PREFIX_CONTROLLER = "controller.";


	public static DispatcherInfo configure(ServletContext app,
			ServletConfig conf) {
		return configure(app, new ServletConfigWrapper(conf));
	}


	public static DispatcherInfo configure(ServletContext app,
			FilterConfig conf) {
		return configure(app, new FilterConfigWrapper(conf));
	}


	private synchronized static DispatcherInfo configure(ServletContext app,
			DispatcherConfigWrapper conf) {
		DispatcherInfo disp = new DispatcherInfo();
		ControllerManager controllerManager;
		ViewEngine viewEngine;
		Router router;
		/*
		 * Load core implementation objects
		 */
		controllerManager = ImplementationProvider.provide(ControllerManager.class,
				conf.getParam(CORE_PARAM_CONTROLLER_MANAGER),
				DefaultControllerManager.class);
		viewEngine = ImplementationProvider.provide(ViewEngine.class,
				conf.getParam(CORE_PARAM_VIEW_ENGINE), JspViewEngine.class);
		router = ImplementationProvider.provide(Router.class,
				conf.getParam(CORE_PARAM_ROUTER), DefaultRouter.class);
		/*
		 * Configure and set implementations for core functions
		 */
		disp.setControllerManager(
				configure(controllerManager, conf, PREFIX_CONTROLLER));
		disp.setRouter(configure(router, conf, PREFIX_ROUTER));
		disp.setViewEngine(configure(viewEngine, conf, PREFIX_VIEWS));
		/*
		 * Try to find the servlet mapping's url pattern as identifier
		 */
		disp.setPattern(PathMappingFinder.find(app, conf));
		/*
		 * Final step: register dispatcher
		 */
		getCore(app).register(disp);

		return disp;
	}


	private static <C extends Configurable<?>> C configure(C c,
			DispatcherConfigWrapper conf, String prefix) {
		Map<String, String> map = new HashMap<>();

		for (String n : conf.getParamNames()) {
			if (n.startsWith(prefix)) {
				map.put(n.substring(prefix.length()), conf.getParam(n));
			}
		}
		new Injector().injectBeanProperties(c.getConfig(), new SimpleParameterProvider(map));

		return c;
	}


	private static WaxCore getCore(ServletContext app) {
		WaxCore core = (WaxCore) app.getAttribute(ATTR_CORE);

		if (core == null) {
			Log.get(Bootstrapper.class).info("Initializing core information");
			app.setAttribute(ATTR_CORE, core = new WaxCore());
		}

		return core;
	}
}
