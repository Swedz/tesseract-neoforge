package net.swedz.tesseract.neoforge.compat.mi.network;

import net.swedz.tesseract.neoforge.packet.CustomPacket;

public interface TesseractMICustomPacket extends CustomPacket
{
	@Override
	default Type<TesseractMICustomPacket> type()
	{
		return TesseractMIPackets.getType(this.getClass());
	}
}
