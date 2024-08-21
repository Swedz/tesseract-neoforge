package net.swedz.tesseract.neoforge.tooltips;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public interface Parser<T>
{
	Parser<Item>        ITEM        = (item) -> item.getDefaultInstance().getHoverName().copy();
	Parser<Block>       BLOCK       = Block::getName;
	Parser<BlockState>  BLOCK_STATE = (blockState) -> BLOCK.parse(blockState.getBlock());
	Parser<Fluid>       FLUID       = (fluid) -> fluid.getFluidType().getDescription();
	Parser<Enchantment> ENCHANTMENT = (enchantment) -> enchantment.description().copy();
	Parser<EntityType>  ENTITY_TYPE = (entityType) -> entityType.getDescription().copy();
	Parser<String>      KEYBIND     = (key) -> Component.keybind("key.%s".formatted(key));
	Parser<Component>   COMPONENT   = (component) -> component;
	Parser<Object>      OBJECT      = (object) -> Component.literal(String.valueOf(object));
	
	Component parse(T value);
	
	default Parser<T> withStyle(Style style)
	{
		return (value) -> this.parse(value).copy().setStyle(style);
	}
}
