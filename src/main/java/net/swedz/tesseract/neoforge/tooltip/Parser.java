package net.swedz.tesseract.neoforge.tooltip;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public interface Parser<T>
{
	Parser<ItemStack>                                         ITEM_STACK        = (stack) -> stack.getHoverName().copy();
	Parser<Item>                                              ITEM              = (item) -> ITEM_STACK.parse(item.getDefaultInstance());
	Parser<Block>                                             BLOCK             = Block::getName;
	Parser<BlockState>                                        BLOCK_STATE       = (blockState) -> BLOCK.parse(blockState.getBlock());
	Parser<Fluid>                                             FLUID             = (fluid) -> fluid.getFluidType().getDescription();
	BiParser<HolderLookup.Provider, ResourceKey<Enchantment>> ENCHANTMENT       = (registries, enchantment) -> registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment).value().description().copy();
	Parser<Integer>                                           ENCHANTMENT_LEVEL = (level) -> Component.translatable("enchantment.level.%d".formatted(level));
	Parser<EntityType>                                        ENTITY_TYPE       = (entityType) -> entityType.getDescription().copy();
	Parser<String>                                            KEYBIND           = (key) -> Component.keybind("key.%s".formatted(key));
	Parser<Component>                                         COMPONENT         = (component) -> component;
	Parser<Object>                                            OBJECT            = (object) -> Component.literal(String.valueOf(object));
	
	Component parse(T value);
	
	default Parser<T> withStyle(Style style)
	{
		return (value) -> this.parse(value).copy().setStyle(style);
	}
}
