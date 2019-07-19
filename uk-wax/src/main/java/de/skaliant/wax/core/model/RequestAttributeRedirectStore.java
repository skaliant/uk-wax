package de.skaliant.wax.core.model;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import de.skaliant.wax.util.MiscUtils;


/**
 * A temporary vault for request scope attributes in case of a redirect result,
 * as the redirection is another request, and all attributes would be lost
 * otherwise.. If a controller returns a redirect result, the dispatcher will
 * copy all request scope attributes into an instance of this class and store it
 * into the session. Before each request, the dispatcher checks for this
 * instance and restores its state.
 *
 * @author Udo Kastilan
 */
public class RequestAttributeRedirectStore {
	private Map<String, Object> attrs = null;


	/**
	 * Empty constructor.
	 */
	public RequestAttributeRedirectStore() {
		this.attrs = new HashMap<>(0);
	}


	/**
	 * Constructor with a pre-filled map. The map will not be copied, but it will
	 * be used directly.
	 * 
	 * @param attrs
	 *          Map
	 */
	public RequestAttributeRedirectStore(Map<String, Object> attrs) {
		this.attrs = attrs;
	}


	/**
	 * Get the content map.
	 * 
	 * @return Content
	 */
	public Map<String, Object> getAttrs() {
		return attrs;
	}


	/**
	 * Sets the content to this map. The map will not be copied, but it will be
	 * used directly.
	 * 
	 * @param attrs
	 */
	public void setAttrs(Map<String, Object> attrs) {
		this.attrs = attrs;
	}


	/**
	 * Convenience method to save the attributes of a request scope into the
	 * session.
	 * 
	 * @param req
	 *          Request
	 */
	public static void save(HttpServletRequest req) {
		Map<String, Object> attrs = new HashMap<>();

		for (String n : MiscUtils.list(req.getAttributeNames(), String.class)) {
			attrs.put(n, req.getAttribute(n));
		}
		if (!attrs.isEmpty()) {
			req.getSession().setAttribute(name(), new RequestAttributeRedirectStore(attrs));
		}
	}


	/**
	 * Restore the attribute state from the session.
	 * 
	 * @param req
	 *          Request
	 */
	public static void restore(HttpServletRequest req) {
		HttpSession sess = req.getSession(false);
		String name = name();

		if ((sess != null) && (sess.getAttribute(name) != null)) {
			RequestAttributeRedirectStore rads = (RequestAttributeRedirectStore) sess.getAttribute(name);

			for (Map.Entry<String, Object> me : rads.getAttrs().entrySet()) {
				req.setAttribute(me.getKey(), me.getValue());
			}
			sess.removeAttribute(name);
		}
	}


	/**
	 * Attribute name of the temporary storage instance in the session.
	 * 
	 * @return Attribute name
	 */
	private static String name() {
		return RequestAttributeRedirectStore.class.getSimpleName();
	}
}
