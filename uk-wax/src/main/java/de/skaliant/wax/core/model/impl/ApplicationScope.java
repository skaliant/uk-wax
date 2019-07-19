package de.skaliant.wax.core.model.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;

import de.skaliant.wax.core.model.Scope;


/**
 * 
 *
 * @author Udo Kastilan
 */
class ApplicationScope implements Scope<ServletContext> {
	private ServletContext app = null;


	ApplicationScope(ServletContext app) {
		this.app = app;
	}


	@Override
	public ServletContext getSource() {
		return app;
	}


	@Override
	public Object get(String name) {
		return app.getAttribute(name);
	}


	@Override
	public <T> T get(String name, Class<T> type) {
		return type.cast(get(name));
	}


	@Override
	public void set(String name, Object value) {
		if (value == null) {
			remove(name);
		} else {
			app.setAttribute(name, value);
		}
	}


	@Override
	public boolean has(String name) {
		return get(name) != null;
	}


	@Override
	public void remove(String name) {
		if (has(name)) {
			app.removeAttribute(name);
		}
	}


	@Override
	public List<String> getNames() {
		List<String> ls = new ArrayList<>();

		for (Enumeration<String> e = app.getAttributeNames(); e
				.hasMoreElements();) {
			ls.add(e.nextElement());
		}
		return ls;
	}
}
