package net.swedz.tesseract.neoforge.lang;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.api.WorldPos;
import net.swedz.tesseract.neoforge.interfaceproxy.InterfaceProxyManager;
import net.swedz.tesseract.neoforge.lang.annotation.PlaceholderKey;
import net.swedz.tesseract.neoforge.lang.exception.UndefinedParserException;
import net.swedz.tesseract.neoforge.lang.exception.UndefinedStyleException;
import net.swedz.tesseract.neoforge.lang.parser.ParserProvider;
import net.swedz.tesseract.neoforge.lang.parser.parser.DecimalParser;
import net.swedz.tesseract.neoforge.lang.placeholder.Placeholder;
import net.swedz.tesseract.neoforge.lang.placeholder.PlaceholderFilter;
import net.swedz.tesseract.neoforge.lang.placeholder.PlaceholderProvider;
import net.swedz.tesseract.neoforge.lang.style.StyleProvider;
import net.swedz.tesseract.neoforge.tooltip.Parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public final class LangManager extends InterfaceProxyManager<LangHandler>
{
	private final String modId;
	
	private final Map<String, StyleProvider>        styles       = Maps.newHashMap();
	private final Map<ParserKey, ParserProvider<?>> parsers      = Maps.newHashMap();
	private final Set<Placeholder>                  placeholders = Sets.newHashSet();
	
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
				.parser("item", ResourceLocation.class, Parser.ITEM_ID)
				.parser(Block.class, Parser.BLOCK)
				.parser(BlockState.class, Parser.BLOCK_STATE)
				.parser("block", ResourceLocation.class, Parser.BLOCK_ID)
				.parser(Fluid.class, Parser.FLUID)
				.parser(EntityType.class, Parser.ENTITY_TYPE)
				.parser("keybind", String.class, Parser.KEYBIND)
				.parser(BlockPos.class, Parser.BLOCK_POS)
				.parser(GlobalPos.class, Parser.GLOBAL_POS)
				.parser(WorldPos.class, Parser.WORLD_POS)
				
				.parser(float.class, new DecimalParser<>())
				.parser(double.class, new DecimalParser<>())
				.parser(Float.class, new DecimalParser<>())
				.parser(Double.class, new DecimalParser<>());
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
	
	public LangManager placeholder(PlaceholderFilter filter, PlaceholderProvider provider)
	{
		Assert.noneNull(filter, provider);
		placeholders.add(new Placeholder(filter, provider));
		return this;
	}
	
	public LangManager placeholder(PlaceholderFilter filter, Component component)
	{
		Assert.noneNull(filter, component);
		return this.placeholder(filter, PlaceholderProvider.simple(component));
	}
	
	public LangManager placeholder(PlaceholderFilter filter, Supplier<Component> provider)
	{
		Assert.noneNull(filter, provider);
		return this.placeholder(filter, PlaceholderProvider.simple(provider));
	}
	
	public LangManager placeholder(String block, PlaceholderProvider provider)
	{
		Assert.noneNull(block, provider);
		return this.placeholder((check) -> check.equals(block), provider);
	}
	
	public LangManager placeholder(String block, Component component)
	{
		Assert.noneNull(block, component);
		return this.placeholder(block, PlaceholderProvider.simple(component));
	}
	
	public LangManager placeholder(String block, Supplier<Component> provider)
	{
		Assert.noneNull(block, provider);
		return this.placeholder(block, PlaceholderProvider.simple(provider));
	}
	
	public <T extends PlaceholderFilter & PlaceholderProvider> LangManager placeholder(T placeholder)
	{
		Assert.notNull(placeholder);
		return this.placeholder(placeholder, placeholder);
	}
	
	private void annotatedPlaceholder(LangInstance<?> instance, Method method, PlaceholderKey placeholderKey)
	{
		var key = placeholderKey.value();
		this.placeholder(key, () ->
		{
			try
			{
				return (MutableComponent) method.invoke(instance.proxy());
			}
			catch (IllegalAccessException | InvocationTargetException ex)
			{
				throw new RuntimeException(ex);
			}
		});
	}
	
	private void includeAnnotatedPlaceholders(LangInstance<?> instance)
	{
		for(var method : instance.proxyClass().getMethods())
		{
			if(method.isAnnotationPresent(PlaceholderKey.class))
			{
				var methodSignature = method.toGenericString();
				
				if(!method.getReturnType().equals(MutableComponent.class))
				{
					throw new IllegalStateException("Method %s does not return MutableComponent".formatted(methodSignature));
				}
				
				if(method.getParameterCount() > 0)
				{
					throw new IllegalStateException("Method with signature %s is annotated with @PlaceholderKey but has parameters".formatted(methodSignature));
				}
				
				var placeholderKey = method.getAnnotation(PlaceholderKey.class);
				this.annotatedPlaceholder(instance, method, placeholderKey);
			}
		}
	}
	
	Optional<Placeholder> getPlaceholder(String block)
	{
		Assert.notNull(block);
		
		for(var placeholder : placeholders)
		{
			if(placeholder.test(block))
			{
				return Optional.of(placeholder);
			}
		}
		
		return Optional.empty();
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
		var instance = (LangInstance<P>) super.build(proxyClass);
		this.includeAnnotatedPlaceholders(instance);
		return instance;
	}
}
