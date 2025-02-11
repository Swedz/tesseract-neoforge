package net.swedz.tesseract.neoforge.tooltip;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.swedz.tesseract.neoforge.api.tuple.Pair;

public interface Parser<T>
{
	// @formatter:off
	Parser<Component>                                         COMPONENT                 = (component) -> component;
	Parser<Object>                                            OBJECT                    = (object) -> Component.literal(String.valueOf(object));
	
	Parser<Integer>                                           INTEGER_PERCENTAGE        = (value) -> Component.literal(String.valueOf(value)).append("%");
	Parser<Integer>                                           INTEGER_PERCENTAGE_SPACED = (value) -> Component.literal(String.valueOf(value)).append(" %");
	BiParser<Double, Integer>                                 DOUBLE                    = (value, precision) -> Component.literal(("%." + precision + "f").formatted(value));
	BiParser<Double, Integer>                                 DOUBLE_PERCENTAGE         = (value, precision) -> DOUBLE.parse(value * 100, precision).copy().append("%");
	BiParser<Double, Integer>                                 DOUBLE_PERCENTAGE_SPACED  = (value, precision) -> DOUBLE.parse(value * 100, precision).copy().append(" %");
	BiParser<Float, Integer>                                  FLOAT                     = (value, precision) -> Component.literal(("%." + precision + "f").formatted(value));
	BiParser<Float, Integer>                                  FLOAT_PERCENTAGE          = (value, precision) -> FLOAT.parse(value * 100, precision).copy().append("%");
	BiParser<Float, Integer>                                  FLOAT_PERCENTAGE_SPACED   = (value, precision) -> FLOAT.parse(value * 100, precision).copy().append(" %");
	
	Parser<ItemStack>                                         ITEM_STACK                = (stack) -> stack.getHoverName().copy();
	Parser<Item>                                              ITEM                      = (item) -> ITEM_STACK.parse(item.getDefaultInstance());
	
	Parser<Block>                                             BLOCK                     = Block::getName;
	Parser<BlockState>                                        BLOCK_STATE               = (blockState) -> BLOCK.parse(blockState.getBlock());
	
	Parser<Fluid>                                             FLUID                     = (fluid) -> fluid.getFluidType().getDescription();
	
	BiParser<HolderLookup.Provider, ResourceKey<Enchantment>>                ENCHANTMENT           = (registries, enchantment) -> registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment).value().description().copy();
	Parser<Integer>                                                          ENCHANTMENT_LEVEL     = (level) -> Component.translatable("enchantment.level.%d".formatted(level));
	BiParser<HolderLookup.Provider, Pair<ResourceKey<Enchantment>, Integer>> ENCHANTMENT_AND_LEVEL = (registries, value) ->
	{
		var enchantment = registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(value.a()).value();
		var level = value.b();
		var component = enchantment.description().copy();
		if(level != 1 || enchantment.getMaxLevel() != 1)
		{
			component.append(ENCHANTMENT_LEVEL.parse(level));
		}
		return component;
	};
	
	Parser<EntityType>                                        ENTITY_TYPE               = (entityType) -> entityType.getDescription().copy();
	
	Parser<String>                                            KEYBIND                   = (key) -> Component.keybind("key.%s".formatted(key));
	
	Parser<BlockPos>                                          BLOCK_POS                 = (pos) -> Component.literal(pos.toShortString());
	BiParser<ResourceKey<Level>, BlockPos>                    DIMENSION_POS             = (dimension, pos) -> Component.literal("%s (%s)".formatted(pos.toShortString(), dimension.location().toString()));
	BiParser<Level, BlockPos>                                 LEVEL_POS                 = (level, pos) -> DIMENSION_POS.parse(level.dimension(), pos);
	Parser<GlobalPos>                                         GLOBAL_POS                = (pos) -> DIMENSION_POS.parse(pos.dimension(), pos.pos());
	// @formatter:on
	
	Component parse(T value);
	
	default Parser<T> withStyle(Style style)
	{
		return (value) -> this.parse(value).copy().setStyle(style);
	}
	
	default Parser<T> plain()
	{
		return (value) -> this.parse(value).plainCopy();
	}
}
