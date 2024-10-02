package net.swedz.tesseract.neoforge.tooltip;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class TextLine implements Component, Parsable
{
	public static TextLine line(TranslatableTextEnum text, Style style)
	{
		return new TextLine(text, style);
	}
	
	public static TextLine line(TranslatableTextEnum text)
	{
		return new TextLine(text);
	}
	
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
	
	@Override
	public <T> TextLine arg(T arg, Parser<T> parser)
	{
		arguments.add(parser.parse(arg));
		this.markDirty();
		return this;
	}
	
	@Override
	public <A, B> TextLine arg(A a, B b, BiParser<A, B> parser)
	{
		arguments.add(parser.parse(a, b));
		this.markDirty();
		return this;
	}
	
	@Override
	public TextLine arg(Object arg)
	{
		return arg instanceof Component c && !c.getStyle().isEmpty() ?
				this.arg(c, Parser.COMPONENT) :
				this.arg(arg, Parser.OBJECT);
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
	
	public TextLine withStyle(Style style)
	{
		TextLine line = new TextLine(text, style);
		line.arguments.addAll(arguments);
		return line;
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
