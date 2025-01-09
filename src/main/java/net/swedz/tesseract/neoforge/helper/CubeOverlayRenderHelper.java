package net.swedz.tesseract.neoforge.helper;

import aztech.modern_industrialization.thirdparty.fabricrendering.QuadBuffer;
import aztech.modern_industrialization.thirdparty.fabricrendering.QuadEmitter;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRenderBuffersEvent;
import net.swedz.tesseract.neoforge.Tesseract;

import static aztech.modern_industrialization.thirdparty.fabricrendering.MutableQuadView.*;
import static net.minecraft.client.renderer.LightTexture.*;
import static net.minecraft.client.renderer.RenderStateShard.*;
import static net.minecraft.world.inventory.InventoryMenu.*;

@EventBusSubscriber(modid = Tesseract.ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CubeOverlayRenderHelper
{
	public static final RenderType CUBE_OVERLAY_CUTOUT = RenderType.create(
			"cube_overlay_cutout",
			DefaultVertexFormat.BLOCK,
			VertexFormat.Mode.QUADS,
			65536, false, false,
			RenderType.CompositeState.builder()
					.setTransparencyState(NO_TRANSPARENCY)
					.setTextureState(new RenderStateShard.TextureStateShard(BLOCK_ATLAS, false, false))
					.setLightmapState(LIGHTMAP)
					.setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)
					.createCompositeState(false)
	);
	
	public static final ResourceLocation TEXTURE = Tesseract.id("block/cube_overlay");
	
	@SubscribeEvent
	private static void registerRenderBuffers(RegisterRenderBuffersEvent event)
	{
		event.registerRenderBuffer(CUBE_OVERLAY_CUTOUT);
	}
	
	private static OverlayQuads QUADS;
	
	private record OverlayQuads(BakedQuad[] quads, TextureAtlasSprite sprite)
	{
	}
	
	private static BakedQuad[] buildQuads(TextureAtlasSprite sprite)
	{
		var overlayQuads = new BakedQuad[6];
		QuadEmitter emitter = new QuadBuffer();
		for(Direction direction : Direction.values())
		{
			emitter.emit();
			emitter.square(direction, 0, 0, 1, 1, 0);
			emitter.spriteBake(sprite, BAKE_LOCK_UV);
			overlayQuads[direction.get3DDataValue()] = emitter.toBakedQuad(sprite);
		}
		return overlayQuads;
	}
	
	public static void render(PoseStack matrices, MultiBufferSource bufferSource, float red, float green, float blue, float alpha, int packedLight, int overlay)
	{
		var sprite = Minecraft.getInstance().getTextureAtlas(BLOCK_ATLAS).apply(TEXTURE);
		if(QUADS == null || QUADS.sprite() != sprite)
		{
			QUADS = new OverlayQuads(buildQuads(sprite), sprite);
		}
		VertexConsumer consumer = bufferSource.getBuffer(CUBE_OVERLAY_CUTOUT);
		for(BakedQuad overlayQuad : QUADS.quads())
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
