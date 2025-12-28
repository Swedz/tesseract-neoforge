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
import net.swedz.tesseract.neoforge.tooltip.Parser;

import java.util.Map;
import java.util.function.Supplier;

public final class LangManager extends InterfaceProxyManager<LangHandler>
{
	private final String modId;
	
	private final Map<String, Supplier<Style>>        styles  = Maps.newHashMap();
	private final Map<ParserKey, Supplier<Parser<?>>> parsers = Maps.newHashMap();
	
	private record ParserKey(String key, Class<?> paramClass)
	{
	}
	
	public LangManager(String modId)
	{
		this.modId = modId;
		
		this.style(() -> Style.EMPTY);
		
		this.parser(Component.class, () -> Parser.COMPONENT);
	}
	
	String modId()
	{
		return modId;
	}
	
	public LangManager style(String key, Supplier<Style> style)
	{
		Assert.noneNull(key, style);
		styles.put(key, style);
		return this;
	}
	
	public LangManager style(Supplier<Style> style)
	{
		return this.style("default", style);
	}
	
	public LangManager builtinColorStyles()
	{
		for(var formatting : ChatFormatting.values())
		{
			if(formatting.isColor())
			{
				this.style(formatting.getName().toLowerCase(), () -> Style.EMPTY.withColor(formatting).withItalic(false));
			}
		}
		return this;
	}
	
	Supplier<Style> getStyle(String key)
	{
		Assert.notNull(key);
		return styles.get(key);
	}
	
	public <T> LangManager parser(String key, Class<T> paramClass, Supplier<Parser<T>> parser)
	{
		Assert.noneNull(key, paramClass, parser);
		parsers.put(new ParserKey(key, paramClass), parser::get);
		return this;
	}
	
	public <T> LangManager parser(Class<T> paramClass, Supplier<Parser<T>> parser)
	{
		return this.parser("default", paramClass, parser);
	}
	
	public LangManager builtinParsers()
	{
		return this
				.parser(ResourceKey.class, () -> Parser.RESOURCE_KEY::parse)
				.parser(ItemStack.class, () -> Parser.ITEM_STACK)
				.parser(Item.class, () -> Parser.ITEM)
				.parser("item", Identifier.class, () -> Parser.ITEM_ID)
				.parser(Block.class, () -> Parser.BLOCK)
				.parser(BlockState.class, () -> Parser.BLOCK_STATE)
				.parser("block", Identifier.class, () -> Parser.BLOCK_ID)
				.parser(Fluid.class, () -> Parser.FLUID)
				.parser(EntityType.class, () -> Parser.ENTITY_TYPE)
				.parser("keybind", String.class, () -> Parser.KEYBIND)
				.parser(BlockPos.class, () -> Parser.BLOCK_POS)
				.parser(GlobalPos.class, () -> Parser.GLOBAL_POS)
				.parser(WorldPos.class, () -> Parser.WORLD_POS);
	}
	
	Supplier<Parser<?>> getParser(String key, Class<?> paramClass)
	{
		Assert.noneNull(key, paramClass);
		return parsers.get(new ParserKey(key, paramClass));
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
