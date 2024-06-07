package net.swedz.tesseract.neoforge.compat.mi.hook;

import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.neoforge.registry.SortOrder;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

public interface MIHookRegistry
{
	DeferredRegister.Blocks blockRegistry();
	
	DeferredRegister<BlockEntityType<?>> blockEntityRegistry();
	
	DeferredRegister.Items itemRegistry();
	
	DeferredRegister<RecipeSerializer<?>> recipeSerializerRegistry();
	
	DeferredRegister<RecipeType<?>> recipeTypeRegistry();
	
	void onBlockRegister(BlockHolder holder);
	
	void onBlockEntityRegister(BlockEntityType<?> type);
	
	void onItemRegister(ItemHolder holder);
	
	void onMachineRecipeTypeRegister(MachineRecipeType type);
	
	SortOrder sortOrderMachines();
}
