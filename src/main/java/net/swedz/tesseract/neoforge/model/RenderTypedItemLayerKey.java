package net.swedz.tesseract.neoforge.model;

import com.mojang.math.Quadrant;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.cuboid.ItemModelGenerator;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.ExtraFaceData;
import org.joml.Vector3f;

/**
 * Functionally the same as {@link ItemModelGenerator.ItemLayerKey} however there is a field for a custom
 * {@link RenderType} included.
 */
public record RenderTypedItemLayerKey(
		Material.Baked material,
		ModelState modelState,
		int layerIndex,
		RenderType renderType,
		ExtraFaceData faceData
) implements ModelBaker.SharedOperationKey<QuadCollection>
{
	@Override
	public QuadCollection compute(ModelBaker modelBakery)
	{
		var builder = new QuadCollection.Builder();
		var sprite = material.sprite();
		var transparency = sprite.transparency();
		var materialInfo = new BakedQuad.MaterialInfo(
				sprite,
				ChunkSectionLayer.byTransparency(transparency),
				renderType,
				layerIndex,
				true,
				faceData.lightEmission(),
				faceData.ambientOcclusion()
		);
		bakeExtrudedSprite(
				builder,
				modelBakery.interner(),
				modelState,
				modelBakery.interner().materialInfo(materialInfo),
				faceData
		);
		return builder.build();
	}
	
	private static final CuboidFace.UVs SOUTH_FACE_UVS = new CuboidFace.UVs(0, 0, 16, 16);
	private static final CuboidFace.UVs NORTH_FACE_UVS = new CuboidFace.UVs(16, 0, 0, 16);
	
	/**
	 * For some reason when trying to access transform the original method in {@link ItemModelGenerator}, it fails
	 * insisting the target doesn't exist. I beg to differ.
	 *
	 * @see ItemModelGenerator#bakeExtrudedSprite(QuadCollection.Builder, ModelBaker.Interner, ModelState, BakedQuad.MaterialInfo, ExtraFaceData)
	 */
	private static void bakeExtrudedSprite(QuadCollection.Builder builder, ModelBaker.Interner interner, ModelState modelState, BakedQuad.MaterialInfo materialInfo, ExtraFaceData faceData)
	{
		var from = new Vector3f(0, 0, 7.5f);
		var to = new Vector3f(16, 16, 8.5f);
		builder.addUnculledFace(FaceBakery.bakeQuad(interner, from, to, SOUTH_FACE_UVS, Quadrant.R0, materialInfo, Direction.SOUTH, modelState, null, faceData));
		builder.addUnculledFace(FaceBakery.bakeQuad(interner, from, to, NORTH_FACE_UVS, Quadrant.R0, materialInfo, Direction.NORTH, modelState, null, faceData));
		ItemModelGenerator.bakeSideFaces(builder, interner, modelState, materialInfo, faceData);
	}
}
