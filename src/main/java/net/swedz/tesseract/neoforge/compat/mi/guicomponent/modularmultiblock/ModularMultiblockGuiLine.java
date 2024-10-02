package net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public record ModularMultiblockGuiLine(Component text, int color, boolean wrap)
{
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
	
	public static ModularMultiblockGuiLine read(RegistryFriendlyByteBuf buf)
	{
		Component text = ComponentSerialization.STREAM_CODEC.decode(buf);
		int color = buf.readInt();
		boolean wrap = buf.readBoolean();
		return new ModularMultiblockGuiLine(text, color, wrap);
	}
	
	public static void write(RegistryFriendlyByteBuf buf, ModularMultiblockGuiLine line)
	{
		ComponentSerialization.STREAM_CODEC.encode(buf, line.text());
		buf.writeInt(line.color());
		buf.writeBoolean(line.wrap());
	}
}
