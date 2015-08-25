package de.skaliant.wax.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Parses request input stream content for file uploads.
 *
 * @author Udo Kastilan
 */
public class MultipartParser
{
	private final static String MULTIPART_FORM_DATA = "multipart/form-data";
	private final static String CONTENT_TYPE = "Content-Type";
	private final static String BOUNDARY = "boundary";
	/**
	 * List of StorageHandlers which need to be cleaned up
	 */
	private List<StorageHandler> storages = new ArrayList<StorageHandler>();
	/**
	 * Store parts in this list, so they can be accessed multiple times
	 */
	private List<Part> parts = new ArrayList<Part>();
	/**
	 * Input stream allowing bytes to be pushed back
	 */
	private DynamicPushbackInputStream in = null;
	/**
	 * Optional: directory for temporary files
	 */
	private File tempDir = null;
	/**
	 * End of stream has been reached (either physically or by format)
	 */
	private boolean eos = false;
	/**
	 * Has the first occurrence of a boundary already been found?
	 */
	private boolean initialized = false;
	private int[] boundaryBytes = null;
	private int threshold = 8192;


	private MultipartParser(String boundary, InputStream in, File tempDir,
			int threshold)
	{
		boundary = "\r\n--" + boundary;
		this.in = new DynamicPushbackInputStream(in, boundary.length());
		this.tempDir = tempDir;
		this.threshold = threshold;
		boundaryBytes = new int[boundary.length()];
		for (int i = 0, blen = boundary.length(); i < blen; i++)
		{
			boundaryBytes[i] = boundary.charAt(i);
		}
	}


	public void cleanup()
	{
		for (StorageHandler sh : storages)
		{
			sh.cleanup();
		}
		storages.clear();
	}


	public List<Part> getAllParts()
		throws IOException, UploadFormatException
	{
		/*
		 * Consume any remaining parts; next() stores them in field "parts"
		 */
		while (next() != null)
		{
			//
		}
		return parts;
	}


	public Part next()
		throws IOException, UploadFormatException
	{
		if (eos)
		{
			return null;
		}
		Part p = null;

		if (!initialized)
		{
			seekFirstPart();
			initialized = true;
		}
		if (!eos)
		{
			StorageHandler sh = FlexibleStorageHandler.create(tempDir, threshold);
			List<HeaderField> headers = new ArrayList<HeaderField>(3);
			OutputStream out = null;
			String contentType = null;
			String name = null;
			String fileName = null;
			String line = null;
			int equal = 0;
			int b = 0;

			storages.add(sh);
			out = sh.getOutputStream();
			/*
			 * Read header lines of current part
			 */
			do
			{
				line = readLine();
				if (line.length() != 0)
				{
					HeaderField hf = HeaderFieldParser.parseFullLine(line);

					if (hf != null)
					{
						if ("Content-Disposition".equalsIgnoreCase(hf.getName())
								&& "form-data".equalsIgnoreCase(hf.getValue()))
						{
							name = hf.getParam("name");
							fileName = hf.getParam("filename");
							if ((fileName != null) && (fileName.length() == 0))
							{
								fileName = null;
							}
						}
						else if ("Content-Type".equalsIgnoreCase(hf.getName()))
						{
							contentType = hf.getValue();
						}
						headers.add(hf);
					}
				}
			}
			while (line.length() != 0);
			/*
			 * Read data until next boundary and push into storage handler;
			 * FlexibleStorageHandler will switch between memory based and temp file
			 * based storage automatically once the threshold is reached.
			 */
			while ((equal < boundaryBytes.length) && ((b = in.read()) != -1))
			{
				if (b == boundaryBytes[equal])
				{
					equal++;
				}
				else if (equal > 0)
				{
					/*
					 * TODO: naive pattern matching algorithm, should be optimized
					 */
					out.write(boundaryBytes[0]);
					in.unread(b);
					while (equal > 1)
					{
						in.unread(boundaryBytes[--equal]);
					}
					equal = 0;
				}
				else
				{
					out.write(b);
				}
			}
			if ((b == -1) || !checkBoundarySuffix())
			{
				throw new UploadFormatException(
						"Boundary not finished according to standard");
			}
			p = new PartImpl(headers, name, fileName, contentType, sh);
			parts.add(p);
		}
		return p;
	}


