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
	
	MIHookRegistry NONE = new MIHookRegistry() {
		@Override
		public DeferredRegister.Blocks blockRegistry()
		{
			return null;
		}
		
		@Override
		public DeferredRegister<BlockEntityType<?>> blockEntityRegistry()
		{
			return null;
		}
		
		@Override
		public DeferredRegister.Items itemRegistry()
		{
			return null;
		}
		
		@Override
		public DeferredRegister<RecipeSerializer<?>> recipeSerializerRegistry()
		{
			return null;
		}
		
		@Override
		public DeferredRegister<RecipeType<?>> recipeTypeRegistry()
		{
			return null;
		}
		
		@Override
		public void onBlockRegister(BlockHolder holder)
		{
		
		}
		
		@Override
		public void onBlockEntityRegister(BlockEntityType<?> type)
		{
		
		}
		
		@Override
		public void onItemRegister(ItemHolder holder)
		{
		
		}
		
		@Override
		public void onMachineRecipeTypeRegister(MachineRecipeType type)
		{
		
		}
		
		@Override
		public SortOrder sortOrderMachines()
		{
			return null;
		}
	};
}
