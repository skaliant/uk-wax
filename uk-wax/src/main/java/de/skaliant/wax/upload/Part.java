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
	 * in the header usually is present, but empty. The MultipartParser will normalize
	 * this to a null value. However, in order to check for a file being present, you
	 * should compare {@link #getSize()} to being greater than 0 instead.
	 * instead.
	 * 
	 * @return
	 */
	String getFileNameRaw();
	
	/**
	 * File name. This is the refined value of the raw file name data the browser originally sent.
	 * All path information, if present, will be discarded, and the result will only be not null,
	 * if there actually is a file name. No need to check for both null and empty String in this case.
	 * 
	 * @return
	 */
	String getFileNameRefined();

	
	/**
	 * The content type as given by the uploader. This information might be missing or simply
	 * be wrong, so handle with care.
	 * 
	 * @return
	 */
	String getContentType();
	

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
	 * Writes content to a temporary file, if that hasn't been done before anyway, and
	 * returns a reference to this file. These files will be cleaned up via the MultipartParser.
	 * 
	 * @return Temporary file
	 * @throws IOException
	 */
	File getAsFile()
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
