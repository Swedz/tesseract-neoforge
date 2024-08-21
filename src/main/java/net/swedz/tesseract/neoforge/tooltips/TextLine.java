package net.swedz.tesseract.neoforge.tooltips;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class TextLine implements Component
{
	protected final TranslatableTextEnum text;
	protected final Style                style;
	
	protected final List<Component> arguments = Lists.newArrayList();
	
	private MutableComponent component;
	
	public TextLine(TranslatableTextEnum text, Style style)
	{
		this.text = text;
		this.style = style;
	}
	
	public TextLine(TranslatableTextEnum text)
	{
		this(text, null);
	}
	
	public <T> TextLine arg(T arg, Parser<T> parser)
	{
		arguments.add(parser.parse(arg));
		this.markDirty();
		return this;
	}
	
	protected void markDirty()
	{
		component = null;
	}
	
	protected MutableComponent createComponent()
	{
		MutableComponent component = text.text(arguments.toArray());
		if(style != null)
		{
			component = component.withStyle(style);
		}
		return component;
	}
	
	private MutableComponent component()
	{
		if(component == null)
		{
			component = this.createComponent();
		}
		return component;
	}
	
	@Override
	public Style getStyle()
	{
		return this.component().getStyle();
	}
	
	@Override
	public ComponentContents getContents()
	{
		return this.component().getContents();
	}
	
	@Override
	public List<Component> getSiblings()
	{
		return this.component().getSiblings();
	}
	
	@Override
	public FormattedCharSequence getVisualOrderText()
	{
		return this.component().getVisualOrderText();
	}
}
