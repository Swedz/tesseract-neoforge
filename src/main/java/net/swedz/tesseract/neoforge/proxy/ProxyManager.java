package net.swedz.tesseract.neoforge.proxy;

import com.google.common.collect.Maps;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import net.swedz.tesseract.neoforge.helper.AnnotationDataHelper;
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;

public final class ProxyManager
{
	private static final Logger LOGGER = LoggerFactory.getLogger("Tesseract API/ProxyManager");
	
	private static final Type PROXY_ENTRYPOINT = Type.getType(ProxyEntrypoint.class);
	
	private static boolean INITIALIZED;
	
	private static final Map<Class<? extends Proxy>, Proxy> PROXIES = Maps.newHashMap();
	
	/**
	 * Get the active proxy for a given superclass. Proxies are referred to by their highest superclass (other than
	 * {@link Proxy} itself).
	 *
	 * @param proxyClass the proxy "superclass"
	 * @return the active proxy
	 * @throws IllegalArgumentException if no proxy is registered with this superclass
	 */
	public static <P extends Proxy> P get(Class<P> proxyClass)
	{
		Objects.requireNonNull(proxyClass);
		if(!PROXIES.containsKey(proxyClass))
		{
			throw new IllegalArgumentException("No proxy found for class %s".formatted(proxyClass.getName()));
		}
		return (P) PROXIES.get(proxyClass);
	}
	
	private static Class<? extends Proxy> getProxyKey(Class<? extends Proxy> proxyClass)
	{
		Class<?> currentClass = proxyClass;
		Class<?> lastProxyClass = currentClass;
		while(Proxy.class.isAssignableFrom(currentClass.getSuperclass()))
		{
			currentClass = currentClass.getSuperclass();
			if(Proxy.class.isAssignableFrom(currentClass))
			{
				lastProxyClass = currentClass;
			}
		}
		return (Class<? extends Proxy>) lastProxyClass;
	}
	
	private static void registerEntrypoint(
			ModFileScanData.AnnotationData annotation,
			int priority,
			EnumSet<ProxyEnvironment> environments,
			Map<Class<? extends Proxy>, ProxyWrapper> proxies
	) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
	{
		Class<?> entrypointClass = Class.forName(annotation.memberName());
		if(Proxy.class.isAssignableFrom(entrypointClass))
		{
			Class<? extends Proxy> proxyClassReference = entrypointClass.asSubclass(Proxy.class);
			Class<? extends Proxy> proxyKey = getProxyKey(proxyClassReference);
			
			ProxyWrapper existingProxy = proxies.get(proxyKey);
			
			if(existingProxy == null ||
			   existingProxy.priority() < priority ||
			   existingProxy.environments().contains(ProxyEnvironment.COMMON))
			{
				Proxy proxy = proxyClassReference.getConstructor().newInstance();
				ProxyWrapper oldProxy = proxies.put(proxyKey, new ProxyWrapper(proxy, priority, environments));
				
				LOGGER.info("Loaded proxy entrypoint {} ({})", proxy.getClass().getName(), proxyKey.getSimpleName());
				
				if(oldProxy != null)
				{
					LOGGER.info("Overriding proxy entrypoint {} ({})", oldProxy.proxy().getClass().getName(), proxyKey.getSimpleName());
				}
			}
		}
		else
		{
			LOGGER.error("Invalid proxy entrypoint {}: does not implement Proxy", annotation.memberName());
		}
	}
	
	@ApiStatus.Internal
	public static void initEntrypoints()
	{
		if(INITIALIZED)
		{
			throw new IllegalStateException("Proxy entrypoints already registered");
		}
		INITIALIZED = true;
		
		LOGGER.info("Starting proxy manager entrypoint loader");
		
		Map<Class<? extends Proxy>, ProxyWrapper> proxies = Maps.newHashMap();
		
		ModList.get().getAllScanData().stream()
				.flatMap((data) -> data.getAnnotations().stream())
				.filter((annotation) -> PROXY_ENTRYPOINT.equals(annotation.annotationType()))
				.forEach((annotation) ->
				{
					try
					{
						int priority = (int) annotation.annotationData().getOrDefault("priority", 0);
						EnumSet<ProxyEnvironment> environments = AnnotationDataHelper.getEnumSetOrDefault(
								annotation, ProxyEnvironment.class,
								"environment", ProxyEnvironment.COMMON
						);
						if(environments.contains(ProxyEnvironment.COMMON) && environments.size() > 1)
						{
							LOGGER.error("Nonsensical proxy entrypoint {}: has multiple environments and one is the common environment", annotation.memberName());
							return;
						}
						String[] modIds = (String[]) annotation.annotationData().getOrDefault("modid", new String[0]);
						if(environments.stream().allMatch((e) -> e.test(modIds)))
						{
							registerEntrypoint(annotation, priority, environments, proxies);
						}
					}
					catch (Throwable ex)
					{
						LOGGER.error("Invalid proxy entrypoint {}: Exception constructing proxy entrypoint", annotation.memberName(), ex);
					}
				});
		
		proxies.forEach((key, proxy) ->
		{
			PROXIES.put(key, proxy.proxy());
			proxy.proxy().init();
		});
		
		LOGGER.info("Done proxy manager entrypoint loader");
	}
	
	private record ProxyWrapper(Proxy proxy, int priority, EnumSet<ProxyEnvironment> environments)
	{
	}
}
