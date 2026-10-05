package net.swedz.tesseract.neoforge.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TextSubSection
{
	/**
	 * <p>Specifies the prefix to append to the keys for the defined subsection. More specifically, this will apply
	 * always at the end of the defined prefix, even after the prefix defined in the {@link LangKey#key()}.</p>
	 *
	 * @return the section prefix
	 */
	String value() default "";
	
	/**
	 * <p>Determines whether this subsection should be included as part of the lang key.</p>
	 *
	 * @return true to exclude this sub section from the path of the lang key, false to keep it (default)
	 */
	boolean skipPath() default false;
}
