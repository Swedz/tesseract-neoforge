package net.swedz.tesseract.neoforge.config;

public class IllegalConfigOptionException extends RuntimeException
{
	public IllegalConfigOptionException()
	{
		super();
	}
	
	public IllegalConfigOptionException(String message)
	{
		super(message);
	}
	
	public IllegalConfigOptionException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public IllegalConfigOptionException(Throwable cause)
	{
		super(cause);
	}
}
