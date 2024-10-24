package net.swedz.tesseract.neoforge.compat.mi.recipe;

import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MIRecipeJson;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import com.google.common.collect.Sets;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapedRecipeBuilder;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapelessRecipeBuilder;
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
	
	public MIMachineRecipeBuilder(MachineRecipeType type, int eu, int duration)
	{
		super(type, eu, duration);
	}
	
	public MIMachineRecipeBuilder(MIRecipeJson<?> otherWithSameData)
	{
		super(otherWithSameData);
	}
	
	public static MIMachineRecipeBuilder fromShaped(ShapedRecipeBuilder shaped, MachineRecipeType machine, int eu, int duration, int division)
	{
		ItemStack result = shaped.result();
		if(result == null)
		{
			throw new NullPointerException("No result set for recipe");
		}
		if(result.getCount() % division != 0)
		{
			throw new IllegalArgumentException("Output must be divisible by division");
		}
		
		MIMachineRecipeBuilder machineRecipe = new MIMachineRecipeBuilder(machine, eu, duration)
				.addItemOutput(result.getItem(), result.getCount() / division);
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
			
			machineRecipe.addItemInput(entry.getValue(), count / division, 1);
		}
		
		return machineRecipe;
	}
	
	public static MIMachineRecipeBuilder fromShapedToAssembler(ShapedRecipeBuilder shaped)
	{
		return fromShaped(shaped, MIMachineRecipeTypes.ASSEMBLER, 8, 200, 1);
	}
	
	public static MIMachineRecipeBuilder fromShapeless(ShapelessRecipeBuilder shaped, MachineRecipeType machine, int eu, int duration, int division)
	{
		ItemStack result = shaped.result();
		if(result == null)
		{
			throw new NullPointerException("No result set for recipe");
		}
		if(result.getCount() % division != 0)
		{
			throw new IllegalArgumentException("Output must be divisible by division");
		}
		
		MIMachineRecipeBuilder machineRecipe = new MIMachineRecipeBuilder(machine, eu, duration)
				.addItemOutput(result.getItem(), result.getCount() / division);
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
			
			machineRecipe.addItemInput(ingredient, count / division, 1);
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
		MIMachineRecipeBuilder inversedRecipe = new MIMachineRecipeBuilder((MachineRecipeType) recipe.getType(), recipe.eu, recipe.duration).forMaterial(defaultMaterial.orElse(null));
		
		for(MachineRecipe.ItemInput itemInput : recipe.itemInputs)
		{
			inversedRecipe.addItemOutput(itemInput.ingredient().getItems()[0].getItem(), itemInput.amount(), itemInput.probability());
		}
		
		for(MachineRecipe.FluidInput fluidInput : recipe.fluidInputs)
		{
			inversedRecipe.addFluidOutput(fluidInput.fluid(), (int) fluidInput.amount(), fluidInput.probability());
		}
		
		for(MachineRecipe.ItemOutput itemOutput : recipe.itemOutputs)
		{
			inversedRecipe.addItemInput(itemOutput.variant().getItem(), itemOutput.amount(), itemOutput.probability());
		}
		
		for(MachineRecipe.FluidOutput fluidOutput : recipe.fluidOutputs)
		{
			inversedRecipe.addFluidInput(fluidOutput.fluid(), (int) fluidOutput.amount(), fluidOutput.probability());
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
	
	public MIMachineRecipeBuilder addPartInput(Material material, MaterialPart part, int count, float probability)
	{
		involvedParts.add(part);
		return material.has(part) ? this.addItemInput(material.get(part).itemReference(), count, probability) : this;
	}
	
	public MIMachineRecipeBuilder addPartInput(Material material, MaterialPart part, int count)
	{
		involvedParts.add(part);
		return material.has(part) ? this.addItemInput(material.get(part).itemReference(), count, 1f) : this;
	}
	
	public MIMachineRecipeBuilder addPartInput(MaterialPart part, int count, float probability)
	{
		return this.addPartInput(defaultMaterial.orElseThrow(), part, count, probability);
	}
	
	public MIMachineRecipeBuilder addPartInput(MaterialPart part, int count)
	{
		return this.addPartInput(defaultMaterial.orElseThrow(), part, count);
	}
	
	public MIMachineRecipeBuilder addPartOutput(Material material, MaterialPart part, int count, float probability)
	{
		involvedParts.add(part);
		return material.has(part) ? this.addItemOutput(material.get(part).itemReference(), count, probability) : this;
	}
	
	public MIMachineRecipeBuilder addPartOutput(Material material, MaterialPart part, int count)
	{
		involvedParts.add(part);
		return material.has(part) ? this.addItemOutput(material.get(part).itemReference(), count, 1f) : this;
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
