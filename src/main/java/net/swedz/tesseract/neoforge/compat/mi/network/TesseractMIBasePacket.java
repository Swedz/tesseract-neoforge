package net.swedz.tesseract.neoforge.compat.mi.network;

import aztech.modern_industrialization.network.BasePacket;
import net.minecraft.resources.ResourceLocation;

public interface TesseractMIBasePacket extends BasePacket
{
	@Override
	default ResourceLocation id()
	{
		return TesseractMIPackets.getId(this.getClass());
	}
}
