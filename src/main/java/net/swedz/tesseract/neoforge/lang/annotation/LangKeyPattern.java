package net.swedz.tesseract.neoforge.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LangKeyPattern
{
	/**
	 * <p>Defines the prefix pattern used by {@link LangKey#key()}. All instances of <code>{}</code> are replaced with
	 * the mod identifier.</p>
	 *
	 * @return the prefix pattern
	 */
	String value() default "text.{}.";
}
