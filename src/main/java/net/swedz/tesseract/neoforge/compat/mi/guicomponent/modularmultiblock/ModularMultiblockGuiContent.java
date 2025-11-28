package net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock;

import com.google.common.collect.Lists;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class ModularMultiblockGuiContent
{
	public static final StreamCodec<RegistryFriendlyByteBuf, ModularMultiblockGuiContent> STREAM_CODEC = ModularMultiblockGuiLine.STREAM_CODEC
			.apply(ByteBufCodecs.list())
			.map(ModularMultiblockGuiContent::new, ModularMultiblockGuiContent::lines);
	
	private final List<ModularMultiblockGuiLine> lines;
	
	ModularMultiblockGuiContent(List<ModularMultiblockGuiLine> lines)
	{
		this.lines = lines;
	}
	
	ModularMultiblockGuiContent()
	{
		this(Lists.newArrayList());
	}
	
	public List<ModularMultiblockGuiLine> lines()
	{
		return Collections.unmodifiableList(lines);
	}
	
	public ModularMultiblockGuiContent add(ModularMultiblockGuiLine line)
	{
		lines.add(line);
		return this;
	}
	
	public ModularMultiblockGuiContent add(Component text, int color, boolean wrap)
	{
		return this.add(new ModularMultiblockGuiLine(text, color, wrap));
	}
	
	public ModularMultiblockGuiContent add(Component text, int color)
	{
		return this.add(new ModularMultiblockGuiLine(text, color));
	}
	
	public ModularMultiblockGuiContent add(Component text)
	{
		return this.add(new ModularMultiblockGuiLine(text));
	}
	
	public ModularMultiblockGuiContent addAll(Collection<ModularMultiblockGuiLine> lines)
	{
		lines.forEach(this::add);
		return this;
	}
}
