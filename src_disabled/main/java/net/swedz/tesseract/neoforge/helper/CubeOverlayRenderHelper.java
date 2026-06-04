package net.swedz.tesseract.neoforge.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterRenderBuffersEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.swedz.tesseract.neoforge.Tesseract;

import java.util.List;

@EventBusSubscriber(modid = Tesseract.ID, value = Dist.CLIENT)
public final class CubeOverlayRenderHelper
{
	private static final RenderType RENDER_TYPE = createRenderType("cube_overlay", Tesseract.id("textures/block/cube_overlay.png"));
	
	public static RenderType createRenderType(String name, Identifier texture)
	{
		return RenderType.create(
				name,
				RenderSetup.builder(RenderPipelines.CUTOUT_BLOCK)
						.withTexture("Sampler0", texture)
						.useLightmap()
						.createRenderSetup()
		);
	}
	
	public static final StandaloneModelKey<QuadCollection> MODEL_KEY = new StandaloneModelKey<>(() -> "cube_overlay");
	
	@SubscribeEvent
	private static void registerStandaloneModel(ModelEvent.RegisterStandalone event)
	{
		event.register(MODEL_KEY, SimpleUnbakedStandaloneModel.quadCollection(Tesseract.id("cube_overlay")));
	}
	
	@SubscribeEvent
	private static void registerRenderBuffers(RegisterRenderBuffersEvent event)
	{
		event.registerRenderBuffer(RENDER_TYPE);
	}
	
	private static QuadCollection getModel()
	{
		return Minecraft.getInstance().getModelManager().getStandaloneModel(MODEL_KEY);
	}
	
	private static List<BakedQuad> getQuadsForFace(Direction direction)
	{
		return getModel().getQuads(direction);
	}
	
	private static List<BakedQuad> getAllQuads()
	{
		return getModel().getAll();
	}
	
	private static QuadInstance buildQuadInstance(int color, int packedLight, int overlay)
	{
		var quadInstance = new QuadInstance();
		quadInstance.setColor(color);
		quadInstance.setLightCoords(packedLight);
		quadInstance.setOverlayCoords(overlay);
		return quadInstance;
	}
	
	public static void render(PoseStack matrices, VertexConsumer consumer, Direction direction, int color, int packedLight, int overlay)
	{
		var quadInstance = buildQuadInstance(color, packedLight, overlay);
		for(var quad : getQuadsForFace(direction))
		{
			consumer.putBakedQuad(matrices.last(), quad, quadInstance);
		}
	}
	
	public static void render(PoseStack matrices, VertexConsumer consumer, Direction direction, int color, int overlay)
	{
		render(matrices, consumer, direction, color, 0xFFFFFFFF, overlay);
	}
	
	public static void render(PoseStack matrices, MultiBufferSource bufferSource, int color, int packedLight, int overlay)
	{
		var consumer = bufferSource.getBuffer(RENDER_TYPE);
		var quadInstance = buildQuadInstance(color, packedLight, overlay);
		for(var quad : getAllQuads())
		{
			consumer.putBakedQuad(matrices.last(), quad, quadInstance);
		}
	}
	
	public static void render(PoseStack matrices, MultiBufferSource bufferSource, int color, int overlay)
	{
		render(matrices, bufferSource, color, 0xFFFFFFFF, overlay);
	}
}