	private String readLine()
		throws IOException
	{
		StringBuilder sb = new StringBuilder();
		int b = 0;

		do
		{
			b = in.read();
			if ((b != -1) && (b != '\r') && (b != '\n'))
			{
				sb.append((char) b);
			}
		}
		while ((b != -1) && (b != '\n'));
		return sb.toString();
	}


	private boolean checkBoundarySuffix()
		throws IOException
	{
		int[] ba =
			{ 0, 0 };
		int r = 0;
		int b = 0;

		while ((r < 2) && ((b = in.read()) != -1))
		{
			ba[r++] = b;
		}
		if ((b == -1) || ((ba[0] == '-') && (ba[1] == '-')))
		{
			return eos = true;
		}
		else
		{
			if ((r == 2) && (ba[0] == '\r') && (ba[1] == '\n'))
			{
				return true;
			}
			else
			{
				while (r > 0)
				{
					in.unread(ba[--r]);
				}
			}
		}
		return false;
	}


	/**
	 * Moves the read location to the byte right after the first boundary, i.e.
	 * the first byte of the header of the first part.
	 * 
	 * @throws IOException
	 * @throws UploadFormatException
	 */
	private void seekFirstPart()
		throws IOException, UploadFormatException
	{
		int[] bb = new int[boundaryBytes.length - 2];
		int equal = 0;
		int b = 0;
		/*
		 * Ignore CRLF prefix of boundaryBytes; in case of the first boundary, there
		 * is not necessarily a CRLF ahead as the content input stream should start
		 * right with the boundary dashes
		 */
		System.arraycopy(boundaryBytes, 2, bb, 0, bb.length);
		while ((equal < bb.length) && ((b = in.read()) != -1))
		{
			if (b == bb[equal])
			{
				equal++;
			}
			else if (equal > 0)
			{
				in.unread(b);
				while (equal > 1)
				{
					in.unread(bb[--equal]);
				}
				equal = 0;
			}
		}
		if (b == -1)
		{
			eos = true;
		}
		else if (!checkBoundarySuffix())
		{
			throw new UploadFormatException(
					"Boundary not finished according to standard");
		}
	}


	/**
	 * Create a new MultipartParser.
	 * 
	 * @param contentType
	 *          The upload's content type; must be multipart/form-data and contain
	 *          boundary information
	 * @param in
	 *          Data as input stream
	 * @return Instance
	 * @throws UploadFormatException
	 *           Will be thrown if content type does not match
	 */
	public static MultipartParser create(String contentType, InputStream in)
		throws UploadFormatException
	{
		return create(contentType, in, null, 8192);
	}


	/**
	 * Create a new MultipartParser.
	 * 
	 * @param contentType
	 *          The upload's content type; must be multipart/form-data and contain
	 *          boundary information
	 * @param in
	 *          Data as input stream
	 * @param tempDir
	 *          Optional: directory for temp files; if null, system default will
	 *          be used
	 * @param threshold
	 *          Maximum value in bytes for memory buffering
	 * @return Instance
	 * @throws UploadFormatException
	 *           Will be thrown if content type does not match
	 */
	public static MultipartParser create(String contentType, InputStream in,
			File tempDir, int threshold)
		throws UploadFormatException
	{
		if ((contentType == null) || (in == null))
		{
			throw new IllegalArgumentException("Parameters may not be null");
		}
		HeaderField hf = HeaderFieldParser.parseValue(CONTENT_TYPE, contentType);

		if (!MULTIPART_FORM_DATA.equalsIgnoreCase(hf.getValue()))
		{
			throw new UploadFormatException("Content type must be "
					+ MULTIPART_FORM_DATA + ", but is \"" + hf.getValue() + '"');
		}
		if (!hf.hasParam(BOUNDARY))
		{
			throw new UploadFormatException("Boundary missing");
		}
		return new MultipartParser(hf.getParam(BOUNDARY), in, tempDir, threshold);
	}


	/**
	 * Checks a content type for file uploads. Content type must be
	 * multipart/form-data, and boundary string must be contained as parameter
	 * value for the return value to be true.
	 * 
	 * @param contentType
	 *          Content type
	 * @return Valid file upload?
	 */
	public static boolean isUpload(String contentType)
	{
		HeaderField hf = HeaderFieldParser.parseValue("Content-Type", contentType);

		if (hf != null)
		{
			return MULTIPART_FORM_DATA.equalsIgnoreCase(hf.getValue())
					&& hf.hasParam(BOUNDARY);
		}
		return false;
	}
}
