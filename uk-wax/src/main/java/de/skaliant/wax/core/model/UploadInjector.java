package de.skaliant.wax.core.model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import de.skaliant.wax.app.FileUpload;
import de.skaliant.wax.upload.MultipartParser;
import de.skaliant.wax.upload.Part;
import de.skaliant.wax.upload.UploadFormatException;
import de.skaliant.wax.util.StatementResolver;
import de.skaliant.wax.util.logging.Log;


/**
 * What the Injector does for parameter and path information, the UploadInjector does for file uploads.
 *
 * @author Udo Kastilan
 */
public class UploadInjector
{
	private final static Log LOG = Log.get(UploadInjector.class);
	
	
	/**
	 * Inject uploaded file parts into the instance.
	 * 
	 * @param instance Bean
	 * @param multi MultipartParser instance handling the request
	 */
	public static void injectUploads(Object instance, MultipartParser multi)
	{
		UploadedFileProvider ufp = new UploadedFileProvider();
		
		try
		{
			for (Part p : multi.getAllParts())
			{
				if (p.isFile() && (p.getSize() > 0))
				{
					ufp.part = p;
					try
					{
						StatementResolver.setValueTo(instance, p.getName(), ufp);
					}
					catch (Exception ex)
					{
						LOG.warn("Caught exception for injecting upload into \"" + p.getName() + "\" of " + 
								instance.getClass().getName(), ex);
						//
					}
				}
			}
		}
		catch (IOException ex)
		{
			LOG.error("Upload error", ex);
		}
		catch (UploadFormatException ex)
		{
			LOG.error("Upload error", ex);
		}
	}
	
	
	/**
	 * Provides values for the StatementResolver.
	 */
	private static class UploadedFileProvider
		implements StatementResolver.ValueProvider
	{
		private Part part = null;
		
		
		public Object provide(Type type)
		{
			if (type instanceof Class)
			{
				Class<?> c = (Class<?>) type;
				
				if (c == File.class)
				{
					try
					{
						return part.getAsFile();
					}
					catch (Exception ex)
					{
						LOG.warn("Cannot provide upload as temporary file", ex);
					}
				}
				else if (c == FileUpload.class)
				{
					return new FileUploadPartWrapper(part);
				}
			}
			return null;
		}
	}
	
	
	/**
	 * Wraps a part for fields of type {@link de.skaliant.wax.app.FileUpload}.
	 *
	 * @author Udo Kastilan
	 */
	private static class FileUploadPartWrapper
		implements FileUpload
	{
		/**
		 * lol private part ...
		 */
		private Part part = null;
		
		
		private FileUploadPartWrapper(Part part)
		{
			this.part = part;
		}
		

		public String getContentType()
		{
			return part.getContentType();
		}

		public String getFileName()
		{
			return part.getFileNameRefined();
		}

		public long getSize()
		{
			return part.getSize();
		}

		public void writeTo(File destination)
			throws IOException
		{
			part.writeTo(destination);
		}
	}
}
