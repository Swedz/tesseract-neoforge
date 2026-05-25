package net.swedz.tesseract.neoforge.compat.mi.serialization;

import aztech.modern_industrialization.api.energy.CableTier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.function.Supplier;

public final class MICodecs
{
	public static final Codec<CableTier> CABLE_TIER = cableTier(() -> CableTier.allTiers().toArray(new CableTier[0]));
	
	public static Codec<CableTier> cableTier(Supplier<CableTier[]> acceptableValues)
	{
		return Codec.STRING.flatXmap(
				(name) ->
				{
					for(CableTier tier : acceptableValues.get())
					{
						if(tier.name.equals(name))
						{
							return DataResult.success(tier);
						}
					}
					return DataResult.error(() -> "No cable tier with name %s exists".formatted(name));
				},
				(tier) -> DataResult.success(tier.name)
		);
	}
}
