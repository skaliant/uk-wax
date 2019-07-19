package de.skaliant.wax.util;

import static de.skaliant.wax.util.MiscUtils.isNotBlank;
import static de.skaliant.wax.util.MiscUtils.isNotEmpty;
import static de.skaliant.wax.util.MiscUtils.splitAtAnyCharOf;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class QueryStringBuilder {
	private StringBuilder sb = new StringBuilder();
	private Charset encoding;
	private char sep = '?';


	public QueryStringBuilder(String query, Charset encoding) {
		this.encoding = encoding;
		if (isNotEmpty(query)) {
			for (String s : splitAtAnyCharOf(query, "?&")) {
				if (isNotBlank(s)) {
					sb.append(sep).append(s);
					sep = '&';
				}
			}
		}
	}


	public QueryStringBuilder(Charset encoding) {
		this.encoding = encoding;
	}


	public QueryStringBuilder(String query) {
		this(query, Charset.forName("utf-8"));
	}


	public QueryStringBuilder() {
		this(null, Charset.forName("utf-8"));
	}


	public QueryStringBuilder append(String name, Object... values) {
		try {
			String nameEncoded = URLEncoder.encode(name, encoding.name());

			if (values.length == 0) {
				appendEncoded(nameEncoded, null);
			} else {
				for (Object o : values) {
					String value = null;

					if (o != null) {
						value = URLEncoder.encode(o.toString(), encoding.name());
					}
					appendEncoded(nameEncoded, value);
				}
			}
		}
		catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}

		return this;
	}


	private void appendEncoded(String name, String value) {
		sb.append(sep).append(name);
		if (value != null) {
			sb.append('=').append(value);
		}
		sep = '&';
	}


	@Override
	public String toString() {
		return sb.toString();
	}
}
