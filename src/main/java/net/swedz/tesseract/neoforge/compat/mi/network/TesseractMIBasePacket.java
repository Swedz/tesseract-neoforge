package net.swedz.tesseract.neoforge.compat.mi.network;

import aztech.modern_industrialization.network.BasePacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface TesseractMIBasePacket extends BasePacket
{
	@Override
	default Type<? extends CustomPacketPayload> type()
	{
		return TesseractMIPackets.getType(this.getClass());
	}
}
