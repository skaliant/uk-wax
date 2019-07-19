package de.skaliant.wax.app;

import java.io.File;
import java.io.IOException;


/**
 * Interface type for declaring controller fields which shall receive uploaded
 * files. If you prefer not to have your classes to be invaded by framework
 * packages, use java.io.File instead; however, this will force the uploading
 * mechanism to create temporary files in any case (as opposed to
 * in-memory-handling), and some information such as content type and file name
 * will be lost then.
 *
 * @author Udo Kastilan
 */
public interface FileUpload {
	/**
	 * Optional information as a hint on the content type as given by the
	 * uploader. This will be extracted from the header fields in the multipart
	 * body, so the information might be missing or wrong.
	 * 
	 * @return Content type or null
	 */
	String getContentType();


	/**
	 * The name of the file as given by the uploader. Some browsers not only send
	 * the name of the file, but also the directory path. This behaviour is
	 * unreliable and should be considered unsafe anyway, so the result of the
	 * method is a refined version of the original information for a more
	 * consistent pattern, containing no path information any more.
	 * 
	 * @return File name or null
	 */
	String getFileName();


	/**
	 * The file's size in bytes.
	 * 
	 * @return Size in bytes
	 */
	long getSize();


	/**
	 * Convenience method: write file data to the path given, possibly overwriting
	 * an existing file.
	 * 
	 * @param destination
	 *          Target path
	 * @throws IOException
	 */
	void writeTo(File destination)
		throws IOException;
}
