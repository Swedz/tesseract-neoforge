package net.swedz.tesseract.neoforge.lang.exception;

import java.io.Serial;

public final class UndefinedParserException extends RuntimeException
{
	@Serial
	private static final long serialVersionUID = 0L;
	
	public UndefinedParserException(String key)
	{
		super("There is no parser defined for the key '" + key + "'.");
	}
}
