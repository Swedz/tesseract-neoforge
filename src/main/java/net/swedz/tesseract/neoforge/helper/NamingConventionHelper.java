package net.swedz.tesseract.neoforge.helper;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public final class NamingConventionHelper
{
	private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("([A-Z][a-z]+)|([a-z]+)|([0-9]+)|([A-Z]+(?![a-z]))");
	
	/**
	 * <p>Converts a method's name from camelCase to snake_case.</p>
	 *
	 * <p>Each "word" is converted to lowercase and joined with an underscore for the delimiter. As such, the method
	 * name is matched against the regex pattern <code>([A-Z][a-z]+)|([a-z]+)|([0-9]+)|([A-Z]+(?![a-z]))</code>.</p>
	 *
	 * @param methodName the method's name, in camelCase
	 * @return the method's name converted to snake_case
	 */
	public static String fromCamelCaseToSnakeCase(String methodName)
	{
		var generated = new StringBuilder();
		var matcher = CAMEL_CASE_PATTERN.matcher(methodName);
		int lastEnd = 0;
		while(matcher.find())
		{
			int start = matcher.start();
			int end = matcher.end();
			// Append any non-matched characters
			if(lastEnd < start)
			{
				generated.append(methodName, lastEnd, start);
			}
			if(!generated.isEmpty())
			{
				generated.append('_');
			}
			generated.append(methodName, start, end);
			lastEnd = end;
		}
		return generated.toString().toLowerCase();
	}
	
	/**
	 * <p>Converts a method's name from camelCase to snake_case.</p>
	 *
	 * <p>Each "word" is converted to lowercase and joined with an underscore for the delimiter. As such, the method
	 * name is matched against the regex pattern <code>([A-Z][a-z]+)|([a-z]+)|([0-9]+)|([A-Z]+(?![a-z]))</code>.</p>
	 *
	 * @param method the method, whose name is in camelCase
	 * @return the method's name converted to snake_case
	 */
	public static String fromCamelCaseToSnakeCase(Method method)
	{
		return fromCamelCaseToSnakeCase(method.getName());
	}
}
