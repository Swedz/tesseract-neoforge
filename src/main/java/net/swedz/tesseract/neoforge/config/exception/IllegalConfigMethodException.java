package net.swedz.tesseract.neoforge.config.exception;

public class IllegalConfigMethodException extends RuntimeException
{
	public IllegalConfigMethodException()
	{
		super();
	}
	
	public IllegalConfigMethodException(String message)
	{
		super(message);
	}
	
	public IllegalConfigMethodException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public IllegalConfigMethodException(Throwable cause)
	{
		super(cause);
	}
}
