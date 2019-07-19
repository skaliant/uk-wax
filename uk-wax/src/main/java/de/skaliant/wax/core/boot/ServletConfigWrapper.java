package de.skaliant.wax.core.boot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;


/**
 * Comnfiguration wrapper for servlet config.
 *
 * @author Udo Kastilan
 */
class ServletConfigWrapper implements DispatcherConfigWrapper {
	private List<String> names = new ArrayList<>();
	private ServletConfig conf = null;


	ServletConfigWrapper(ServletConfig conf) {
		this.conf = conf;
		for (Object o : Collections
				.list((Enumeration<?>) conf.getInitParameterNames())) {
			names.add(o.toString());
		}
	}


	@Override
	public List<String> getParamNames() {
		return names;
	}


	@Override
	public String getParam(String name) {
		return conf.getInitParameter(name);
	}


	@Override
	public String getDispatcherName() {
		return conf.getServletName();
	}


	@Override
	public DispatcherType getType() {
		return DispatcherType.SERVLET;
	}
}
