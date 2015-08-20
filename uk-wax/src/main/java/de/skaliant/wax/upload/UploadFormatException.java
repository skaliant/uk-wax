package de.skaliant.wax.upload;

/**
 * Exception which will be thrown if the format/syntax of an upload does not
 * match expectations.
 *
 * @author Udo Kastilan
 */
public class UploadFormatException
	extends Exception
{
	public UploadFormatException()
	{
		//
	}


	public UploadFormatException(String msg)
	{
		super(msg);
	}


	public UploadFormatException(Throwable cause)
	{
		super(cause);
	}


	public UploadFormatException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
