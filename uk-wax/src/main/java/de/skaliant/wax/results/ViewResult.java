package de.skaliant.wax.results;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import de.skaliant.wax.core.model.Call;


/**
 * 
 *
 * @author Udo Kastilan
 */
class ViewResult
	extends Result
{
	private String name = null;
	private int code = HttpServletResponse.SC_OK;


	ViewResult(String name, int code)
	{
		this.name = name;
		this.code = code;
	}


	ViewResult(String name)
	{
		this(name, HttpServletResponse.SC_OK);
	}


	int getCode()
	{
		return code;
	}


	String getName()
	{
		return name;
	}


	@Override
	public void handle(Call call)
		throws ServletException, IOException
	{
		call.getResponse().setStatus(code);
		call.getDispatcherInfo().getViewEngine().render(name, call);
	}
}