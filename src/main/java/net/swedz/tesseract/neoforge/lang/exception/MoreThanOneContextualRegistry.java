package net.swedz.tesseract.neoforge.lang.exception;

import java.io.Serial;
import java.lang.reflect.Method;

public final class MoreThanOneContextualRegistry extends RuntimeException
{
	@Serial
	private static final long serialVersionUID = 0L;
	
	public MoreThanOneContextualRegistry(Method method)
	{
		super("There is more than one ContextualRegistry defined for the method '" + method.toGenericString() + "'.");
	}
}
