package net.swedz.tesseract.neoforge.registry.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.function.Consumer;

public final class CommonModelBuilders
{
	public static Consumer<ItemModelBuilder> generated(ItemHolder item, String texture)
	{
		return (builder) -> builder
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0", ResourceLocation.fromNamespaceAndPath(item.identifier().modId(), "item/" + texture));
	}
	
	public static Consumer<ItemModelBuilder> generated(ItemHolder item)
	{
		return generated(item, item.identifier().id());
	}
	
	public static Consumer<ItemModelBuilder> generatedOverlayed(ItemHolder item, String texture)
	{
		return (builder) -> builder
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0", ResourceLocation.fromNamespaceAndPath(item.identifier().modId(), "item/" + texture))
				.texture("layer1", ResourceLocation.fromNamespaceAndPath(item.identifier().modId(), "item/" + texture + "_overlay"));
	}
	
	public static Consumer<ItemModelBuilder> generatedOverlayed(ItemHolder item)
	{
		return generatedOverlayed(item, item.identifier().id());
	}
	
	public static Consumer<ItemModelBuilder> handheld(ItemHolder item, String texture)
	{
		return (builder) -> builder
				.parent(new ModelFile.UncheckedModelFile("item/handheld"))
				.texture("layer0", ResourceLocation.fromNamespaceAndPath(item.identifier().modId(), "item/" + texture));
	}
	
	public static Consumer<ItemModelBuilder> handheld(ItemHolder item)
	{
		return handheld(item, item.identifier().id());
	}
	
	public static Consumer<ItemModelBuilder> handheldOverlayed(ItemHolder item, String texture)
	{
		return (builder) -> builder
				.parent(new ModelFile.UncheckedModelFile("item/handheld"))
				.texture("layer0", ResourceLocation.fromNamespaceAndPath(item.identifier().modId(), "item/" + texture))
				.texture("layer1", ResourceLocation.fromNamespaceAndPath(item.identifier().modId(), "item/" + texture + "_overlay"));
	}
	
	public static Consumer<ItemModelBuilder> handheldOverlayed(ItemHolder item)
	{
		return handheldOverlayed(item, item.identifier().id());
	}
	
	public static Consumer<ItemModelBuilder> itemBlockEntity(ItemHolder item)
	{
		return (builder) ->
		{
			builder.parent(new ModelFile.UncheckedModelFile("builtin/entity"));
			ModelBuilder.TransformsBuilder transforms = builder.transforms();
			transforms.transform(ItemDisplayContext.GUI)
					.rotation(30, 225, 0)
					.translation(0, 0, 0)
					.scale(0.625f, 0.625f, 0.625f);
			transforms.transform(ItemDisplayContext.GROUND)
					.rotation(0, 0, 0)
					.translation(0, 3, 0)
					.scale(0.25f, 0.25f, 0.25f);
			transforms.transform(ItemDisplayContext.FIXED)
					.rotation(0, 0, 0)
					.translation(0, 0, 0)
					.scale(0.5f, 0.5f, 0.5f);
			transforms.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
					.rotation(75, 45, 0)
					.translation(0, 2.5f, 0)
					.scale(0.375f, 0.375f, 0.375f);
			transforms.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
					.rotation(0, 45, 0)
					.translation(0, 0, 0)
					.scale(0.4f, 0.4f, 0.4f);
			transforms.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
					.rotation(0, 225, 0)
					.translation(0, 0, 0)
					.scale(0.4f, 0.4f, 0.4f);
		};
	}
	
	public static Consumer<ItemModelBuilder> block(ItemHolder item)
	{
		return (builder) -> builder
				.parent(new ModelFile.UncheckedModelFile("%s:block/%s".formatted(item.identifier().modId(), item.identifier().id())));
	}
	
	public static Consumer<BlockStateProvider> blockstateOnly(BlockHolder block)
	{
		return (builder) -> builder
				.simpleBlock(block.get(), builder.models().getExistingFile(builder.modLoc("block/%s".formatted(block.identifier().id()))));
	}
	
	public static Consumer<BlockStateProvider> blockCubeAll(BlockHolder block)
	{
		return (builder) -> builder
				.simpleBlockWithItem(block.get(), builder.cubeAll(block.get()));
	}
	
	public static Consumer<BlockStateProvider> blockTopEnd(BlockHolder block)
	{
		return (builder) -> builder.simpleBlockWithItem(
				block.get(),
				builder.models().cubeColumn(
						block.identifier().id(),
						ResourceLocation.fromNamespaceAndPath(block.identifier().modId(), "block/%s_side".formatted(block.identifier().id())),
						ResourceLocation.fromNamespaceAndPath(block.identifier().modId(), "block/%s_end".formatted(block.identifier().id()))
				)
		);
	}
}
