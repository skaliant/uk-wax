package de.skaliant.wax.results;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;

import de.skaliant.wax.core.model.Call;
import de.skaliant.wax.results.binary.FileProvider;
import de.skaliant.wax.results.binary.RawProvider;



/**
 * General base for the result of an action. Should not be subclassed, as there is a set of known,
 * internal subclasses. Instead, use the factory methods present in this class.
 *
 * @author Udo Kastilan
 */
public abstract class Result
{
	/**
	 * Show a view named <code>name</code>. This name will be suffixed with the default view suffix
	 * (".jspx") if not already present, and it will be searched in the default view location ("/WEB-INF/views/")
	 * if there's not path information present (i.e. name starts with "/"). Example: the name "main"
	 * will typically result in a view path of "/WEB-INF/views/main.jspx".
	 * 
	 * @param name View name
	 * @return Result instance
	 */
	public static Result view(String name)
	{
		return new ViewResult(name);
	}
	

	/**
	 * Show a view (see {@link #view(String)}), but use a specific HTTP status code instead of 200.
	 * 
	 * @param name View name
	 * @param code HTTP status code
	 * @return Result instance
	 */
	public static Result view(String name, int code)
	{
		return new ViewResult(name, code);
	}
	
	
	/**
	 * Redirect to a certain URL. As this will be delegated to <code>HttpServletResponse.sendRedirect()</code>,
	 * the same rules apply to the URL.
	 * 
	 * @param url URL
	 * @return Result instance
	 */
	public static Result redirect(String url)
	{
		return new RedirectResult(url);
	}
	
	
	/**
	 * Redirect to the default action of the controller referenced by its class. The class reference makes
	 * sure any redirection will still work should the controller be renamed. Further information can be
	 * configured on the RedirectBuilder instance returned.
	 * 
	 * @param ctrl Controller class
	 * @return RedirectBuilder instance
	 */
	public static RedirectBuilder redirect(Class<?> ctrl)
	{
		return new ControllerRedirectResult(ctrl);
	}
	

	/**
	 * Redirect to an action of the controller referenced by its class. As in Java before version 8 there is no such thing as
	 * a method reference, the action must be referenced by its method name as written in source code, not the 
	 * action name given by annotation. Further information can be configured on the RedirectBuilder instance 
	 * returned.
	 * 
	 * @param ctrl Controller class
	 * @param actionMethod Action method name
	 * @return RedirectBuilder instance
	 */
	public static RedirectBuilder redirect(Class<?> ctrl, String actionMethod)
	{
		return new ControllerRedirectResult(ctrl, actionMethod);
	}
	

	/**
	 * Return an object in JSON format along with the content type "application/json". JSON conversion
	 * will cover the following types:
	 * <ul>
	 *   <li>The <code>null</code> reference</li>
	 * 	 <li>Any number (primitive values or subclasses of java.lang.Number)</li>
	 *   <li>Strings</li>
	 *   <li>Booleans</li>
	 *   <li>Arrays and Collections (Set, List) of any of the types listed here</li>
	 *   <li>Maps</li>
	 *   <li>Generic beans will have all properties converted which are both readable and writeable</li>
	 * </ul>
	 * 
	 * @param value Object or null
	 * @return Result instance
	 */
	public static Result json(Object value)
	{
		return new JsonResult(value);
	}
	

	/**
	 * A general HTTP status code, will not transfer any data to the client.
	 * 
	 * @param code HTTP status code
	 * @return Result instance
	 */
	public static Result error(int code)
	{
		return new ErrorResult(code);
	}
	

	/**
	 * Binary data along with the given content type. Will use "application/octet-stream" in case the
	 * content type is null.
	 * 
	 * @param data Data
	 * @param contentType Content type
	 * @return Result instance
	 */
	public static Result binary(byte[] data, String contentType)
	{
		return new BinaryResult(new RawProvider(data, contentType));
	}
	

	/**
	 * Binary data along with the given content type and a name for the transferred file.
	 * If name is not null, the answer to the client will have a "Content-Disposition"
	 * header field set to enforce a download.
	 * 
	 * @param data Data
	 * @param contentType Content type
	 * @return Result instance
	 */
	public static Result binary(byte[] data, String contentType, String name)
	{
		return new BinaryResult(new RawProvider(data, contentType, name));
	}
	

	/**
	 * Binary data along with the given content type. The answer to the client will 
	 * have a "Content-Disposition" header field set to enforce a download, and the
	 * name of the file will be used for the download. This can be overridden by using
	 * {@link #binary(File, String, String)} instead.
	 * 
	 * @param file Data
	 * @param contentType Content type
	 * @return Result instance
	 */
	public static Result binary(File file, String contentType)
	{
		return new BinaryResult(new FileProvider(file, contentType));
	}

	/**
	 * Binary data along with the given content type and a name for the transferred file.
	 * The answer to the client will have a "Content-Disposition" header field set to 
	 * enforce a download.
	 * 
	 * @param file Data
	 * @param contentType Content type
	 * @return Result instance
	 */
	public static Result binary(File file, String contentType, String name)
	{
		return new BinaryResult(new FileProvider(file, contentType, name));
	}
	
	
	/**
	 * Method implementing the answer to the client.
	 * 
	 * @param call
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract void handle(Call call)
			throws ServletException, IOException;
	
	
	/**
	 * Builder object for setting optional information on a redirect. Use <code>build()</code>
	 * to acquire the final Result instance.
	 */
	public static interface RedirectBuilder
	{
		/**
		 * Append partial path information. May start with "/", but doesn't have to. This method
		 * makes sure any part appended will be seperated by a "/" from the former path.
		 * 
		 * @param path Path part
		 * @return A reference to this builder
		 */
		RedirectBuilder appendPath(String path);

		
		/**
		 * Use the given scheme ("http" or "https"). This may lead to an absolute URL to be used 
		 * for the redirect if necessary.
		 * 
		 * @param scheme URL scheme for the redirect target
		 * @return A reference to this builder
		 */
		RedirectBuilder usingScheme(String scheme);

		/**
		 * Give a certain port number. This may lead to an absolute URL to be used for the redirect
		 * if necessary.
		 * 
		 * @param port Port number
		 * @return A reference to this builder
		 */
		RedirectBuilder atPort(int port);

		/**
		 * Add a HTTP parameter to the redirect URL.
		 * 
		 * @param name Parameter name
		 * @param values Value(s), will be converted to Strings
		 * @return
		 */
		RedirectBuilder addParam(String name, Object... values);

		/**
		 * Add an anchor (i.e. "#name").
		 * 
		 * @param name Anchor name
		 * @return A reference to this builder
		 */
		RedirectBuilder anchor(String name);

		/**
		 * Generate the final Result instance.
		 * 
		 * @return Result instance
		 */
		Result build();
	}
}
