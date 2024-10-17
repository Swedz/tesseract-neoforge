package net.swedz.tesseract.neoforge.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ModeledRegisteredObjectHolder<Thing, ActualThing extends Thing, ModelBuilderType, Self extends ModeledRegisteredObjectHolder<Thing, ActualThing, ModelBuilderType, Self>> extends RegisteredObjectHolder<Thing, ActualThing, Self>
{
	protected Optional<Consumer<ModelBuilderType>> modelBuilder = Optional.empty();
	
	public ModeledRegisteredObjectHolder(ResourceLocation location, String englishName)
	{
		super(location, englishName);
	}
	
	public boolean hasModelBuilder()
	{
		return modelBuilder.isPresent();
	}
	
	public Consumer<ModelBuilderType> modelBuilder()
	{
		return modelBuilder.orElseThrow();
	}
	
	public Self withModel(Function<Self, Consumer<ModelBuilderType>> modelBuilderCreator)
	{
		this.guaranteeUnlocked();
		this.modelBuilder = Optional.of(modelBuilderCreator.apply(this.self()));
		return this.self();
	}
}
