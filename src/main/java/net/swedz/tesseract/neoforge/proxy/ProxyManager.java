package net.swedz.tesseract.neoforge.proxy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import net.swedz.tesseract.neoforge.helper.AnnotationDataHelper;
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

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
	
	/**
	 * Merges data from multiple proxy types into a single value.
	 * <p>
	 * Each proxy from the provided {@code proxyClasses} is processed in order, and for each one, the {@code merge}
	 * function is called with the current value and the proxy. The result from the {@code merge} function becomes the
	 * new current value. The process begins with the {@code startingValue} as the initial value.
	 * </p>
	 *
	 * @param commonInterface the common interface {@code I} that is implemented by all proxies
	 * @param startingValue   the initial value to start the merge with
	 * @param merge           the function that merges data from a proxy of type {@code I} into the current value of
	 *                        type {@code T}
	 * @param proxyClasses    an array of proxy classes to merge from that implement both {@code Proxy} and the
	 *                        specified common interface {@code I}
	 * @return the final value after merging data from all proxies
	 * @throws IllegalArgumentException if any proxy provided does not implement {@code I}
	 */
	@SafeVarargs
	public static <T, I> T merge(Class<I> commonInterface, T startingValue, BiFunction<T, I, T> merge, Class<? extends Proxy>... proxyClasses)
	{
		for(Class<? extends Proxy> proxyClass : proxyClasses)
		{
			if(!commonInterface.isAssignableFrom(proxyClass))
			{
				throw new IllegalArgumentException("Cannot merge proxy class %s as it does not implement %s".formatted(proxyClass, commonInterface));
			}
		}
		
		T value = startingValue;
		
		for(Class<? extends Proxy> proxyClass : proxyClasses)
		{
			I proxy = (I) get(proxyClass);
			value = merge.apply(value, proxy);
		}
		
		return value;
	}
	
	/**
	 * Merges data from multiple proxy types and combines the results into a list.
	 *
	 * @param commonInterface the common interface {@code I} that is implemented by all proxies
	 * @param merge           the function that returns a collection of values from each proxy, which are added to the
	 *                        resulting list
	 * @param proxyClasses    an array of proxy classes to merge from that implement both {@code Proxy} and the
	 *                        specified common interface {@code I}
	 * @return the list containing all the values collected from each proxy
	 * @throws IllegalArgumentException if any proxy provided does not implement {@code I}
	 */
	@SafeVarargs
	public static <T, I> List<T> mergeList(Class<I> commonInterface, Function<I, Collection<T>> merge, Class<? extends Proxy>... proxyClasses)
	{
		return merge(
				commonInterface, Lists.newArrayList(),
				(list, proxy) ->
				{
					list.addAll(merge.apply(proxy));
					return list;
				},
				proxyClasses
		);
	}
	
	/**
	 * Merges data from multiple proxy types and ensures that all return {@code true}.
	 *
	 * @param commonInterface the common interface {@code I} that is implemented by all proxies
	 * @param merge           the function that returns a boolean from each proxy, which is combined with the current
	 *                        value using the {@code &&} operator.
	 * @param proxyClasses    an array of proxy classes to merge from that implement both {@code Proxy} and the
	 *                        specified common interface {@code I}
	 * @return {@code true} if all proxies return {@code true}; {@code false} otherwise
	 * @throws IllegalArgumentException if any proxy provided does not implement {@code I}
	 */
	@SafeVarargs
	public static <I> boolean mergeAnd(Class<I> commonInterface, Function<I, Boolean> merge, Class<? extends Proxy>... proxyClasses)
	{
		return merge(
				commonInterface, true,
				(value, proxy) -> value && merge.apply(proxy),
				proxyClasses
		);
	}
	
	/**
	 * Merges data from multiple proxy types and ensures that at least one returns {@code true}.
	 *
	 * @param commonInterface the common interface {@code I} that is implemented by all proxies
	 * @param merge           the function that returns a boolean from each proxy, which is combined with the current
	 *                        value using the {@code ||} operator.
	 * @param proxyClasses    an array of proxy classes to merge from that implement both {@code Proxy} and the
	 *                        specified common interface {@code I}
	 * @return {@code true} if any proxies return {@code true}; {@code false} otherwise
	 * @throws IllegalArgumentException if any proxy provided does not implement {@code I}
	 */
	@SafeVarargs
	public static <I> boolean mergeOr(Class<I> commonInterface, Function<I, Boolean> merge, Class<? extends Proxy>... proxyClasses)
	{
		return merge(
				commonInterface, false,
				(value, proxy) -> value || merge.apply(proxy),
				proxyClasses
		);
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
