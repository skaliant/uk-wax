package de.skaliant.wax.upload;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.skaliant.wax.util.MiscUtils;


/**
 * Storage handler implementation for a dynamic, automatic switch between memory
 * buffering and temp file usage as needed. Switch to temp file will be done
 * once a threshold value is reached.
 *
 * @author Udo Kastilan
 */
class FlexibleStorageHandler
	extends OutputStream
	implements StorageHandler
{
	/**
	 * Prefix string for temp files
	 */
	private final static String PREFIX = "fuel-";
	/**
	 * Suffix string for temp files
	 */
	private final static String SUFFIX = ".tmp";
	/**
	 * Directory for temp files, optional
	 */
	private File tempDir = null;
	/**
	 * Temp file; null until needed
	 */
	private File file = null;
	/**
	 * List of (input) streams which need to be cleaned up after usage, i.e.
	 * FileInputStreams
	 */
	private List<Closeable> closeables = new ArrayList<Closeable>(1);
	private OutputStream out = null;
	private byte[] data = null;
	private boolean closed = false;
	private int threshold = 0;
	private long written = 0;


	private FlexibleStorageHandler(File tempDir, int threshold)
	{
		if (threshold < 0)
		{
			throw new IllegalArgumentException(
					"Argument threshold must not be a negative value");
		}
		this.tempDir = tempDir;
		this.threshold = threshold;
	}


	/**
	 * Creates a new StorageHandler instance for a given (optional) temp dir and
	 * temp file threshold. Threshold must not be a negative value. Below and up
	 * to this value, a memory buffer will be used.
	 * 
	 * @param tempDir
	 *          Directory for temp files; if null, the default system temp file
	 *          location will be used
	 * @param threshold
	 *          Threshold value in bytes; uploaded items which are bigger than
	 *          this value will be stored in a temp file
	 * @return StorageHandler instance
	 */
	static StorageHandler create(File tempDir, int threshold)
	{
		return new FlexibleStorageHandler(tempDir, threshold);
	}


	public long getSize()
	{
		return written;
	}
	
	
	public File getAsFile()
		throws IOException
	{
		if (file == null)
		{
			File f = File.createTempFile(PREFIX, SUFFIX, tempDir);
			
			try
			{
				writeTo(f);
				file = f;
			}
			catch (IOException ex)
			{
				if (f.isFile())
				{
					f.delete();
				}
				throw ex;
			}
		}
		return file;
	}


	public void writeTo(File to)
		throws IOException
	{
		InputStream sin = null;
		OutputStream sout = null;
		byte[] buf = new byte[8192];
		int r = 0;

		close();
		try
		{
			if (data != null)
			{
				sin = new ByteArrayInputStream(data);
			}
			else if (file != null)
			{
				sin = new FileInputStream(file);
			}
			else
			{
				throw new RuntimeException("This is not possible ... yet it happened");
			}
			sout = new FileOutputStream(to);
			while ((r = sin.read(buf)) != -1)
			{
				sout.write(buf, 0, r);
			}
		}
		finally
		{
			MiscUtils.close(sin);
			MiscUtils.close(sout);
		}
	}


	public InputStream getInputStream()
		throws IOException
	{
		if (!closed)
		{
			close();
		}
		if (data != null)
		{
			return new ByteArrayInputStream(data);
		}
		if (file != null)
		{
			InputStream in = new FileInputStream(file);

			closeables.add(in);
			return in;
		}
		throw new IllegalStateException("This cannot happen");
	}


	public void cleanup()
	{
		MiscUtils.close(out);
		for (Closeable c : closeables)
		{
			MiscUtils.close(c);
		}
		closeables.clear();
		if (file != null)
		{
			file.delete();
		}
	}


	@Override
	public void close()
		throws IOException
	{
		if (closed)
		{
			return;
		}
		if (file == null)
		{
			if (out instanceof ByteArrayOutputStream)
			{
				data = ((ByteArrayOutputStream) out).toByteArray();
			}
			else
			{
				data = new byte[0];
			}
		}
		out = MiscUtils.close(out);
		closed = true;
	}


	@Override
	public void write(int b)
		throws IOException
	{
		if (closed)
		{
			throw new IOException("Stream has already been closed");
		}
		if (out == null)
		{
			out = new ByteArrayOutputStream(threshold);
		}
		if (written == threshold)
		{
			OutputStream curr = out;

			file = (tempDir == null) ? File.createTempFile(PREFIX, SUFFIX) : File
					.createTempFile(PREFIX, SUFFIX, tempDir);
			out = new BufferedOutputStream(new FileOutputStream(file));
			if (curr instanceof ByteArrayOutputStream)
			{
				out.write(((ByteArrayOutputStream) curr).toByteArray());
			}
		}
		out.write(b);
		written++;
	}


	public OutputStream getOutputStream()
		throws IOException
	{
		return this;
	}
}
