package net.swedz.tesseract.neoforge.gui.tinteditem;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public final class TintedItemPictureInPictureRenderer extends PictureInPictureRenderer<TintedItemPictureInPictureRenderState>
{
	private Object modelOnTextureIdentity;
	
	public TintedItemPictureInPictureRenderer(MultiBufferSource.BufferSource bufferSource)
	{
		super(bufferSource);
	}
	
	@Override
	public Class<TintedItemPictureInPictureRenderState> getRenderStateClass()
	{
		return TintedItemPictureInPictureRenderState.class;
	}
	
	@Override
	protected void renderToTexture(TintedItemPictureInPictureRenderState state, PoseStack pose)
	{
		pose.scale(1, -1, -1);
		
		var guiItemRenderState = state.state();
		var slotCenterX = (float) guiItemRenderState.x();
		var slotCenterY = (float) guiItemRenderState.y();
		pose.translate(slotCenterX / 16f, slotCenterY / 16f, 0);
		var itemStackRenderState = guiItemRenderState.itemStackRenderState();
		var flat = !itemStackRenderState.usesBlockLight();
		if(flat)
		{
			Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
		}
		else
		{
			Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
		}
		
		var featureRenderDispatcher = Minecraft.getInstance().gameRenderer.getFeatureRenderDispatcher();
		var submitNodeStorage = featureRenderDispatcher.getSubmitNodeStorage();
		itemStackRenderState.submit(pose, submitNodeStorage, 0xFFFFFFFF, OverlayTexture.NO_OVERLAY, 0);
		featureRenderDispatcher.renderAllFeatures();
		modelOnTextureIdentity = itemStackRenderState.getModelIdentity();
	}
	
	@Override
	public boolean textureIsReadyToBlit(TintedItemPictureInPictureRenderState state)
	{
		var itemStackRenderState = state.state().itemStackRenderState();
		return !itemStackRenderState.isAnimated() &&
			   itemStackRenderState.getModelIdentity().equals(modelOnTextureIdentity);
	}
	
	@Override
	public void blitTexture(TintedItemPictureInPictureRenderState state, GuiRenderState guiState)
	{
		guiState.addBlitToCurrentLayer(
				new BlitRenderState(
						RenderPipelines.GUI_TEXTURED,
						TextureSetup.singleTexture(
								textureView,
								RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)
						),
						state.pose(),
						state.x0(),
						state.y0(),
						state.x1(),
						state.y1(),
						0,
						1,
						1,
						0,
						state.color(),
						state.scissorArea(),
						null
				)
		);
	}
	
	@Override
	protected float getTranslateY(int height, int guiScale)
	{
		return (float) height / 2f;
	}
	
	@Override
	protected String getTextureLabel()
	{
		return "tinted_oversized_item";
	}
}
