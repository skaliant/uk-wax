package de.skaliant.wax.core.model.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpSession;

import de.skaliant.wax.core.model.Scope;


/**
 * 
 *
 * @author Udo Kastilan
 */
class SessionScope implements Scope<HttpSession> {
	private HttpSession sess = null;


	SessionScope(HttpSession sess) {
		this.sess = sess;
	}


	@Override
	public HttpSession getSource() {
		return sess;
	}


	@Override
	public Object get(String name) {
		return sess.getAttribute(name);
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
			sess.setAttribute(name, value);
		}
	}


	@Override
	public boolean has(String name) {
		return get(name) != null;
	}


	@Override
	public void remove(String name) {
		if (has(name)) {
			sess.removeAttribute(name);
		}
	}


	@Override
	public List<String> getNames() {
		List<String> ls = new ArrayList<>();

		for (Enumeration<String> e = sess.getAttributeNames(); e
				.hasMoreElements();) {
			ls.add((String) e.nextElement());
		}
		return ls;
	}
}
