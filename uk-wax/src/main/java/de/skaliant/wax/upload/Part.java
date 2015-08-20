package de.skaliant.wax.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * Represents a single part of a multipart upload. Every part has its own header
 * lines, usually a name (originating from the HTML form field name), possibly a
 * filename (in case of input type file), and possibly some data.
 *
 * @author Udo Kastilan
 */
public interface Part
{
	/**
	 * Part name. This is the name you gave your form field. May be null or empty.
	 * 
	 * @return
	 */
	String getName();


	/**
	 * File name. This is the original name of the file the user selected for
	 * uploading. Depending on the browser, this may contain path information as
	 * well. For file fields which are empty on form submitting, the file name
	 * usually is empty, not null; make sure to check for both anyway. However,
	 * checking this information is not a good way to check whether a file has
	 * been selected by the user; compare getSize() for being greater than 0
	 * instead.
	 * 
	 * @return
	 */
	String getFileName();


	/**
	 * Determins whether this had been a file field. Currently, the file name is
	 * checked for null. If the browser decides to keep this information for
	 * itself, this check is not useful.
	 * 
	 * @return
	 */
	boolean isFile();


	/**
	 * Returns the value of the part as String. This is the best way to access
	 * content of form fields which are not of type file. As browsers usually do
	 * not send content type information for these fields, there is no way of
	 * determining encoding automatically. You need to take encoding for your HTML
	 * form page into consideration.
	 * 
	 * @param encoding
	 *          Charset name
	 * @return String value
	 * @throws IOException
	 */
	String getValue(String encoding)
		throws IOException;


	/**
	 * Delivers all header fields of this part.
	 * 
	 * @return
	 */
	List<HeaderField> getHeader();


	/**
	 * Gets an input stream to access the data of this part. Every call to this
	 * method should get a fresh input stream starting at byte 0 without
	 * interfering with input streams which had been opened before.
	 * 
	 * @return
	 * @throws IOException
	 */
	InputStream getStream()
		throws IOException;


	/**
	 * Write content of this part to a file. In case content is held as temp file,
	 * this call might be more efficient than copying byte by byte (currently, it
	 * is not, but this may change in future).
	 * 
	 * @param file
	 * @throws IOException
	 */
	void writeTo(File file)
		throws IOException;


	/**
	 * Gets data size of this part in bytes.
	 * 
	 * @return
	 */
	long getSize();
}
