package net.swedz.tesseract.neoforge.compat.mi.serialization;

import aztech.modern_industrialization.api.energy.CableTier;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public final class MIStreamCodecs
{
	public static final StreamCodec<ByteBuf, CableTier> CABLE_TIER = cableTier(() -> CableTier.allTiers().toArray(new CableTier[0]));
	
	public static StreamCodec<ByteBuf, CableTier> cableTier(Supplier<CableTier[]> acceptableValues)
	{
		return ByteBufCodecs.STRING_UTF8.map(
				(name) ->
				{
					for(CableTier tier : acceptableValues.get())
					{
						if(tier.name.equals(name))
						{
							return tier;
						}
					}
					throw new NoSuchElementException("No cable tier with name %s exists".formatted(name));
				},
				(tier) -> tier.name
		);
	}
}
