package net.swedz.tesseract.neoforge.mixin;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.swedz.tesseract.neoforge.helper.datagen.BlockModelGeneratorsByIdentifier;
import net.swedz.tesseract.neoforge.helper.datagen.BlockStateGeneratorCollectorByIdentifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(BlockModelGenerators.class)
@Implements(@Interface(iface = BlockModelGeneratorsByIdentifier.class, prefix = "tesseractapi$"))
public abstract class BlockModelGeneratorsByIdentifierMixin
{
	@Shadow
	@Final
	public Consumer<BlockModelDefinitionGenerator> blockStateOutput;
	
	public BlockStateGeneratorCollectorByIdentifier tesseractapi$blockStateOutputById()
	{
		return (BlockStateGeneratorCollectorByIdentifier) blockStateOutput;
	}
}
