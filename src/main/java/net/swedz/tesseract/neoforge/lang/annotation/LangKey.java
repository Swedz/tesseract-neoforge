package net.swedz.tesseract.neoforge.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Specifies the language key to be used for a method.</p>
 *
 * <p>When neither {@link #value()} and {@link #key()} are set, the key is generated using the method name. Method
 * names are assumed to be in camelCase. Each word is converted to lowercase and joined with an underscore for the
 * delimiter. As such, the method name is matched against the regex pattern
 * <code>([A-Z][a-z]+)|([a-z]+)|([0-9]+)|([A-Z]+(?![a-z]))</code>. This generated key will be used as if it were set in
 * the {@link #key()} parameter.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LangKey
{
	/**
	 * <p>Specifies the language key to use for this method.</p>
	 *
	 * <p>When set, the {@link #key()} parameter is ignored.</p>
	 *
	 * <p>All instances of <code>{}</code> will be replaced with the mod identifier.</p>
	 *
	 * @return the language key
	 */
	String value() default "";
	
	/**
	 * <p>Specifies the short language key to use for this method. This will be prefixed with <code>text.{}.</code>
	 * where <code>{}</code> is replaced with the mod identifier.</p>
	 *
	 * @return the short language key
	 */
	String key() default "";
}
