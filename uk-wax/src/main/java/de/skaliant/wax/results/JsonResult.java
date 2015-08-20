package de.skaliant.wax.results;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import de.skaliant.wax.core.model.Call;
import de.skaliant.wax.util.Json;


/**
 * 
 *
 * @author Udo Kastilan
 */
class JsonResult
	extends Result
{
	private Object value = null;
	
	
	JsonResult(Object value)
	{
		this.value = value;
	}
	

	@Override
	public void handle(Call ctx)
		throws ServletException, IOException
	{
		HttpServletResponse resp = ctx.getResponse();
		
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().write(Json.convert(value));
	}

}
