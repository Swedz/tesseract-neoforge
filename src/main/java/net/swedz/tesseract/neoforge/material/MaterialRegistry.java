package net.swedz.tesseract.neoforge.material;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.RegisteredMaterialPart;
import net.swedz.tesseract.neoforge.material.recipe.MaterialRecipeGroup;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.Optional;
import java.util.function.Function;

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
	
	public final boolean includes(Material material, MaterialPart part)
	{
		ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(material.get(part).asItem());
		return this.modId().equals(itemId.getNamespace());
	}
	
	public final RegisteredMaterialPart create(Material material, MaterialPart part)
	{
		RegisteredMaterialPart registered = part.register(this, material);
		material.add(part, registered);
		return registered;
	}
	
	public final void createRecipesFor(Material material, MaterialRecipeGroup recipeGroup, RecipeOutput recipes)
	{
		recipeGroup.create(this, material, recipes);
	}
	
	public final void createRecipesFor(Material material, Function<MaterialRecipeGroup, Optional<MaterialRecipeGroup>> recipeGroupFilter, RecipeOutput recipes)
	{
		for(MaterialRecipeGroup recipeGroup : material.recipeGroups())
		{
			Optional<MaterialRecipeGroup> targetRecipeGroup = recipeGroupFilter.apply(recipeGroup);
			targetRecipeGroup.ifPresent((group) -> createRecipesFor(material, group, recipes));
		}
	}
	
	public final void createRecipesFor(Material material, RecipeOutput recipes)
	{
		createRecipesFor(material, Optional::of, recipes);
	}
}
