package de.skaliant.wax.app.results.binary;

import java.io.IOException;
import java.io.InputStream;


/**
 * 
 *
 * @author Udo Kastilan
 */
public interface StreamProvider {
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	InputStream getStream()
		throws IOException;


	long getSize();


	String getName();


	String getContentType();
}
