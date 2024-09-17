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
	 * The priority of this proxy entrypoint. If multiple proxy entrypoints are able to be loaded in an instance, the one with the highest priority will override the others. The behavior for proxies with equal priorities being loaded at the same time is undefined, so it is recommended to make use of this when applicable.
	 *
	 * @return the priority
	 */
	int priority() default 0;
	
	/**
	 * The environments this proxy entrypoint should load on. If multiple environments are supplied, all of their requirements must be met and none of them can be {@link ProxyEnvironment#COMMON}.
	 *
	 * @return the environments
	 */
	ProxyEnvironment[] environment() default ProxyEnvironment.COMMON;
	
	/**
	 * The ids of the mods to check are loaded before this proxy entrypoint is loaded. Only used if {@link ProxyEntrypoint#environment()} includes {@link ProxyEnvironment#MOD}.
	 *
	 * @return the mod ids
	 */
	String[] modid() default {};
}
