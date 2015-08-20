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
	private String fileName = null;
	private String name = null;


	PartImpl(List<HeaderField> header, String name, String fileName,
			StorageHandler storage)
	{
		this.name = name;
		this.fileName = fileName;
		this.header = header;
		this.storage = storage;
	}


	public long getSize()
	{
		return storage.getSize();
	}


	public String getName()
	{
		return name;
	}


	public List<HeaderField> getHeader()
	{
		return header;
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


	public String getFileName()
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
