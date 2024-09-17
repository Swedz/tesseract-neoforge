package net.swedz.tesseract.neoforge.proxy;

import net.neoforged.fml.loading.FMLEnvironment;
import net.swedz.tesseract.neoforge.compat.ModLoadedHelper;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum ProxyEnvironment
{
	/**
	 * The common proxy environment. Proxies with this environment will always be loaded. If another proxy extends this proxy, the other one will be registered instead of this one.
	 */
	COMMON,
	/**
	 * The client proxy environment. Proxies with this environment will only be loaded if its on the client.
	 */
	CLIENT(FMLEnvironment.dist::isClient),
	/**
	 * The mod proxy environment. Proxies with this environment will only be loaded if the mod with the id of {@link ProxyEntrypoint#modid()} is loaded.
	 */
	MOD(ModLoadedHelper::isLoaded);
	
	private final Predicate<String> test;
	
	ProxyEnvironment(Predicate<String> test)
	{
		this.test = test;
	}
	
	ProxyEnvironment(Supplier<Boolean> test)
	{
		this((__) -> test.get());
	}
	
	ProxyEnvironment()
	{
		this(() -> true);
	}
	
	public boolean test(List<String> modIds)
	{
		return !modIds.isEmpty() ? modIds.stream().allMatch(test) : test.test("");
	}
}
