package net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public record ModularMultiblockGuiLine(Component text, int color)
{
	public ModularMultiblockGuiLine(Component text)
	{
		this(text, 0xFFFFFF);
	}
	
	public static ModularMultiblockGuiLine read(RegistryFriendlyByteBuf buf)
	{
		Component text = ComponentSerialization.STREAM_CODEC.decode(buf);
		int color = buf.readInt();
		return new ModularMultiblockGuiLine(text, color);
	}
	
	public static void write(RegistryFriendlyByteBuf buf, ModularMultiblockGuiLine line)
	{
		ComponentSerialization.STREAM_CODEC.encode(buf, line.text());
		buf.writeInt(line.color());
	}
}
