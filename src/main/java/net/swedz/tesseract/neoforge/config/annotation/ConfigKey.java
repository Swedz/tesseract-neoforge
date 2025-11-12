package net.swedz.tesseract.neoforge.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConfigKey
{
	/**
	 * <p>The key to use for the config entry.</p>
	 *
	 * <p>If left unspecified (or empty), the key will be generated using the method name. The method name is assumed
	 * to be in camelCase. The key used for the config will be the method name converted to snake_case.</p>
	 *
	 * @return the config key to use
	 */
	String value() default "";
}
