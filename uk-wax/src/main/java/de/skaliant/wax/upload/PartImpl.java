package de.skaliant.wax.upload;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

import de.skaliant.wax.util.MiscUtils;


/**
 * Implements a Part as in Multipart.
 *
 * @author Udo Kastilan
 */
class PartImpl
	implements Part
{
	private List<HeaderField> header = Collections.emptyList();
	private StorageHandler storage = null;
	private String contentType = null;
	private String fileName = null;
	private String name = null;


	PartImpl(List<HeaderField> header, String name, String fileName, String contentType,
			StorageHandler storage)
	{
		this.name = name;
		this.fileName = fileName;
		this.header = header;
		this.contentType = contentType;
		this.storage = storage;
	}


	public long getSize()
	{
		return storage.getSize();
	}

	
	public String getContentType()
	{
		return contentType;
	}
	

	public String getName()
	{
		return name;
	}


	public List<HeaderField> getHeader()
	{
		return header;
	}

	
	public File getAsFile()
		throws IOException
	{
		return storage.getAsFile();
	}
	

	public String getValue(String encoding)
		throws IOException
	{
		StringBuilder sb = new StringBuilder();
		Reader r = null;
		int c = 0;

		try
		{
			r = new BufferedReader(new InputStreamReader(getStream(), encoding));
			while ((c = r.read()) != -1)
			{
				sb.append((char) c);
			}
		}
		finally
		{
			r = MiscUtils.close(r);
		}
		return sb.toString();
	}


	public void writeTo(File file)
		throws IOException
	{
		storage.writeTo(file);
	}

	
	public String getFileNameRefined()
	{
		if (MiscUtils.notEmpty(fileName))
		{
			char[] seps = { '/', '\\' };
			
			for (char s : seps)
			{
				if (fileName.indexOf(s) != -1)
				{
					fileName = fileName.substring(fileName.lastIndexOf(s) + 1);
				}
			}
			return fileName;
		}
		return null;
	}
	

	public String getFileNameRaw()
	{
		return fileName;
	}


	public boolean isFile()
	{
		return fileName != null;
	}


	public InputStream getStream()
		throws IOException
	{
		return storage.getInputStream();
	}
}
