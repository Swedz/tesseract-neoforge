package net.swedz.tesseract.neoforge.lang.exception;

import java.io.Serial;

public final class MalformedParserException extends RuntimeException
{
	@Serial
	private static final long serialVersionUID = 0L;
	
	public MalformedParserException(String message)
	{
		super(message);
	}
}
