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
class ErrorResult
	extends Result
{
	private String message = null;
	private int code = HttpServletResponse.SC_OK;


	ErrorResult(int code, String message)
	{
		this.message = message;
		this.code = code;
	}


	ErrorResult(int code)
	{
		this.code = code;
	}


	@Override
	public void handle(Call ctx)
		throws ServletException, IOException
	{
		if (message != null)
		{
			ctx.getResponse().sendError(code, message);
		}
		else
		{
			ctx.getResponse().sendError(code);
		}
	}
}
