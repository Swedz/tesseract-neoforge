package net.swedz.tesseract.neoforge.lang.exception;

import java.io.Serial;

public final class UndefinedStyleException extends RuntimeException
{
	@Serial
	private static final long serialVersionUID = 0L;
	
	public UndefinedStyleException(String key)
	{
		super("There is no style defined for the key '" + key + "'.");
	}
}
