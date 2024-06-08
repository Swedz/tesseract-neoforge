package net.swedz.tesseract.neoforge.compat.mi.component.multiplied;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.TesseractMod;

import java.util.function.Supplier;

public abstract class EuCostTransformer
{
	private final ResourceLocation id;
	
	public EuCostTransformer(ResourceLocation id)
	{
		this.id = id;
	}
	
	public String getTranslationKey()
	{
		return "eu_cost_transformer.%s.%s".formatted(id.getNamespace(), id.getPath());
	}
	
	public MutableComponent text()
	{
		return Component.translatable(this.getTranslationKey());
	}
	
	public abstract long transform(MultipliedCrafterComponent crafter, long eu);
	
	public static final class PercentageEuCostTransformer extends EuCostTransformer
	{
		private final Supplier<Float> percentage;
		
		public PercentageEuCostTransformer(Supplier<Float> percentage)
		{
			super(TesseractMod.id("percentage"));
			this.percentage = percentage;
		}
		
		@Override
		public long transform(MultipliedCrafterComponent crafter, long eu)
		{
			return (long) (eu * crafter.getRecipeMultiplier() * percentage.get());
		}
		
		@Override
		public MutableComponent text()
		{
			return Component.translatable(this.getTranslationKey(), (int) (percentage.get() * 100));
		}
	}
}
