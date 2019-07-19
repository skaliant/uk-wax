package de.skaliant.wax.upload;

import java.io.File;
import java.io.IOException;

import de.skaliant.wax.app.FileUpload;


/**
 * Wraps a part for fields of type {@link de.skaliant.wax.app.FileUpload}.
 *
 * @author Udo Kastilan
 */
class FileUploadPartWrapper implements FileUpload {
	/**
	 * lol private part ...
	 */
	private Part part = null;


	FileUploadPartWrapper(Part part) {
		this.part = part;
	}


	@Override
	public String getContentType() {
		return part.getContentType();
	}


	@Override
	public String getFileName() {
		return part.getFileNameRefined();
	}


	@Override
	public long getSize() {
		return part.getSize();
	}


	@Override
	public void writeTo(File destination)
		throws IOException {
		part.writeTo(destination);
	}
}
