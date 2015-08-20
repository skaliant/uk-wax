package de.skaliant.wax.results;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import de.skaliant.wax.core.model.Call;
import de.skaliant.wax.results.binary.StreamProvider;
import de.skaliant.wax.util.MiscUtils;


/**
 * 
 *
 * @author Udo Kastilan
 */
class BinaryResult
	extends Result
{
	private StreamProvider provider = null;
	
	
	BinaryResult(StreamProvider provider)
	{
		this.provider = provider;
	}
	
	
	@Override
	public void handle(Call ctx)
		throws IOException
	{
		HttpServletResponse resp = ctx.getResponse();
		String ct = provider.getContentType();
		OutputStream out = null;
		InputStream in = null;
		byte[] buf = new byte[4096];
		int r = 0;
		
		if (provider.getSize() > 0)
		{
			resp.setContentLength((int) provider.getSize());
		}
		resp.setContentType(ct == null ? "application/octet-stream" : ct);
		if (provider.getName() != null)
		{
			resp.setHeader("Content-Disposition", "attachment; filename=" + provider.getName());
		}
		try
		{
			in = provider.getStream();
			out = resp.getOutputStream();
			while ((r = in.read(buf)) != -1)
			{
				out.write(buf, 0, r);
			}
		}
		finally
		{
			MiscUtils.close(in);
		}
	}
}
