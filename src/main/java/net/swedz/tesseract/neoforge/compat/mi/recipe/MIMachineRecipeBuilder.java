package net.swedz.tesseract.neoforge.compat.mi.recipe;

import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MIRecipeJson;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import com.google.common.collect.Sets;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapedRecipeBuilder;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapelessRecipeBuilder;
import net.swedz.tesseract.neoforge.helper.RecipeHelper;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.recipe.RecipeOfferable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MIMachineRecipeBuilder extends MIRecipeJson<MIMachineRecipeBuilder> implements RecipeOfferable
{
	protected Optional<Material> defaultMaterial = Optional.empty();
	
	protected final Set<MaterialPart> involvedParts = Sets.newHashSet();
	
	public MIMachineRecipeBuilder(HolderGetter<Fluid> fluids, HolderGetter<Item> items, MachineRecipeType type, int eu, int duration)
	{
		super(fluids, items, type, eu, duration);
	}
	
	public MIMachineRecipeBuilder(HolderLookup.Provider registries, MachineRecipeType type, int eu, int duration)
	{
		this(registries.lookupOrThrow(Registries.FLUID), registries.lookupOrThrow(Registries.ITEM), type, eu, duration);
	}
	
	public MIMachineRecipeBuilder(MIRecipeJson<?> otherWithSameData)
	{
		super(otherWithSameData);
	}
	
	public static MIMachineRecipeBuilder fromShaped(ShapedRecipeBuilder shaped, MachineRecipeType machine, int eu, int duration, int division)
	{
		var result = shaped.result();
		if(result == null)
		{
			throw new NullPointerException("No result set for recipe");
		}
		if(result.count() % division != 0)
		{
			throw new IllegalArgumentException("Output must be divisible by division");
		}
		
		MIMachineRecipeBuilder machineRecipe = new MIMachineRecipeBuilder(shaped.registries(), machine, eu, duration)
				.itemOut(new ItemStackTemplate(result.item(), result.count() / division, result.components()), 1);
		for(Map.Entry<Character, Ingredient> entry : shaped.key().entrySet())
		{
			int count = 0;
			for(String row : shaped.pattern())
			{
				for(char c : row.toCharArray())
				{
					if(c == entry.getKey())
					{
						count++;
					}
				}
			}
			
			if(count % division != 0)
			{
				throw new IllegalArgumentException("Input must be divisible by division");
			}
			
			machineRecipe.itemIn(entry.getValue(), count / division, 1);
		}
		
		return machineRecipe;
	}
	
	public static MIMachineRecipeBuilder fromShapedToAssembler(ShapedRecipeBuilder shaped)
	{
		return fromShaped(shaped, MIMachineRecipeTypes.ASSEMBLER, 8, 200, 1);
	}
	
	public static MIMachineRecipeBuilder fromShapeless(ShapelessRecipeBuilder shaped, MachineRecipeType machine, int eu, int duration, int division)
	{
		var result = shaped.result();
		if(result == null)
		{
			throw new NullPointerException("No result set for recipe");
		}
		if(result.count() % division != 0)
		{
			throw new IllegalArgumentException("Output must be divisible by division");
		}
		
		MIMachineRecipeBuilder machineRecipe = new MIMachineRecipeBuilder(shaped.registries(), machine, eu, duration)
				.itemOut(new ItemStackTemplate(result.item(), result.count() / division, result.components()), 1);
		for(Ingredient ingredient : shaped.input())
		{
			int count = 0;
			for(Ingredient other : shaped.input())
			{
				if(ingredient.equals(other))
				{
					count++;
				}
			}
			
			if(count % division != 0)
			{
				throw new IllegalArgumentException("Input must be divisible by division");
			}
			
			machineRecipe.itemIn(ingredient, count / division, 1);
		}
		
		return machineRecipe;
	}
	
	public static MIMachineRecipeBuilder fromShapelessToPacker(ShapelessRecipeBuilder shapeless)
	{
		return fromShapeless(shapeless, MIMachineRecipeTypes.PACKER, 2, 5 * 20, 1);
	}
	
	public static MIMachineRecipeBuilder fromShapelessToUnpackerAndFlip(ShapelessRecipeBuilder shapeless)
	{
		return fromShapeless(shapeless, MIMachineRecipeTypes.UNPACKER, 2, 5 * 20, 1).flip();
	}
	
	public MIMachineRecipeBuilder forMaterial(Material material)
	{
		this.defaultMaterial = Optional.ofNullable(material);
		return this;
	}
	
	public MIMachineRecipeBuilder flip()
	{
		MIMachineRecipeBuilder inversedRecipe = new MIMachineRecipeBuilder(fluids, items, (MachineRecipeType) recipe.getType(), recipe.eu, recipe.duration).forMaterial(defaultMaterial.orElse(null));
		
		for(MachineRecipe.ItemInput itemInput : recipe.itemInputs)
		{
			inversedRecipe.itemOut(new ItemStackTemplate(itemInput.ingredient().getValues().get(0), itemInput.amount()), itemInput.probability());
		}
		
		for(MachineRecipe.FluidInput fluidInput : recipe.fluidInputs)
		{
			inversedRecipe.fluidIn(fluidInput.getInputFluids().getFirst(), (int) fluidInput.amount(), fluidInput.probability());
		}
		
		for(MachineRecipe.ItemOutput itemOutput : recipe.itemOutputs)
		{
			inversedRecipe.itemIn(Ingredient.of(HolderSet.direct(itemOutput.template().item())), itemOutput.template().count(), itemOutput.probability());
		}
		
		for(MachineRecipe.FluidOutput fluidOutput : recipe.fluidOutputs)
		{
			inversedRecipe.fluidIn(fluidOutput.fluid(), (int) fluidOutput.amount(), fluidOutput.probability());
		}
		
		return inversedRecipe;
	}
	
	@Override
	public void validate()
	{
	}
	
	@Override
	public Recipe<?> convert()
	{
		return recipe;
	}
	
	public Set<MaterialPart> involvedParts()
	{
		return Sets.newHashSet(involvedParts);
	}
	
	public MIMachineRecipeBuilder addCondition(MachineProcessCondition condition)
	{
		recipe.conditions.add(condition);
		return this;
	}
	
	public MIMachineRecipeBuilder itemIn(Identifier itemId, int amount, float probability)
	{
		return this.itemIn(RecipeHelper.ingredient(itemId), amount, probability);
	}
	
	public MIMachineRecipeBuilder itemIn(Identifier itemId, int amount)
	{
		return this.itemIn(itemId, amount, 1);
	}
	
	public MIMachineRecipeBuilder itemOut(Identifier itemId, int amount, float probability)
	{
		return this.itemOut(new ItemStackTemplate(BuiltInRegistries.ITEM.get(itemId).orElseThrow(), amount), probability);
	}
	
	public MIMachineRecipeBuilder itemOut(Identifier itemId, int amount)
	{
		return this.itemOut(itemId, amount, 1);
	}
	
	public MIMachineRecipeBuilder partIn(Material material, MaterialPart part, int count, float probability)
	{
		involvedParts.add(part);
		return material.has(part) ? this.itemIn(material.get(part).itemReference(), count, probability) : this;
	}
	
	public MIMachineRecipeBuilder partIn(Material material, MaterialPart part, int count)
	{
		involvedParts.add(part);
		return material.has(part) ? this.itemIn(material.get(part).itemReference(), count, 1f) : this;
	}
	
	public MIMachineRecipeBuilder partIn(MaterialPart part, int count, float probability)
	{
		return this.partIn(defaultMaterial.orElseThrow(), part, count, probability);
	}
	
	public MIMachineRecipeBuilder partIn(MaterialPart part, int count)
	{
		return this.partIn(defaultMaterial.orElseThrow(), part, count);
	}
	
	public MIMachineRecipeBuilder addPartOutput(Material material, MaterialPart part, int count, float probability)
	{
		involvedParts.add(part);
		return material.has(part) ? this.itemOut(material.get(part).asItem(), count, probability) : this;
	}
	
	public MIMachineRecipeBuilder addPartOutput(Material material, MaterialPart part, int count)
	{
		involvedParts.add(part);
		return material.has(part) ? this.itemOut(material.get(part).asItem(), count, 1f) : this;
	}
	
	public MIMachineRecipeBuilder addPartOutput(MaterialPart part, int count, float probability)
	{
		return this.addPartOutput(defaultMaterial.orElseThrow(), part, count, probability);
	}
	
	public MIMachineRecipeBuilder addPartOutput(MaterialPart part, int count)
	{
		return this.addPartOutput(defaultMaterial.orElseThrow(), part, count);
	}
}
