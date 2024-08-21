package net.swedz.tesseract.neoforge.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyEntrypoint
{
	/**
	 * The environments this proxy entrypoint should load on. If multiple environments are supplied, all of their requirements must be met and none of them can be {@link ProxyEnvironment#COMMON}.
	 *
	 * @return the environments
	 */
	ProxyEnvironment[] environment() default ProxyEnvironment.COMMON;
	
	/**
	 * The id of the mod to check is loaded before this proxy entrypoint is loaded. Only used if {@link ProxyEntrypoint#environment()} includes {@link ProxyEnvironment#MOD}.
	 *
	 * @return the mod id
	 */
	String modid() default "";
}
