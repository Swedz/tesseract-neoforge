package net.swedz.tesseract.neoforge.registry.registerable;

import com.google.common.collect.Lists;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.api.function.QuadFunction;
import net.swedz.tesseract.api.function.TriFunction;
import net.swedz.tesseract.neoforge.api.MCIdentifier;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegisterableWrapper<Type, DeferredType extends DeferredHolder<? super Type, Type>, Register extends DeferredRegister<? super Type>, Properties>
{
	private final Register register;
	
	private final Properties                 properties;
	private final List<Consumer<Properties>> propertiesActions = Lists.newArrayList();
	
	private final Function<Properties, Type> creator;
	
	private Optional<DeferredType> deferred = Optional.empty();
	
	public RegisterableWrapper(Register register, Properties properties, Function<Properties, Type> creator)
	{
		this.register = register;
		this.properties = properties;
		this.creator = creator;
	}
	
	public Properties properties()
	{
		return properties;
	}
	
	public void withProperties(Consumer<Properties> action)
	{
		propertiesActions.add(action);
	}
	
	public Function<Properties, Type> creator()
	{
		return creator;
	}
	
	public void register(MCIdentifier identifier, QuadFunction<Register, String, Function<Properties, Type>, Supplier<Properties>, DeferredType> builder)
	{
		this.deferred = Optional.of(builder.apply(
				register,
				identifier.id(),
				(p) ->
				{
					propertiesActions.forEach((action) -> action.accept(p));
					return this.creator().apply(p);
				},
				() -> properties
		));
	}
	
	public void registerSimple(MCIdentifier identifier, TriFunction<Register, String, Supplier<Type>, DeferredType> builder)
	{
		this.register(identifier, (r, id, f, p) -> builder.apply(r, id, () -> this.creator().apply(null)));
	}
	
	public DeferredType get()
	{
		return deferred.orElseThrow(() -> new IllegalStateException("Cannot get object that hasn't been registered yet"));
	}
	
	public Type getOrThrow()
	{
		return this.get().get();
	}
}
