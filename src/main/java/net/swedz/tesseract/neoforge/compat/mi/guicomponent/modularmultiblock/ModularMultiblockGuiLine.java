package net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ModularMultiblockGuiLine(Component text, int color, boolean wrap)
{
	public static final StreamCodec<RegistryFriendlyByteBuf, ModularMultiblockGuiLine> STREAM_CODEC = StreamCodec.composite(
			ComponentSerialization.STREAM_CODEC, ModularMultiblockGuiLine::text,
			ByteBufCodecs.INT, ModularMultiblockGuiLine::color,
			ByteBufCodecs.BOOL, ModularMultiblockGuiLine::wrap,
			ModularMultiblockGuiLine::new
	);
	
	public ModularMultiblockGuiLine(Component text, int color)
	{
		this(text, color, false);
	}
	
	public ModularMultiblockGuiLine(Component text)
	{
		this(text, WHITE);
	}
	
	public static final int WHITE = 0xFFFFFF;
	public static final int RED   = 0xFF0000;
}
