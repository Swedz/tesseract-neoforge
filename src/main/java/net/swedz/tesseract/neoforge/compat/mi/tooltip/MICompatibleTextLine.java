package net.swedz.tesseract.neoforge.compat.mi.tooltip;

import aztech.modern_industrialization.MIText;
import aztech.modern_industrialization.MITooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.tooltip.Parser;
import net.swedz.tesseract.neoforge.tooltip.TextLine;
import net.swedz.tesseract.neoforge.tooltip.TranslatableTextEnum;

import static aztech.modern_industrialization.MITooltips.*;

public final class MICompatibleTextLine extends TextLine
{
	public static MICompatibleTextLine line(TranslatableTextEnum text, Style style)
	{
		return new MICompatibleTextLine(text, style);
	}
	
	public static MICompatibleTextLine line(MIText text, Style style)
	{
		return new MICompatibleTextLine(text, style);
	}
	
	public static MICompatibleTextLine line(TranslatableTextEnum text)
	{
		return new MICompatibleTextLine(text, DEFAULT_STYLE);
	}
	
	public static MICompatibleTextLine line(MIText text)
	{
		return new MICompatibleTextLine(text);
	}
	
	public MICompatibleTextLine(TranslatableTextEnum text, Style style)
	{
		super(text, style);
	}
	
	public MICompatibleTextLine(MIText text, Style style)
	{
		this(MITooltipCompat.wrap(text), style);
	}
	
	public MICompatibleTextLine(TranslatableTextEnum text)
	{
		this(text, MITooltips.DEFAULT_STYLE);
	}
	
	public MICompatibleTextLine(MIText text)
	{
		this(MITooltipCompat.wrap(text));
	}
	
	@Override
	public <T> MICompatibleTextLine arg(T arg, Parser<T> parser)
	{
		super.arg(arg, parser);
		return this;
	}
	
	public <T> MICompatibleTextLine arg(T arg, MITooltips.Parser<T> parser)
	{
		this.arg(arg, MITooltipCompat.wrap(parser));
		return this;
	}
	
	public MICompatibleTextLine arg(Object arg)
	{
		if(arg instanceof Component component && !component.getStyle().isEmpty())
		{
			return this.arg(component, Parser.COMPONENT);
		}
		return this.arg(arg, MITooltips.DEFAULT_PARSER);
	}
}
