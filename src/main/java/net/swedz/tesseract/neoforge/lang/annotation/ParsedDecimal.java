package net.swedz.tesseract.neoforge.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ParsedDecimal
{
	/**
	 * <p>The amount of decimal places to display for this parameter.</p>
	 *
	 * <p>The parameter must be one of the following types: <code>float</code>, <code>double</code>, {@link Float}, or
	 * {@link Double}</p>
	 *
	 * @return the number of decimal places to display
	 */
	int value() default 0;
}
