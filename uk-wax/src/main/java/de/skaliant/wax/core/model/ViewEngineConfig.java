package de.skaliant.wax.core.model;

/**
 * Configuration object for the ViewEngine. The Bootstrapper will try to inject
 * any init parameter prefixed with "views." into the properties of this bean.
 * In case you implement a view engine of your own and need more properties,
 * subclass this class and the
 * {@link de.skaliant.wax.core.ViewEngine#getConfig()} method and return an
 * instance of your config bean instead.
 *
 * @author Udo Kastilan
 */
public class ViewEngineConfig {
	private String location = null;
	private String suffix = null;


	/**
	 * Location path of the views. By default (JspViewEngine), this is an absolute
	 * path which will be interpreted as part of the web application and extended
	 * through <code>ServletContext.getRealPath()</code>, e.g. "/WEB-INF/views/"
	 * (the default location of JspViewEngine). However, this interpretation is
	 * done by the view engine, so you are free to implement system-absolute paths
	 * as well, making it possible to deploy views independent of the web
	 * application.
	 * 
	 * @return View location
	 */
	public String getLocation() {
		return location;
	}


	/**
	 * See {@link #getLocation()}.
	 * 
	 * @param location
	 *          View location path
	 */
	public void setLocation(String location) {
		this.location = location;
	}


	/**
	 * The file suffix for views including the dot, e.g. ".jspx". Controllers
	 * should only return a name of the view without suffix, making it easier to
	 * change the view implementation later on.
	 * 
	 * @return View file suffix
	 */
	public String getSuffix() {
		return suffix;
	}


	/**
	 * See {@link #getSuffix()}.
	 * 
	 * @param suffix
	 *          View file suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
