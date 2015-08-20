package de.skaliant.wax.core.model;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * A location pointing to a part of the web application, typically the action of a controller.
 *
 * @author Udo Kastilan
 */
public interface Call
	extends ParameterProvider
{
	/**
	 * Gets the context path of the web application.
	 * 
	 * @return
	 */
	String getContextPath();

	/**
	 * Protocol scheme, i.e. "http" or "https".
	 * 
	 * @return
	 */
	String getScheme();

	/**
	 * Host name. Primarily taken from the "Host" http header field. If this field is missing, calls <code>request.getServerName()</code>.
	 * Port number gets cut off if present.
	 * 
	 * @return
	 */
	String getHost();

	/**
	 * Is this call a form upload (multipart/form-data)?
	 * 
	 * @return
	 */
	boolean isUpload();
	
	/**
	 * Server port number.
	 * 
	 * @return
	 */
	int getPort();

	/**
	 * Returns the path info of the call. This is any rest of the actual path info which has not lead to a certain controller and
	 * action. For example, if the original path info had been "/main/delete/123", and "/main/delete" could be matched to a controller named
	 * "main" and its action method named "delete", then the path info will be "/123".
	 * 
	 * @return
	 */
	String getPathInfo();


	/**
	 * Get path info elements split into separate strings at the "/". 
	 * 
	 * @return Path info split up
	 */
	List<String> getPathInfoParts();

	/**
	 * Returns the dispatcher path. This is currently taken from <code>request.getServletPath()</code>.
	 * 
	 * @return
	 */
	String getDispatcherPath();

	/**
	 * Returns the complete path which could be matched to the controller/action handling the request. For example, if the context path
	 * was "/app", the dispatcher was matched to "/disp", the original path info had been "/main/delete/123", and "/main/delete" could 
	 * be matched to a controller named "main" and its action method named "delete", then the path will be "/app/disp/main/delete".
	 * 
	 * @return
	 */
	String getPath();
	
	/**
	 * Information on the dispatcher.
	 * 
	 * @return
	 */
	DispatcherInfo getDispatcherInfo();
	
	/**
	 * 
	 * @param path
	 * @throws Exception
	 */
	void dispatch(String path)
		throws ServletException, IOException;

	/**
	 * 
	 * @param webPath
	 * @return
	 */
	String getRealPath(String webPath);

	/**
	 * 
	 * @return
	 */
	String getContentType();
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	String getHeader(String name);
	
	/**
	 * 
	 * @return
	 */
	List<String> getHeaderNames();

	/**
	 * 
	 * @return
	 */
	List<String> getHeaderValues(String name);
	/**
	 * 
	 * @return
	 */
	ControllerInfo getController();
	
	/**
	 * 
	 * @return
	 */
	ActionInfo getAction();
	
	/**
	 * The servlet context (or application scope) as a scope.
	 * 
	 * @return
	 */
	Scope<ServletContext> getApplicationScope();

	/**
	 * The session as a scope.
	 * 
	 * @return
	 */
	Scope<HttpSession> getSessionScope();
	
	/**
	 * The servlet request as a scope.
	 * 
	 * @return
	 */
	Scope<HttpServletRequest> getRequestScope();

	/**
	 * The servlet request.
	 * 
	 * @return
	 */
	HttpServletRequest getRequest();
	
	/**
	 * The servlet response.
	 * 
	 * @return
	 */
	HttpServletResponse getResponse();
}
