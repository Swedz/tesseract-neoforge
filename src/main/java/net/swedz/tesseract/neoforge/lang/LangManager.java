package net.swedz.tesseract.neoforge.lang;

import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.api.WorldPos;
import net.swedz.tesseract.neoforge.interfaceproxy.InterfaceProxyManager;
import net.swedz.tesseract.neoforge.lang.exception.UndefinedParserException;
import net.swedz.tesseract.neoforge.lang.exception.UndefinedStyleException;
import net.swedz.tesseract.neoforge.lang.parser.ParserProvider;
import net.swedz.tesseract.neoforge.lang.style.StyleProvider;
import net.swedz.tesseract.neoforge.tooltip.Parser;

import java.util.Map;
import java.util.function.Supplier;

public final class LangManager extends InterfaceProxyManager<LangHandler>
{
	private final String modId;
	
	private final Map<String, StyleProvider>        styles  = Maps.newHashMap();
	private final Map<ParserKey, ParserProvider<?>> parsers = Maps.newHashMap();
	
	private record ParserKey(String key, Class<?> paramClass)
	{
	}
	
	public LangManager(String modId)
	{
		this.modId = modId;
		
		this.style(Style.EMPTY);
		
		this.parser(Component.class, Parser.COMPONENT);
	}
	
	String modId()
	{
		return modId;
	}
	
	public LangManager style(String key, StyleProvider style)
	{
		Assert.noneNull(key, style);
		styles.put(key, style);
		return this;
	}
	
	public LangManager style(StyleProvider style)
	{
		return this.style("default", style);
	}
	
	public LangManager style(String key, Supplier<Style> style)
	{
		return this.style(key, StyleProvider.simple(style));
	}
	
	public LangManager style(String key, Style style)
	{
		return this.style(key, StyleProvider.simple(style));
	}
	
	public LangManager style(Supplier<Style> style)
	{
		return this.style(StyleProvider.simple(style));
	}
	
	public LangManager style(Style style)
	{
		Assert.noneNull(style);
		return this.style(StyleProvider.simple(style));
	}
	
	public LangManager builtinColorStyles()
	{
		for(var formatting : ChatFormatting.values())
		{
			if(formatting.isColor())
			{
				this.style(formatting.getName().toLowerCase(), Style.EMPTY.withColor(formatting).withItalic(false));
			}
		}
		return this;
	}
	
	StyleProvider getStyle(String key)
	{
		Assert.notNull(key);
		
		var style = styles.get(key);
		
		if(style == null)
		{
			throw new UndefinedStyleException(key);
		}
		
		return style;
	}
	
	public <T> LangManager parser(String key, Class<T> paramClass, ParserProvider<T> parser)
	{
		Assert.noneNull(key, paramClass, parser);
		parsers.put(new ParserKey(key, paramClass), parser);
		return this;
	}
	
	public <T> LangManager parser(Class<T> paramClass, ParserProvider<T> parser)
	{
		return this.parser("default", paramClass, parser);
	}
	
	public <T> LangManager parser(String key, Class<T> paramClass, Supplier<Parser<T>> parser)
	{
		return this.parser(key, paramClass, ParserProvider.simple(parser));
	}
	
	public <T> LangManager parser(Class<T> paramClass, Supplier<Parser<T>> parser)
	{
		return this.parser(paramClass, ParserProvider.simple(parser));
	}
	
	public <T> LangManager parser(String key, Class<T> paramClass, Parser<T> parser)
	{
		return this.parser(key, paramClass, ParserProvider.simple(parser));
	}
	
	public <T> LangManager parser(Class<T> paramClass, Parser<T> parser)
	{
		return this.parser(paramClass, ParserProvider.simple(parser));
	}
	
	public LangManager builtinParsers()
	{
		return this
				.parser(ResourceKey.class, Parser.RESOURCE_KEY::parse)
				.parser(ItemStack.class, Parser.ITEM_STACK)
				.parser(Item.class, Parser.ITEM)
				.parser("item", Identifier.class, Parser.ITEM_ID)
				.parser(Block.class, Parser.BLOCK)
				.parser(BlockState.class, Parser.BLOCK_STATE)
				.parser("block", Identifier.class, Parser.BLOCK_ID)
				.parser(Fluid.class, Parser.FLUID)
				.parser(EntityType.class, Parser.ENTITY_TYPE)
				.parser("keybind", String.class, Parser.KEYBIND)
				.parser(BlockPos.class, Parser.BLOCK_POS)
				.parser(GlobalPos.class, Parser.GLOBAL_POS)
				.parser(WorldPos.class, Parser.WORLD_POS);
	}
	
	ParserProvider<?> getParser(String key, Class<?> paramClass)
	{
		Assert.noneNull(key, paramClass);
		
		var parser = parsers.get(new ParserKey(key, paramClass));
		
		if(parser == null)
		{
			if(key.equals("default"))
			{
				parser = ParserProvider.simple(Parser.OBJECT);
			}
			else
			{
				throw new UndefinedParserException(key);
			}
		}
		
		return parser;
	}
	
	@Override
	protected <P> LangHandler createHandler(Class<P> proxyClass)
	{
		return new LangHandler(this);
	}
	
	@Override
	protected <P> LangInstance<P> createInstance(Class<P> proxyClass, P proxy, LangHandler handler)
	{
		return new LangInstance<>(proxyClass, proxy, handler);
	}
	
	@Override
	public <P> LangInstance<P> build(Class<P> proxyClass)
	{
		return (LangInstance<P>) super.build(proxyClass);
	}
}
