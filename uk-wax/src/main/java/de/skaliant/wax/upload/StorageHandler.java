package de.skaliant.wax.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Storage handlers take care of data for one part each, both for collecting
 * during parse process as well as for accessing the data afterwards.
 *
 * @author Udo Kastilan
 */
interface StorageHandler
{
	/**
	 * Gets an output stream for data during parse process.
	 * 
	 * @return
	 * @throws IOException
	 */
	OutputStream getOutputStream()
		throws IOException;


	/**
	 * Gets an input stream for accessing the data. Calling this method
	 * automatically closes the output stream, so there is no way to write more
	 * data afterwards. Call this method only if all data has been collected.
	 * 
	 * @return
	 * @throws IOException
	 */
	InputStream getInputStream()
		throws IOException;


	/**
	 * Copy all data to the given file.
	 * 
	 * @param file
	 * @throws IOException
	 */
	void writeTo(File file)
		throws IOException;


	/**
	 * Close any open streams and delete temporary files.
	 */
	void cleanup();


	/**
	 * Gets the size of this store in bytes, i.e. the size of the part.
	 * 
	 * @return
	 */
	long getSize();
}
