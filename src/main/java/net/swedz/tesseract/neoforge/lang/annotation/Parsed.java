package net.swedz.tesseract.neoforge.lang.annotation;

import net.swedz.tesseract.neoforge.lang.LangManager;
import net.swedz.tesseract.neoforge.lang.exception.UndefinedParserException;
import net.swedz.tesseract.neoforge.tooltip.Parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Parsed
{
	/**
	 * <p>Defines the parser to be used for the parameter. If this annotation is not present, the default parser on the
	 * manager will be used. If no default parser is defined, the object will be converted to a string. Parsers are
	 * defined with {@link LangManager#parser(String, Class, Parser)}.</p>
	 *
	 * <p>If no parser exists for this key (unless it is <code>default</code>), an {@link UndefinedParserException}
	 * will be thrown.</p>
	 *
	 * @return the parser key
	 */
	String value() default "default";
}
