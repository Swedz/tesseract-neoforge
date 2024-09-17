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

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class LoadedProxies
{
	private static final Logger LOGGER = LoggerFactory.getLogger("Tesseract API/Proxies");
	
	private static final Type PROXY_ENTRYPOINT = Type.getType(ProxyEntrypoint.class);
	
	private final Map<Class<? extends Proxy>, LoadedProxy> proxies = Maps.newHashMap();
	
	private boolean initialized;
	
	public <P extends Proxy> P get(Class<P> proxyClass)
	{
		Objects.requireNonNull(proxyClass);
		if(!proxies.containsKey(proxyClass))
		{
			throw new IllegalArgumentException("No proxy found for class %s".formatted(proxyClass.getName()));
		}
		return (P) proxies.get(proxyClass).proxy();
	}
	
	@SafeVarargs
	public final <T, I> T merge(Class<I> commonInterface, T startingValue, BiFunction<T, I, T> merge, Class<? extends Proxy>... proxyClasses)
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
	
	@SafeVarargs
	public final <T, I> List<T> mergeList(Class<I> commonInterface, Function<I, Collection<T>> merge, Class<? extends Proxy>... proxyClasses)
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
	
	@SafeVarargs
	public final <I> boolean mergeAnd(Class<I> commonInterface, Function<I, Boolean> merge, Class<? extends Proxy>... proxyClasses)
	{
		return merge(
				commonInterface, true,
				(value, proxy) -> value && merge.apply(proxy),
				proxyClasses
		);
	}
	
	@SafeVarargs
	public final <I> boolean mergeOr(Class<I> commonInterface, Function<I, Boolean> merge, Class<? extends Proxy>... proxyClasses)
	{
		return merge(
				commonInterface, false,
				(value, proxy) -> value || merge.apply(proxy),
				proxyClasses
		);
	}
	
	private void add(Class<? extends Proxy> type, Proxy proxy, int priority, EnumSet<ProxyEnvironment> environments)
	{
		proxies.put(type, new LoadedProxy(type, proxy, priority, environments));
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
	
	private void registerEntrypoint(ModFileScanData.AnnotationData annotation, int priority, EnumSet<ProxyEnvironment> environments) throws Exception
	{
		Class<?> entrypointClass = Class.forName(annotation.memberName());
		if(Proxy.class.isAssignableFrom(entrypointClass))
		{
			Class<? extends Proxy> proxyClassReference = entrypointClass.asSubclass(Proxy.class);
			Class<? extends Proxy> proxyKey = getProxyKey(proxyClassReference);
			
			LoadedProxy existingProxy = proxies.get(proxyKey);
			
			if(existingProxy == null ||
			   existingProxy.priority() < priority ||
			   existingProxy.environments().contains(ProxyEnvironment.COMMON))
			{
				Proxy proxy = proxyClassReference.getConstructor().newInstance();
				this.add(proxyKey, proxy, priority, environments);
			}
		}
		else
		{
			LOGGER.error("Invalid proxy entrypoint {}: does not implement Proxy", annotation.memberName());
		}
	}
	
	@ApiStatus.Internal
	public void initEntrypoints()
	{
		if(initialized)
		{
			throw new IllegalStateException("Proxy entrypoints already registered");
		}
		initialized = true;
		
		LOGGER.info("Starting proxy manager entrypoint loader");
		
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
						
						List<String> modIds = (List<String>) annotation.annotationData().getOrDefault("modid", Lists.newArrayList());
						
						if(environments.stream().allMatch((e) -> e.test(modIds)))
						{
							this.registerEntrypoint(annotation, priority, environments);
						}
					}
					catch (Throwable ex)
					{
						LOGGER.error("Invalid proxy entrypoint {}: Exception constructing proxy entrypoint", annotation.memberName(), ex);
					}
				});
		
		proxies.forEach((key, proxy) ->
		{
			proxy.proxy().init();
			LOGGER.info("Loaded proxy entrypoint {} ({})", proxy.key().getName(), proxy.proxy().getClass().getSimpleName());
		});
		
		LOGGER.info("Done proxy manager entrypoint loader");
	}
}
