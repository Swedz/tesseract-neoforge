package net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.swedz.tesseract.neoforge.tooltip.TranslatableTextEnum;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class ModularMultiblockGuiContent
{
	private final List<ModularMultiblockGuiLine> lines = Lists.newArrayList();
	
	ModularMultiblockGuiContent()
	{
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
	
	public ModularMultiblockGuiContent add(TranslatableTextEnum text, int color, boolean wrap)
	{
		return this.add(text.text(), color, wrap);
	}
	
	public ModularMultiblockGuiContent add(Component text, int color)
	{
		return this.add(new ModularMultiblockGuiLine(text, color));
	}
	
	public ModularMultiblockGuiContent add(TranslatableTextEnum text, int color)
	{
		return this.add(text.text(), color);
	}
	
	public ModularMultiblockGuiContent add(Component text)
	{
		return this.add(new ModularMultiblockGuiLine(text));
	}
	
	public ModularMultiblockGuiContent add(TranslatableTextEnum text)
	{
		return this.add(text.text());
	}
	
	public ModularMultiblockGuiContent addAll(Collection<ModularMultiblockGuiLine> lines)
	{
		this.lines.addAll(lines);
		return this;
	}
	
	public List<ModularMultiblockGuiLine> lines()
	{
		return Collections.unmodifiableList(lines);
	}
}
