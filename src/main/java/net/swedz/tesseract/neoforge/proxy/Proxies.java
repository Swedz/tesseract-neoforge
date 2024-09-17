package net.swedz.tesseract.neoforge.proxy;

import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class Proxies
{
	private static final LoadedProxies PROXIES = new LoadedProxies();
	
	@ApiStatus.Internal
	public static void initEntrypoints()
	{
		PROXIES.initEntrypoints();
	}
	
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
		return PROXIES.get(proxyClass);
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
		return PROXIES.merge(commonInterface, startingValue, merge, proxyClasses);
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
		return PROXIES.mergeList(commonInterface, merge, proxyClasses);
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
		return PROXIES.mergeAnd(commonInterface, merge, proxyClasses);
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
		return PROXIES.mergeOr(commonInterface, merge, proxyClasses);
	}
}
