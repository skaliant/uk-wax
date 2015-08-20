package de.skaliant.wax.util;

/**
 * 
 * 
 * @author Udo Kastilan
 */
public class URLBuilder
{
	private QueryStringBuilder qsb = new QueryStringBuilder();
	private StringBuilder path = new StringBuilder("/");
	private String scheme = null;
	private String host = null;
	private String anchor = null;
	private Integer port = null;


	public String getScheme()
	{
		return scheme;
	}
	
	
	public void setQueryString(String queryString)
	{
		qsb = new QueryStringBuilder(queryString);
	}
	
	
	public String getQueryString()
	{
		return qsb.toString();
	}


	public void setScheme(String scheme)
	{
		this.scheme = scheme;
	}


	public String getHost()
	{
		return host;
	}


	public void setHost(String host)
	{
		this.host = host;
	}


	public String getAnchor()
	{
		return anchor;
	}


	public void setAnchor(String anchor)
	{
		this.anchor = anchor;
	}


	public String getPath()
	{
		return path.toString();
	}


	public void setPath(String path)
	{
		if (path == null || (path.length() == 0))
		{
			path = String.valueOf('/');
		}
		this.path.delete(0, this.path.length());
		if (path.charAt(0) != '/')
		{
			this.path.append('/');
		}
		this.path.append(path);
	}
	
	
	public URLBuilder appendPath(String path)
	{
		if (path == null || (path.length() == 0))
		{
			return this;
		}
		if (path.charAt(0) == '/')
		{
			if (this.path.charAt(this.path.length() - 1) == '/')
			{
				this.path.append(path, 1, path.length());
			}
			else
			{
				this.path.append(path);
			}
		}
		else if (this.path.charAt(this.path.length() - 1) == '/')
		{
			this.path.append(path);
		}
		else
		{
			this.path.append('/').append(path);
		}
		return this;
	}


	public Integer getPort()
	{
		return port;
	}


	public void setPort(Integer port)
	{
		if ((port != null) && ((port.intValue() < 0) || (port.intValue() > 65535)))
		{
			throw new IllegalArgumentException("Invalid port number: " + port);
		}
		this.port = port;
	}

	
	public void addParam(String name, Object... values)
	{
		qsb.append(name, values);
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		if (host != null)
		{
			sb.append(scheme == null ? "http" : scheme).append("://").append(host);
			if (port != null)
			{
				sb.append(':').append(port);
			}
		}
		sb.append(path).append(qsb);
		if (anchor != null)
		{
			sb.append('#').append(anchor);
		}
		return sb.toString();
	}
}
