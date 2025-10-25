package net.swedz.tesseract.neoforge.tooltip;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

@Deprecated(forRemoval = true)
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
	
	protected final String translationKey;
	protected final Style  style;
	
	protected final List<Component> arguments = Lists.newArrayList();
	
	private MutableComponent component;
	
	public TextLine(String translationKey, Style style)
	{
		this.translationKey = translationKey;
		this.style = style;
	}
	
	public TextLine(String translationKey)
	{
		this(translationKey, null);
	}
	
	public TextLine(TranslatableTextEnum text, Style style)
	{
		this(text.getTranslationKey(), style);
	}
	
	public TextLine(TranslatableTextEnum text)
	{
		this(text.getTranslationKey());
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
		return arg instanceof Component c ?
				this.arg(c, Parser.COMPONENT) :
				this.arg(arg, Parser.OBJECT);
	}
	
	protected void markDirty()
	{
		component = null;
	}
	
	protected MutableComponent createComponent()
	{
		MutableComponent component = Component.translatable(translationKey, arguments.toArray());
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
		TextLine line = new TextLine(translationKey, style);
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
