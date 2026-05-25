package net.swedz.tesseract.neoforge.helper.gui;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

public final class ExtraGuiGraphics
{
	public static void nineSlice(
			GuiGraphicsExtractor graphics,
			RenderPipeline pipeline,
			Identifier texture,
			int color,
			int screenX,
			int screenY,
			int screenWidth,
			int screenHeight,
			int textureWidth,
			int textureHeight,
			int border
	)
	{
		int texCenterWidth = textureWidth - (border * 2);
		int texCenterHeight = textureHeight - (border * 2);
		
		// Corners
		graphics.blit(pipeline, texture, screenX, screenY, 0, 0, border, border, border, border, textureWidth, textureHeight, color);
		graphics.blit(pipeline, texture, screenX + screenWidth - border, screenY, textureWidth - border, 0, border, border, border, border, textureWidth, textureHeight, color);
		graphics.blit(pipeline, texture, screenX, screenY + screenHeight - border, 0, textureHeight - border, border, border, border, border, textureWidth, textureHeight, color);
		graphics.blit(pipeline, texture, screenX + screenWidth - border, screenY + screenHeight - border, textureWidth - border, textureHeight - border, border, border, border, border, textureWidth, textureHeight, color);
		/*this.blit(screenX, screenY, 0, 0, border, border, textureWidth, textureHeight);
		this.blit(screenX + screenWidth - border, screenY, textureWidth - border, 0, border, border, textureWidth, textureHeight);
		this.blit(screenX, screenY + screenHeight - border, 0, textureHeight - border, border, border, textureWidth, textureHeight);
		this.blit(screenX + screenWidth - border, screenY + screenHeight - border, textureWidth - border, textureHeight - border, border, border, textureWidth, textureHeight);*/
		
		// Edges
		for(int i = 0; i <= (screenWidth / texCenterWidth); i++)
		{
			int x = screenX + border + (i * texCenterWidth);
			int width = Math.min(texCenterWidth, screenWidth - (i * texCenterWidth) - (border * 2));
			graphics.blit(pipeline, texture, x, screenY, border, 0, width, border, width, border, textureWidth, textureHeight, color);
			graphics.blit(pipeline, texture, x, screenY + screenHeight - border, border, textureHeight - border, width, border, width, border, textureWidth, textureHeight, color);
			/*this.blit(x, screenY, border, 0, width, border, textureWidth, textureHeight);
			this.blit(x, screenY + screenHeight - border, border, textureHeight - border, width, border, textureWidth, textureHeight);*/
		}
		for(int i = 0; i <= (screenHeight / texCenterHeight); i++)
		{
			int y = screenY + border + (i * texCenterHeight);
			int height = Math.min(texCenterHeight, screenHeight - (i * texCenterHeight) - (border * 2));
			graphics.blit(pipeline, texture, screenX, y, 0, border, border, height, border, height, textureWidth, textureHeight, color);
			graphics.blit(pipeline, texture, screenX + screenWidth - border, y, textureWidth - border, border, border, height, border, height, textureWidth, textureHeight, color);
			/*this.blit(screenX, y, 0, border, border, height, textureWidth, textureHeight);
			this.blit(screenX + screenWidth - border, y, textureWidth - border, border, border, height, textureWidth, textureHeight);*/
		}
		
		// Center
		int centerWidth = (screenWidth - (border * 2)) / texCenterWidth;
		int centerHeight = (screenHeight - (border * 2)) / texCenterHeight;
		for(int ix = 0; ix <= centerWidth; ix++)
		{
			for(int iy = 0; iy <= centerHeight; iy++)
			{
				int x = screenX + border + (ix * texCenterWidth);
				int y = screenY + border + (iy * texCenterHeight);
				int width = Math.min(texCenterWidth, screenWidth - (ix * texCenterWidth) - (border * 2));
				int height = Math.min(texCenterHeight, screenHeight - (iy * texCenterHeight) - (border * 2));
				graphics.blit(pipeline, texture, x, y, border, border, width, height, width, height, textureWidth, textureHeight, color);
				//this.blit(x, y, border, border, width, height, textureWidth, textureHeight);
			}
		}
	}
	
	public static void nineSlice(
			GuiGraphicsExtractor graphics,
			RenderPipeline pipeline,
			Identifier texture,
			int screenX,
			int screenY,
			int screenWidth,
			int screenHeight,
			int textureWidth,
			int textureHeight,
			int border
	)
	{
		nineSlice(
				graphics,
				pipeline,
				texture,
				-1,
				screenX,
				screenY,
				screenWidth,
				screenHeight,
				textureWidth,
				textureHeight,
				border
		);
	}
	
	public static void nineSlice(
			GuiGraphicsExtractor graphics,
			Identifier texture,
			int color,
			int screenX,
			int screenY,
			int screenWidth,
			int screenHeight,
			int textureWidth,
			int textureHeight,
			int border
	)
	{
		nineSlice(
				graphics,
				RenderPipelines.GUI,
				texture,
				color,
				screenX,
				screenY,
				screenWidth,
				screenHeight,
				textureWidth,
				textureHeight,
				border
		);
	}
	
	public static void nineSlice(
			GuiGraphicsExtractor graphics,
			Identifier texture,
			int screenX,
			int screenY,
			int screenWidth,
			int screenHeight,
			int textureWidth,
			int textureHeight,
			int border
	)
	{
		nineSlice(
				graphics,
				texture,
				-1,
				screenX,
				screenY,
				screenWidth,
				screenHeight,
				textureWidth,
				textureHeight,
				border
		);
	}
	
	public static void centeredText(
			GuiGraphicsExtractor graphics,
			Font font,
			FormattedCharSequence text,
			int x,
			int y,
			int color,
			boolean dropShadow
	)
	{
		graphics.text(
				font,
				text,
				x - font.width(text),
				y,
				color,
				dropShadow
		);
	}
	
	public static void centeredText(
			GuiGraphicsExtractor graphics,
			Font font,
			Component text,
			int x,
			int y,
			int color,
			boolean dropShadow
	)
	{
		centeredText(
				graphics,
				font,
				text.getVisualOrderText(),
				x,
				y,
				color,
				dropShadow
		);
	}
}
