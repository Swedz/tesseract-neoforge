package net.swedz.tesseract.neoforge.helper.gui;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import org.joml.Matrix3x2f;

public record BlitWithLightRenderState(
		RenderPipeline pipeline,
		TextureSetup textureSetup,
		Matrix3x2f pose,
		int x0,
		int y0,
		int x1,
		int y1,
		float u0,
		float u1,
		float v0,
		float v1,
		int color,
		int packedLight,
		ScreenRectangle scissorArea,
		ScreenRectangle bounds
) implements GuiElementRenderState
{
	public BlitWithLightRenderState(
			RenderPipeline pipeline,
			TextureSetup textureSetup,
			Matrix3x2f pose,
			int x0,
			int y0,
			int x1,
			int y1,
			float u0,
			float u1,
			float v0,
			float v1,
			int color,
			int packedLight,
			ScreenRectangle scissorArea
	)
	{
		this(
				pipeline,
				textureSetup,
				pose,
				x0,
				y0,
				x1,
				y1,
				u0,
				u1,
				v0,
				v1,
				color,
				packedLight,
				scissorArea,
				BlitRenderState.getBounds(x0, y0, x1, y1, pose, scissorArea)
		);
	}
	
	@Override
	public void buildVertices(VertexConsumer vertexConsumer)
	{
		vertexConsumer.addVertexWith2DPose(this.pose(), x0, y0).setUv(u0, v0).setColor(color).setLight(packedLight);
		vertexConsumer.addVertexWith2DPose(this.pose(), x0, y1).setUv(u0, v1).setColor(color).setLight(packedLight);
		vertexConsumer.addVertexWith2DPose(this.pose(), x1, y1).setUv(u1, v1).setColor(color).setLight(packedLight);
		vertexConsumer.addVertexWith2DPose(this.pose(), x1, y0).setUv(u1, v0).setColor(color).setLight(packedLight);
	}
}