package net.swedz.tesseract.neoforge.registry.common;

import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.CompositeModel;
import net.minecraft.client.renderer.item.SpecialModelWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.swedz.tesseract.neoforge.model.ModelGenerators;
import net.swedz.tesseract.neoforge.model.UseBlockEntityRenderer;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.List;
import java.util.function.Consumer;

public final class CommonModelBuilders
{
	public static Consumer<ModelGenerators> generated(ItemHolder item, String texture)
	{
		return (generators) ->
				ModelTemplates.FLAT_ITEM.create(
						item.asItem(),
						new TextureMapping()
								.put(TextureSlot.LAYER0, Identifier.fromNamespaceAndPath(item.identifier().modId(), "item/" + texture)),
						generators.item().modelOutput
				);
	}
	
	public static Consumer<ModelGenerators> generated(ItemHolder item)
	{
		return generated(item, item.identifier().id());
	}
	
	public static Consumer<ModelGenerators> generatedOverlayed(ItemHolder item, String texture)
	{
		return (generators) ->
				ModelTemplates.FLAT_ITEM.create(
						item.asItem(),
						new TextureMapping()
								.put(TextureSlot.LAYER0, Identifier.fromNamespaceAndPath(item.identifier().modId(), "item/" + texture))
								.put(TextureSlot.LAYER1, Identifier.fromNamespaceAndPath(item.identifier().modId(), "item/" + texture + "_overlay")),
						generators.item().modelOutput
				);
	}
	
	public static Consumer<ModelGenerators> generatedOverlayed(ItemHolder item)
	{
		return generatedOverlayed(item, item.identifier().id());
	}
	
	public static Consumer<ModelGenerators> handheld(ItemHolder item, String texture)
	{
		return (generators) ->
				ModelTemplates.FLAT_HANDHELD_ITEM.create(
						item.asItem(),
						new TextureMapping()
								.put(TextureSlot.LAYER0, Identifier.fromNamespaceAndPath(item.identifier().modId(), "item/" + texture)),
						generators.item().modelOutput
				);
	}
	
	public static Consumer<ModelGenerators> handheld(ItemHolder item)
	{
		return handheld(item, item.identifier().id());
	}
	
	public static Consumer<ModelGenerators> handheldOverlayed(ItemHolder item, String texture)
	{
		return (generators) ->
				ModelTemplates.FLAT_HANDHELD_ITEM.create(
						item.asItem(),
						new TextureMapping()
								.put(TextureSlot.LAYER0, Identifier.fromNamespaceAndPath(item.identifier().modId(), "item/" + texture))
								.put(TextureSlot.LAYER1, Identifier.fromNamespaceAndPath(item.identifier().modId(), "item/" + texture + "_overlay")),
						generators.item().modelOutput
				);
	}
	
	public static Consumer<ModelGenerators> handheldOverlayed(ItemHolder item)
	{
		return handheldOverlayed(item, item.identifier().id());
	}
	
	public static Consumer<ModelGenerators> itemBlockEntity(ItemHolder item)
	{
		return (generators) ->
		{
			var baseBlockModel = BuiltInRegistries.ITEM.getKey(item.asItem()).withPrefix("block/");
			generators.item().itemModelOutput.accept(
					item.asItem(),
					new CompositeModel.Unbaked(List.of(
							new BlockModelWrapper.Unbaked(baseBlockModel, List.of()),
							new SpecialModelWrapper.Unbaked(baseBlockModel, new UseBlockEntityRenderer.Unbaked())
					))
			);
		};
		// TODO 26.1
		/*return (builder) ->
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
		};*/
	}
	
	public static Consumer<ModelGenerators> block(ItemHolder item)
	{
		return (generators) -> {};
		/*return (builder) -> builder
				.parent(new ModelFile.UncheckedModelFile("%s:block/%s".formatted(item.identifier().modId(), item.identifier().id())));*/
	}
	
	public static Consumer<ModelGenerators> blockstateOnly(BlockHolder block)
	{
		return (builder) -> builder
				.simpleBlock(block.get(), builder.models().getExistingFile(builder.modLoc("block/%s".formatted(block.identifier().id()))));
	}
	
	public static Consumer<ModelGenerators> blockCubeAll(BlockHolder block)
	{
		return (builder) -> builder
				.simpleBlockWithItem(block.get(), builder.cubeAll(block.get()));
	}
	
	public static Consumer<ModelGenerators> blockTopEnd(BlockHolder block)
	{
		return (builder) -> builder.simpleBlockWithItem(
				block.get(),
				builder.models().cubeColumn(
						block.identifier().id(),
						Identifier.fromNamespaceAndPath(block.identifier().modId(), "block/%s_side".formatted(block.identifier().id())),
						Identifier.fromNamespaceAndPath(block.identifier().modId(), "block/%s_end".formatted(block.identifier().id()))
				)
		);
	}
}
