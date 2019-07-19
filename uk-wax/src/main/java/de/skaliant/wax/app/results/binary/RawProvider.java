package de.skaliant.wax.app.results.binary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class RawProvider implements StreamProvider {
	private String contentType = null;
	private String name = null;
	private byte[] data = null;


	/**
	 * @param data
	 * @param contentType
	 */
	public RawProvider(byte[] data, String contentType) {
		this(data, contentType, null);
	}


	/**
	 * @param data
	 * @param contentType
	 * @param name
	 */
	public RawProvider(byte[] data, String contentType, String name) {
		this.data = data;
		this.contentType = contentType;
		this.name = name;
	}


	@Override
	public InputStream getStream()
		throws IOException {
		return new ByteArrayInputStream(data);
	}


	@Override
	public long getSize() {
		return data.length;
	}


	@Override
	public String getName() {
		return name;
	}


	@Override
	public String getContentType() {
		return contentType;
	}
}
