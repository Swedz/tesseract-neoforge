package net.swedz.tesseract.neoforge.compat.mi.recipe;

import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MIRecipeJson;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.MachineRecipeBuilder;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.swedz.tesseract.neoforge.recipe.RecipeOfferable;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapedRecipeBuilder;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapelessRecipeBuilder;

import java.lang.reflect.Field;
import java.util.Map;

public class MIMachineRecipeBuilderWrapper implements RecipeOfferable
{
	protected final MachineRecipeBuilder internal;
	
	public MIMachineRecipeBuilderWrapper(MachineRecipeBuilder internal)
	{
		this.internal = internal;
	}
	
	public static MIMachineRecipeBuilderWrapper fromShaped(ShapedRecipeBuilder shaped, MachineRecipeType machine, int eu, int duration, int division)
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
		
		MachineRecipeBuilder machineRecipe = new MachineRecipeBuilder(machine, eu, duration).addItemOutput(result.getItem(), result.getCount() / division);
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
		
		return new MIMachineRecipeBuilderWrapper(machineRecipe);
	}
	
	public static MIMachineRecipeBuilderWrapper fromShapedToAssembler(ShapedRecipeBuilder shaped)
	{
		return fromShaped(shaped, MIMachineRecipeTypes.ASSEMBLER, 8, 200, 1);
	}
	
	public static MIMachineRecipeBuilderWrapper fromShapeless(ShapelessRecipeBuilder shaped, MachineRecipeType machine, int eu, int duration, int division)
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
		
		MachineRecipeBuilder machineRecipe = new MachineRecipeBuilder(machine, eu, duration).addItemOutput(result.getItem(), result.getCount() / division);
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
		
		return new MIMachineRecipeBuilderWrapper(machineRecipe);
	}
	
	public static MIMachineRecipeBuilderWrapper fromShapelessToPacker(ShapelessRecipeBuilder shapeless)
	{
		return fromShapeless(shapeless, MIMachineRecipeTypes.PACKER, 2, 5 * 20, 1);
	}
	
	public static MIMachineRecipeBuilderWrapper fromShapelessToUnpackerAndFlip(ShapelessRecipeBuilder shapeless)
	{
		return fromShapeless(shapeless, MIMachineRecipeTypes.UNPACKER, 2, 5 * 20, 1).flip();
	}
	
	public MachineRecipeBuilder internal()
	{
		return internal;
	}
	
	public MachineRecipe internalRecipe()
	{
		try
		{
			Field fieldRecipe = MIRecipeJson.class.getDeclaredField("recipe");
			fieldRecipe.setAccessible(true);
			MachineRecipe actualRecipe = (MachineRecipe) fieldRecipe.get(internal);
			fieldRecipe.setAccessible(false);
			return actualRecipe;
		}
		catch (NoSuchFieldException | IllegalAccessException ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	public MIMachineRecipeBuilderWrapper flip()
	{
		MachineRecipe actualRecipe = this.internalRecipe();
		
		MachineRecipeBuilder inversedRecipe = new MachineRecipeBuilder((MachineRecipeType) actualRecipe.getType(), actualRecipe.eu, actualRecipe.duration);
		
		for(MachineRecipe.ItemInput itemInput : actualRecipe.itemInputs)
		{
			inversedRecipe.addItemOutput(itemInput.ingredient().getItems()[0].getItem(), itemInput.amount(), itemInput.probability());
		}
		
		for(MachineRecipe.FluidInput fluidInput : actualRecipe.fluidInputs)
		{
			inversedRecipe.addFluidOutput(fluidInput.fluid(), (int) fluidInput.amount(), fluidInput.probability());
		}
		
		for(MachineRecipe.ItemOutput itemOutput : actualRecipe.itemOutputs)
		{
			inversedRecipe.addItemInput(itemOutput.variant().getItem(), itemOutput.amount(), itemOutput.probability());
		}
		
		for(MachineRecipe.FluidOutput fluidOutput : actualRecipe.fluidOutputs)
		{
			inversedRecipe.addFluidInput(fluidOutput.fluid(), (int) fluidOutput.amount(), fluidOutput.probability());
		}
		
		return new MIMachineRecipeBuilderWrapper(inversedRecipe);
	}
	
	@Override
	public void validate()
	{
	}
	
	@Override
	public Recipe<?> convert()
	{
		return this.internalRecipe();
	}
}
