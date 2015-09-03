package de.skaliant.wax.app.results;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import de.skaliant.wax.core.model.Call;
import de.skaliant.wax.core.model.RequestAttributeRedirectStore;


/**
 * 
 *
 * @author Udo Kastilan
 */
class RedirectResult
	extends Result
{
	private String url = null;
	
	
	RedirectResult(String url)
	{
		this.url = url;
	}


	@Override
	public void handle(Call call)
		throws ServletException, IOException
	{
		HttpServletResponse resp = call.getResponse();
		/*
		 * Save any given request attributes into the session, so they survive the redirect
		 */
		RequestAttributeRedirectStore.save(call.getRequest());
		resp.sendRedirect(resp.encodeRedirectURL(url));
	}
}
