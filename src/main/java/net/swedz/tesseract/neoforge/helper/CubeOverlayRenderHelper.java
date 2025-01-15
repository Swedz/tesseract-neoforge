package net.swedz.tesseract.neoforge.helper;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRenderBuffersEvent;
import net.swedz.tesseract.neoforge.Tesseract;

import java.util.function.Supplier;

import static net.minecraft.client.renderer.LightTexture.*;
import static net.minecraft.client.renderer.RenderStateShard.*;

@EventBusSubscriber(modid = Tesseract.ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CubeOverlayRenderHelper
{
	public static final RenderType CUBE_OVERLAY_CUTOUT = RenderType.create(
			"cube_overlay_cutout",
			DefaultVertexFormat.BLOCK,
			VertexFormat.Mode.QUADS,
			65536, false, false,
			RenderType.CompositeState.builder()
					.setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)
					.setTransparencyState(NO_TRANSPARENCY)
					.setTextureState(new RenderStateShard.TextureStateShard(Tesseract.id("textures/block/cube_overlay.png"), false, false))
					.setLightmapState(LIGHTMAP)
					.createCompositeState(false)
	);
	
	@SubscribeEvent
	private static void registerRenderBuffers(RegisterRenderBuffersEvent event)
	{
		event.registerRenderBuffer(CUBE_OVERLAY_CUTOUT);
	}
	
	private static Supplier<BakedQuad[]> QUADS = Suppliers.memoize(() ->
	{
		BakedQuad[] quads = new BakedQuad[6];
		int index = 0;
		for(Direction direction : Direction.values())
		{
			quads[index++] = createQuadForFace(direction);
		}
		return quads;
	});
	
	private static BakedQuad createQuadForFace(Direction direction)
	{
		float[][] positions = switch (direction)
		{
			case UP -> new float[][]{{0, 1, 0}, {0, 1, 1}, {1, 1, 1}, {1, 1, 0}};
			case DOWN -> new float[][]{{0, 0, 0}, {1, 0, 0}, {1, 0, 1}, {0, 0, 1}};
			case NORTH -> new float[][]{{0, 0, 0}, {0, 1, 0}, {1, 1, 0}, {1, 0, 0}};
			case SOUTH -> new float[][]{{0, 0, 1}, {1, 0, 1}, {1, 1, 1}, {0, 1, 1}};
			case WEST -> new float[][]{{0, 0, 0}, {0, 0, 1}, {0, 1, 1}, {0, 1, 0}};
			case EAST -> new float[][]{{1, 0, 0}, {1, 1, 0}, {1, 1, 1}, {1, 0, 1}};
		};
		return new BakedQuad(createVertexData(positions, direction), 0, direction, null, false);
	}
	
	private static int[] createVertexData(float[][] positions, Direction direction)
	{
		int[] vertexData = new int[positions.length * 8];
		for(int i = 0; i < positions.length; i++)
		{
			float x = positions[i][0];
			float y = positions[i][1];
			float z = positions[i][2];
			
			float u = switch (direction.getAxis())
			{
				case X -> z;
				case Y, Z -> x;
			};
			float v = switch (direction.getAxis())
			{
				case X, Z -> y;
				case Y -> z;
			};
			
			int index = i * 8;
			vertexData[index] = Float.floatToRawIntBits(x);
			vertexData[index + 1] = Float.floatToRawIntBits(y);
			vertexData[index + 2] = Float.floatToRawIntBits(z);
			vertexData[index + 3] = -1; // Color
			vertexData[index + 4] = Float.floatToRawIntBits(u);
			vertexData[index + 5] = Float.floatToRawIntBits(v);
			vertexData[index + 6] = 0; // Lightmap
			vertexData[index + 7] = 0; // Overlay
		}
		return vertexData;
	}
	
	public static void render(PoseStack matrices, MultiBufferSource bufferSource, float red, float green, float blue, float alpha, int packedLight, int overlay)
	{
		VertexConsumer consumer = bufferSource.getBuffer(CUBE_OVERLAY_CUTOUT);
		for(BakedQuad overlayQuad : QUADS.get())
		{
			consumer.putBulkData(matrices.last(), overlayQuad, red, green, blue, alpha, packedLight, overlay);
		}
	}
	
	public static void render(PoseStack matrices, MultiBufferSource bufferSource, float red, float green, float blue, float alpha, int overlay)
	{
		render(matrices, bufferSource, red, green, blue, alpha, FULL_BRIGHT, overlay);
	}
	
	public static void render(PoseStack matrices, MultiBufferSource bufferSource, float red, float green, float blue, int overlay)
	{
		render(matrices, bufferSource, red, green, blue, 1f, FULL_BRIGHT, overlay);
	}
}
