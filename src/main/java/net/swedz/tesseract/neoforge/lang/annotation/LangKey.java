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
	 * <p>Specifies the short language key to use for this method. This will be prefixed with the pattern defined by
	 * the {@link LangKeyPattern} annotation on the class, or <code>text.{}.</code> if not present, where
	 * <code>{}</code> is replaced with the mod identifier.</p>
	 *
	 * @return the short language key
	 */
	String key() default "";
	
	/**
	 * <p>Defines the default text for the method. This is purely used for datagen.</p>
	 *
	 * <p>Note that this is an array type simply so an empty string can be provided for a text value. Any strings
	 * beyond index 0 will be ignored.</p>
	 *
	 * @return the default text for the method
	 */
	String[] text() default {};
	
	/**
	 * <p>Whether the translatable component should include the default text as fallback.</p>
	 *
	 * <p>This is helpful for when you may or may not have the language file distributed to all clients and want to
	 * allow clients to override text in their own local resource pack.</p>
	 *
	 * @return true if the default text should be included on the component as fallback, false if not
	 */
	boolean includeFallback() default false;
}
