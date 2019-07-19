package de.skaliant.wax.util;

/**
 * 
 * 
 * @author Udo Kastilan
 */
public class URLBuilder {
	public final static String HTTPS = "https";
	public final static String HTTP = "http";
	private final static String DEFAULT_SCHEME = HTTP;
	private final static int MIN_PORT = 0;
	private final static int MAX_PORT = 65535;

	private QueryStringBuilder qsb = new QueryStringBuilder();
	private StringBuilder path = new StringBuilder("/");
	private String scheme = null;
	private String host = null;
	private String anchor = null;
	private Integer port = null;


	public String getScheme() {
		return scheme;
	}


	public void setQueryString(String queryString) {
		qsb = new QueryStringBuilder(queryString);
	}


	public String getQueryString() {
		return qsb.toString();
	}


	public void setScheme(String scheme) {
		this.scheme = scheme;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public String getAnchor() {
		return anchor;
	}


	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}


	public String getPath() {
		return path.toString();
	}


	public void setPath(String path) {
		if (MiscUtils.isEmpty(path)) {
			path = String.valueOf('/');
		}
		this.path.delete(0, this.path.length());
		if (path.charAt(0) != '/') {
			this.path.append('/');
		}
		this.path.append(path);
	}


	public URLBuilder appendPath(String path) {
		if (MiscUtils.isEmpty(path)) {
			return this;
		}
		if (path.charAt(0) == '/') {
			if (this.path.charAt(this.path.length() - 1) == '/') {
				this.path.append(path, 1, path.length());
			} else {
				this.path.append(path);
			}
		} else if (this.path.charAt(this.path.length() - 1) == '/') {
			this.path.append(path);
		} else {
			this.path.append('/').append(path);
		}

		return this;
	}


	public Integer getPort() {
		return port;
	}


	public void setPort(Integer port) {
		if ((port != null) && ((port < MIN_PORT) || (port > MAX_PORT))) {
			throw new IllegalArgumentException("Invalid port number: " + port);
		}
		this.port = port;
	}


	public void addParam(String name, Object... values) {
		qsb.append(name, values);
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (host != null) {
			sb.append(scheme == null ? DEFAULT_SCHEME : scheme).append("://").append(host);
			if (port != null) {
				sb.append(':').append(port);
			}
		}
		sb.append(path).append(qsb);
		if (anchor != null) {
			sb.append('#').append(anchor);
		}
		return sb.toString();
	}
}
