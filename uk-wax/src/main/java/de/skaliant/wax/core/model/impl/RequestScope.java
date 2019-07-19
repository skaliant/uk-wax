package de.skaliant.wax.core.model.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import de.skaliant.wax.core.model.Scope;


/**
 * 
 *
 * @author Udo Kastilan
 */
class RequestScope implements Scope<HttpServletRequest> {
	private HttpServletRequest request = null;


	RequestScope(HttpServletRequest request) {
		this.request = request;
	}


	@Override
	public HttpServletRequest getSource() {
		return request;
	}


	@Override
	public Object get(String name) {
		return request.getAttribute(name);
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
			request.setAttribute(name, value);
		}
	}


	@Override
	public boolean has(String name) {
		return get(name) != null;
	}


	@Override
	public void remove(String name) {
		if (has(name)) {
			request.removeAttribute(name);
		}
	}


	@Override
	public List<String> getNames() {
		List<String> ls = new ArrayList<>();

		for (Enumeration<String> e = request.getAttributeNames(); e
				.hasMoreElements();) {
			ls.add(e.nextElement());
		}
		return ls;
	}
}
