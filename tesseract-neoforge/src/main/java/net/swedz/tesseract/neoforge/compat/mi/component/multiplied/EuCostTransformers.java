package net.swedz.tesseract.neoforge.compat.mi.component.multiplied;

import java.util.function.Supplier;

public final class EuCostTransformers
{
	public static final EuCostTransformer FULL_COST = percentage(() -> 1f);
	
	public static EuCostTransformer percentage(Supplier<Float> percentage)
	{
		return new EuCostTransformer.PercentageEuCostTransformer(percentage);
	}
}
