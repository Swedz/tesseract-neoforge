package net.swedz.tesseract.neoforge.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ModeledRegisteredObjectHolder<Thing, ActualThing extends Thing, ModelProviderType, Self extends ModeledRegisteredObjectHolder<Thing, ActualThing, ModelProviderType, Self>> extends RegisteredObjectHolder<Thing, ActualThing, Self>
{
	protected Optional<Consumer<ModelProviderType>> modelProvider = Optional.empty();
	
	public ModeledRegisteredObjectHolder(ResourceLocation location, String englishName)
	{
		super(location, englishName);
	}
	
	public boolean hasModelProvider()
	{
		return modelProvider.isPresent();
	}
	
	public Consumer<ModelProviderType> modelProvider()
	{
		return modelProvider.orElseThrow();
	}
	
	public Self withModel(Function<Self, Consumer<ModelProviderType>> modelProvider)
	{
		this.guaranteeUnlocked();
		this.modelProvider = Optional.of(modelProvider.apply(this.self()));
		return this.self();
	}
	
	public Self withoutModel()
	{
		this.guaranteeUnlocked();
		this.modelProvider = Optional.empty();
		return this.self();
	}
}
