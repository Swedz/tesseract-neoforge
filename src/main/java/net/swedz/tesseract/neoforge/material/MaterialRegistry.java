package net.swedz.tesseract.neoforge.material;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.RegisteredMaterialPart;
import net.swedz.tesseract.neoforge.material.part.recipe.MaterialRecipeGroup;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

public abstract class MaterialRegistry
{
	public abstract String modId();
	
	public final ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(this.modId(), path);
	}
	
	public abstract DeferredRegister.Blocks blockRegistry();
	
	public abstract DeferredRegister<BlockEntityType<?>> blockEntityRegistry();
	
	public abstract DeferredRegister.Items itemRegistry();
	
	public abstract void onBlockRegister(BlockHolder holder);
	
	public abstract void onBlockEntityRegister(BlockEntityType<?> type);
	
	public abstract void onItemRegister(ItemHolder holder);
	
	public final RegisteredMaterialPart create(Material material, MaterialPart part)
	{
		RegisteredMaterialPart registered = part.register(this, material);
		material.add(part, registered);
		return registered;
	}
	
	public final void createRecipesFor(Material material, MaterialRecipeGroup recipeGroup, RecipeOutput recipes)
	{
		material.parts().forEach((part, registeredPart) ->
		{
			ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(registeredPart.asItem());
			if(itemId.getNamespace().equals(this.modId()))
			{
				recipeGroup.create(material, part, registeredPart, recipes);
			}
		});
	}
	
	public final void createRecipesFor(Material material, RecipeOutput recipes)
	{
		for(MaterialRecipeGroup recipeGroup : material.recipes())
		{
			createRecipesFor(material, recipeGroup, recipes);
		}
	}
}
