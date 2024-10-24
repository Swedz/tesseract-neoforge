package net.swedz.tesseract.neoforge.compat.mi.material.recipe;

import aztech.modern_industrialization.MIFluids;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.data.recipes.RecipeOutput;
import net.swedz.tesseract.neoforge.compat.mi.recipe.MIMachineRecipeBuilder;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.recipe.MaterialRecipeContext;

import java.util.function.Consumer;

import static aztech.modern_industrialization.machines.init.MIMachineRecipeTypes.*;
import static net.swedz.tesseract.neoforge.compat.mi.material.part.MIMaterialParts.*;
import static net.swedz.tesseract.neoforge.compat.mi.material.property.MIMaterialProperties.*;
import static net.swedz.tesseract.neoforge.material.builtin.part.MaterialParts.*;

public class MIMachineMaterialRecipeContext extends MaterialRecipeContext
{
	public MIMachineMaterialRecipeContext(MaterialRegistry registry, Material material, RecipeOutput recipes)
	{
		super(registry, material, recipes);
	}
	
	public MIMachineMaterialRecipeContext machine(String id, MachineRecipeType type, int eu, int duration, MaterialPart output, int outputCount, Consumer<MIMachineRecipeBuilder> builder)
	{
		MIMachineRecipeBuilder recipe = new MIMachineRecipeBuilder(type, eu, duration)
				.forMaterial(material);
		while(outputCount > 64)
		{
			recipe.addPartOutput(output, 64);
			outputCount -= 64;
		}
		if(outputCount > 0)
		{
			recipe.addPartOutput(output, outputCount);
		}
		builder.accept(recipe);
		if(this.has(recipe.involvedParts().toArray(new MaterialPart[0])))
		{
			recipe.offerTo(recipes, this.id("materials/%s/%s/%s".formatted(material.id().getPath(), type.getPath(), id)));
		}
		return this;
	}
	
	public MIMachineMaterialRecipeContext machine(MachineRecipeType type, int eu, int duration, MaterialPart output, int outputCount, Consumer<MIMachineRecipeBuilder> builder)
	{
		return this.machine(output.id().getPath(), type, eu, duration, output, outputCount, builder);
	}
	
	public MIMachineMaterialRecipeContext machine(String id, MachineRecipeType type, MaterialPart output, int outputCount, Consumer<MIMachineRecipeBuilder> builder)
	{
		return this.machine(id, type, 2, (int) (200 * this.get(TIME_FACTOR)), output, outputCount, builder);
	}
	
	public MIMachineMaterialRecipeContext machine(String id, MachineRecipeType type, MaterialPart input, int inputCount, MaterialPart output, int outputCount)
	{
		return this.machine(id, type, output, outputCount, (b) -> b.addPartInput(input, inputCount));
	}
	
	public MIMachineMaterialRecipeContext machine(MachineRecipeType type, MaterialPart input, int inputCount, MaterialPart output, int outputCount)
	{
		return this.machine(input.id().getPath(), type, output, outputCount, (b) -> b.addPartInput(input, inputCount));
	}
	
	public MIMachineMaterialRecipeContext machine(MachineRecipeType type, MaterialPart output, int outputCount, Consumer<MIMachineRecipeBuilder> builder)
	{
		return this.machine(output.id().getPath(), type, output, outputCount, builder);
	}
	
	public MIMachineMaterialRecipeContext packAndUnpack(MaterialPart small, int smallCount, MaterialPart big, int bigCount)
	{
		return this
				.machine(big.id().getPath(), PACKER, big, bigCount, (b) -> b.addPartInput(small, smallCount))
				.machine(small.id().getPath(), UNPACKER, small, smallCount, (b) -> b.addPartInput(big, bigCount));
	}
	
	public MIMachineMaterialRecipeContext maceratorRecycling(MaterialPart part, int tinyDust)
	{
		MaterialPart output = TINY_DUST;
		int outputCount = tinyDust;
		if(outputCount % 9 == 0)
		{
			output = DUST;
			outputCount /= 9;
		}
		return this.machine(part.id().getPath(), MACERATOR, output, outputCount, (b) -> b.addPartInput(part, 1));
	}
	
	public MIMachineMaterialRecipeContext cuttingMachine(String id, MaterialPart input, int inputCount, MaterialPart output, int outputCount)
	{
		return this.machine(id, CUTTING_MACHINE, output, outputCount, (b) -> b.addPartInput(input, inputCount).addFluidInput(MIFluids.LUBRICANT, 1));
	}
	
	public MIMachineMaterialRecipeContext cuttingMachine(MaterialPart input, int inputCount, MaterialPart output, int outputCount)
	{
		return this.cuttingMachine(input.id().getPath(), input, inputCount, output, outputCount);
	}
}
