package de.skaliant.wax.app.results.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class FileProvider implements StreamProvider {
	private String contentType = null;
	private String name = null;
	private File file = null;


	/**
	 * @param file
	 * @param contentType
	 */
	public FileProvider(File file, String contentType) {
		this(file, contentType, file.getName());
	}


	/**
	 * @param file
	 * @param contentType
	 * @param name
	 */
	public FileProvider(File file, String contentType, String name) {
		this.file = file;
		this.contentType = contentType;
		this.name = name;
	}


	@Override
	public InputStream getStream()
		throws IOException {
		return new FileInputStream(file);
	}


	@Override
	public String getContentType() {
		return contentType;
	}


	@Override
	public String getName() {
		return name;
	}


	@Override
	public long getSize() {
		return file.length();
	}
}
