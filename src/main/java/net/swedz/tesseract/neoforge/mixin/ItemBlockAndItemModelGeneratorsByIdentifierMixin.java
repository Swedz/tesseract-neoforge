package net.swedz.tesseract.neoforge.mixin;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.resources.Identifier;
import net.swedz.tesseract.neoforge.helper.datagen.BlockAndItemModelGeneratorsByIdentifier;
import net.swedz.tesseract.neoforge.helper.datagen.SimpleModelCollectorByIdentifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.BiConsumer;

@Mixin(ItemModelGenerators.class)
@Implements(@Interface(iface = BlockAndItemModelGeneratorsByIdentifier.class, prefix = "tesseractapi$"))
public abstract class ItemBlockAndItemModelGeneratorsByIdentifierMixin
{
	@Shadow
	@Final
	public BiConsumer<Identifier, ModelInstance> modelOutput;
	
	public SimpleModelCollectorByIdentifier tesseractapi$modelOutputById()
	{
		return (SimpleModelCollectorByIdentifier) modelOutput;
	}
}
