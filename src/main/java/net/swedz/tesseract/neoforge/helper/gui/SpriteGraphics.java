package net.swedz.tesseract.neoforge.helper.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiMetadataSection;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * <p>This is similar to what {@link GuiGraphicsExtractor} does to blit sprites to the screen. However, these methods
 * allow for blit-ing sprites to any arbitrary {@link PoseStack} and {@link SubmitNodeCollector}. In the past, I would
 * just hijack the <code>GuiGraphics</code> class by making a new instance with the {@link PoseStack} and
 * {@link MultiBufferSource} and rendering as needed onto that. Unfortunately now, as far as I can tell,
 * {@link GuiGraphicsExtractor} does not have a way to blit arbitrarily and instead always adds it to the
 * {@link GuiRenderState}. So, anything added to it, will always render flat onto the screen.</p>
 */
public final class SpriteGraphics
{
	public static GuiSpriteScaling getSpriteScaling(TextureAtlasSprite sprite)
	{
		return sprite.contents().getAdditionalMetadata(GuiMetadataSection.TYPE).orElse(GuiMetadataSection.DEFAULT).scaling();
	}
	
	public static TextureAtlasSprite getSprite(Identifier spriteLocation)
	{
		return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.GUI).getSprite(spriteLocation);
	}
	
	private static void blit(
			PoseStack pose,
			SubmitNodeCollector submit,
			TextureAtlasSprite sprite,
			int x0,
			int x1,
			int y0,
			int y1,
			float u0,
			float u1,
			float v0,
			float v1,
			float z,
			int packedLight,
			int color
	)
	{
		submit.submitCustomGeometry(
				pose,
				RenderTypes.text(sprite.atlasLocation()),
				(renderPose, renderVertices) ->
				{
					renderVertices.addVertex(renderPose, x0, y1, z).setColor(color).setUv(u0, v1).setLight(packedLight);
					renderVertices.addVertex(renderPose, x1, y1, z).setColor(color).setUv(u1, v1).setLight(packedLight);
					renderVertices.addVertex(renderPose, x1, y0, z).setColor(color).setUv(u1, v0).setLight(packedLight);
					renderVertices.addVertex(renderPose, x0, y0, z).setColor(color).setUv(u0, v0).setLight(packedLight);
				}
		);
	}
	
	private static void blit(
			PoseStack pose,
			SubmitNodeCollector submit,
			TextureAtlasSprite sprite,
			int spriteWidth,
			int spriteHeight,
			int textureX,
			int textureY,
			int x,
			int y,
			int width,
			int height,
			float z,
			int packedLight,
			int color
	)
	{
		if(width == 0 || height == 0)
		{
			return;
		}
		blit(
				pose,
				submit,
				sprite,
				x,
				x + width,
				y,
				y + height,
				sprite.getU((float) textureX / spriteWidth),
				sprite.getU((float) (textureX + width) / spriteWidth),
				sprite.getV((float) textureY / spriteHeight),
				sprite.getV((float) (textureY + height) / spriteHeight),
				z,
				packedLight,
				color
		);
	}
	
	private static void blitStretch(
			PoseStack pose,
			SubmitNodeCollector submit,
			TextureAtlasSprite sprite,
			int x,
			int y,
			int width,
			int height,
			float z,
			int packedLight,
			int color
	)
	{
		if(width == 0 || height == 0)
		{
			return;
		}
		blit(
				pose,
				submit,
				sprite,
				x,
				x + width,
				y,
				y + height,
				sprite.getU0(),
				sprite.getU1(),
				sprite.getV0(),
				sprite.getV1(),
				z,
				packedLight,
				color
		);
	}
	
	private static void blitTileAddVertices(
			PoseStack.Pose renderPose,
			VertexConsumer renderVertices,
			int tileWidth,
			int tileHeight,
			int x0,
			int y0,
			int x1,
			int y1,
			float u0,
			float u1,
			float v0,
			float v1,
			float z,
			int packedLight,
			int color
	)
	{
		int width = x1 - x0;
		int height = y1 - y0;
		
		for(int tileX = 0; tileX < width; tileX += tileWidth)
		{
			int remainingWidth = width - tileX;
			int innerTileWidth;
			float tileU1;
			if(tileWidth <= remainingWidth)
			{
				innerTileWidth = tileWidth;
				tileU1 = u1;
			}
			else
			{
				innerTileWidth = remainingWidth;
				tileU1 = Mth.lerp((float) remainingWidth / (float) tileWidth, u0, u1);
			}
			
			for(int tileY = 0; tileY < height; tileY += tileHeight)
			{
				int remainingHeight = height - tileY;
				int innerTileHeight;
				float tileV1;
				if(tileHeight <= remainingHeight)
				{
					innerTileHeight = tileHeight;
					tileV1 = v1;
				}
				else
				{
					innerTileHeight = remainingHeight;
					tileV1 = Mth.lerp((float) remainingHeight / (float) tileHeight, v0, v1);
				}
				
				int tileX0 = x0 + tileX;
				int tileX1 = x0 + tileX + innerTileWidth;
				int tileY0 = y0 + tileY;
				int tileY1 = y0 + tileY + innerTileHeight;
				renderVertices.addVertex(renderPose, tileX0, tileY0, z).setUv(u0, v0).setColor(color).setLight(packedLight);
				renderVertices.addVertex(renderPose, tileX0, tileY1, z).setUv(u0, tileV1).setColor(color).setLight(packedLight);
				renderVertices.addVertex(renderPose, tileX1, tileY1, z).setUv(tileU1, tileV1).setColor(color).setLight(packedLight);
				renderVertices.addVertex(renderPose, tileX1, tileY0, z).setUv(tileU1, v0).setColor(color).setLight(packedLight);
			}
		}
	}
	
	private static void blitTile(
			PoseStack pose,
			SubmitNodeCollector submit,
			TextureAtlasSprite sprite,
			int x,
			int y,
			int width,
			int height,
			int textureX,
			int textureY,
			int tileWidth,
			int tileHeight,
			int spriteWidth,
			int spriteHeight,
			float z,
			int packedLight,
			int color
	)
	{
		if(width == 0 || height == 0 ||
		   tileWidth == 0 || tileHeight == 0)
		{
			return;
		}
		var u0 = sprite.getU((float) textureX / spriteWidth);
		var u1 = sprite.getU((float) (textureX + tileWidth) / spriteWidth);
		var v0 = sprite.getV((float) textureY / spriteHeight);
		var v1 = sprite.getV((float) (textureY + tileHeight) / spriteHeight);
		submit.submitCustomGeometry(
				pose,
				RenderTypes.text(sprite.atlasLocation()),
				(renderPose, renderVertices) ->
						blitTileAddVertices(
								renderPose,
								renderVertices,
								tileWidth,
								tileHeight,
								x,
								y,
								x + width,
								y + height,
								u0,
								u1,
								v0,
								v1,
								z,
								packedLight,
								color
						)
		);
	}
	
	private static void blitNineSliceSegment(
			PoseStack pose,
			SubmitNodeCollector submit,
			TextureAtlasSprite sprite,
			GuiSpriteScaling.NineSlice nineSlice,
			int x,
			int y,
			int width,
			int height,
			int textureX,
			int textureY,
			int textureWidth,
			int textureHeight,
			int spriteWidth,
			int spriteHeight,
			float z,
			int packedLight,
			int color
	)
	{
		if(width == 0 || height == 0)
		{
			return;
		}
		if(nineSlice.stretchInner())
		{
			blit(
					pose,
					submit,
					sprite,
					x,
					x + width,
					y,
					y + height,
					sprite.getU((float) textureX / spriteWidth),
					sprite.getU((float) (textureX + textureWidth) / spriteWidth),
					sprite.getV((float) textureY / spriteHeight),
					sprite.getV((float) (textureY + textureHeight) / spriteHeight),
					z,
					packedLight,
					color
			);
		}
		else
		{
			blitTile(
					pose,
					submit,
					sprite,
					x,
					y,
					width,
					height,
					textureX,
					textureY,
					textureWidth,
					textureHeight,
					spriteWidth,
					spriteHeight,
					z,
					packedLight,
					color
			);
		}
	}
	
	private static void blitNineSlice(
			PoseStack pose,
			SubmitNodeCollector submit,
			TextureAtlasSprite sprite,
			GuiSpriteScaling.NineSlice nineSlice,
			int x,
			int y,
			int width,
			int height,
			float z,
			int packedLight,
			int color
	)
	{
		var border = nineSlice.border();
		var borderLeft = Math.min(border.left(), width / 2);
		var borderRight = Math.min(border.right(), width / 2);
		var borderTop = Math.min(border.top(), height / 2);
		var borderBottom = Math.min(border.bottom(), height / 2);
		if(width == nineSlice.width() && height == nineSlice.height())
		{
			blit(
					pose,
					submit,
					sprite,
					nineSlice.width(),
					nineSlice.height(),
					0,
					0,
					x,
					y,
					width,
					height,
					z,
					packedLight,
					color
			);
		}
		else if(height == nineSlice.height())
		{
			blit(
					pose,
					submit,
					sprite,
					nineSlice.width(),
					nineSlice.height(),
					0,
					0,
					x,
					y,
					borderLeft,
					height,
					z,
					packedLight,
					color
			);
			blitNineSliceSegment(
					pose,
					submit,
					sprite,
					nineSlice,
					x + borderLeft,
					y,
					width - borderRight - borderLeft,
					height,
					borderLeft,
					0,
					nineSlice.width() - borderRight - borderLeft,
					nineSlice.height(),
					nineSlice.width(),
					nineSlice.height(),
					z,
					packedLight,
					color
			);
			blit(
					pose,
					submit,
					sprite,
					nineSlice.width(),
					nineSlice.height(),
					nineSlice.width() - borderRight,
					0,
					x + width - borderRight,
					y,
					borderRight,
					height,
					z,
					packedLight,
					color
			);
		}
		else if(width == nineSlice.width())
		{
			blit(
					pose,
					submit,
					sprite,
					nineSlice.width(),
					nineSlice.height(),
					0,
					0,
					x,
					y,
					width,
					borderTop,
					z,
					packedLight,
					color
			);
			blitNineSliceSegment(
					pose,
					submit,
					sprite,
					nineSlice,
					x,
					y + borderTop,
					width,
					height - borderBottom - borderTop,
					0,
					borderTop,
					nineSlice.width(),
					nineSlice.height() - borderBottom - borderTop,
					nineSlice.width(),
					nineSlice.height(),
					z,
					packedLight,
					color
			);
			blit(
					pose,
					submit,
					sprite,
					nineSlice.width(),
					nineSlice.height(),
					0,
					nineSlice.height() - borderBottom,
					x,
					y + height - borderBottom,
					width,
					borderBottom,
					z,
					packedLight,
					color
			);
		}
		else
		{
			blit(
					pose,
					submit,
					sprite,
					nineSlice.width(),
					nineSlice.height(),
					0,
					0,
					x,
					y,
					borderLeft,
					borderTop,
					z,
					packedLight,
					color
			);
			blitNineSliceSegment(
					pose,
					submit,
					sprite,
					nineSlice,
					x + borderLeft,
					y,
					width - borderRight - borderLeft,
					borderTop,
					borderLeft,
					0,
					nineSlice.width() - borderRight - borderLeft,
					borderTop,
					nineSlice.width(),
					nineSlice.height(),
					z,
					packedLight,
					color
			);
			blit(
					pose,
					submit,
					sprite,
					nineSlice.width(),
					nineSlice.height(),
					nineSlice.width() - borderRight,
					0,
					x + width - borderRight,
					y,
					borderRight,
					borderTop,
					z,
					packedLight,
					color
			);
			blit(
					pose,
					submit,
					sprite,
					nineSlice.width(),
					nineSlice.height(),
					0,
					nineSlice.height() - borderBottom,
					x,
					y + height - borderBottom,
					borderLeft,
					borderBottom,
					z,
					packedLight,
					color
			);
			blitNineSliceSegment(
					pose,
					submit,
					sprite,
					nineSlice,
					x + borderLeft,
					y + height - borderBottom,
					width - borderRight - borderLeft,
					borderBottom,
					borderLeft,
					nineSlice.height() - borderBottom,
					nineSlice.width() - borderRight - borderLeft,
					borderBottom,
					nineSlice.width(),
					nineSlice.height(),
					z,
					packedLight,
					color
			);
			blit(
					pose,
					submit,
					sprite,
					nineSlice.width(),
					nineSlice.height(),
					nineSlice.width() - borderRight,
					nineSlice.height() - borderBottom,
					x + width - borderRight,
					y + height - borderBottom,
					borderRight,
					borderBottom,
					z,
					packedLight,
					color
			);
			blitNineSliceSegment(
					pose,
					submit,
					sprite,
					nineSlice,
					x,
					y + borderTop,
					borderLeft,
					height - borderBottom - borderTop,
					0,
					borderTop,
					borderLeft,
					nineSlice.height() - borderBottom - borderTop,
					nineSlice.width(),
					nineSlice.height(),
					z,
					packedLight,
					color
			);
			blitNineSliceSegment(
					pose,
					submit,
					sprite,
					nineSlice,
					x + borderLeft,
					y + borderTop,
					width - borderRight - borderLeft,
					height - borderBottom - borderTop,
					borderLeft,
					borderTop,
					nineSlice.width() - borderRight - borderLeft,
					nineSlice.height() - borderBottom - borderTop,
					nineSlice.width(),
					nineSlice.height(),
					z,
					packedLight,
					color
			);
			blitNineSliceSegment(
					pose,
					submit,
					sprite,
					nineSlice,
					x + width - borderRight,
					y + borderTop,
					borderRight,
					height - borderBottom - borderTop,
					nineSlice.width() - borderRight,
					borderTop,
					borderRight,
					nineSlice.height() - borderBottom - borderTop,
					nineSlice.width(),
					nineSlice.height(),
					z,
					packedLight,
					color
			);
		}
	}
	
	public static void blit(
			PoseStack pose,
			SubmitNodeCollector submit,
			Identifier spriteLocation,
			int x,
			int y,
			int width,
			int height,
			float z,
			int packedLight,
			int color
	)
	{
		var sprite = getSprite(spriteLocation);
		switch(getSpriteScaling(sprite))
		{
			case GuiSpriteScaling.Stretch stretch -> blitStretch(pose, submit, sprite, x, y, width, height, z, packedLight, color);
			case GuiSpriteScaling.Tile tile -> blitTile(pose, submit, sprite, x, y, width, height, 0, 0, tile.width(), tile.height(), tile.width(), tile.height(), z, packedLight, color);
			case GuiSpriteScaling.NineSlice nineSlice -> blitNineSlice(pose, submit, sprite, nineSlice, x, y, width, height, z, packedLight, color);
			default ->
			{
			}
		}
	}
}
