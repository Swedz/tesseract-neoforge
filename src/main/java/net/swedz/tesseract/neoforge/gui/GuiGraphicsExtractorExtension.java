package net.swedz.tesseract.neoforge.gui;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public interface GuiGraphicsExtractorExtension
{
	default void blitLight(RenderPipeline pipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color, int packedLight)
	{
		throw new UnsupportedOperationException();
	}
	
	default void blitLight(RenderPipeline pipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int packedLight)
	{
		throw new UnsupportedOperationException();
	}
	
	default void blitLight(RenderPipeline pipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int srcWidth, int srcHeight, int textureWidth, int textureHeight, int packedLight)
	{
		throw new UnsupportedOperationException();
	}
	
	default void blitLight(Identifier texture, int x, int y, float u, float v, int width, int height, int srcWidth, int srcHeight, int textureWidth, int textureHeight, int color, int packedLight)
	{
		throw new UnsupportedOperationException();
	}
	
	default void blitLight(RenderPipeline pipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int srcWidth, int srcHeight, int textureWidth, int textureHeight, int color, int packedLight)
	{
		throw new UnsupportedOperationException();
	}
	
	default void blitLight(RenderPipeline pipeline, Identifier texture, int x0, int x1, int y0, int y1, float u0, float u1, float v0, float v1, int color, int packedLight)
	{
		throw new UnsupportedOperationException();
	}
	
	default void blitLight(RenderPipeline pipeline, GpuTextureView textureView, GpuSampler sampler, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color, int packedLight)
	{
		throw new UnsupportedOperationException();
	}
	
	default void blit(Identifier texture, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight, int color)
	{
		throw new UnsupportedOperationException();
	}
	
	default void blit(Identifier texture, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight, int color)
	{
		throw new UnsupportedOperationException();
	}
	
	default void blit(Identifier texture, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight)
	{
		throw new UnsupportedOperationException();
	}
	
	default void blit(RenderPipeline pipeline, GpuTextureView textureView, GpuSampler sampler, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color)
	{
		throw new UnsupportedOperationException();
	}
	
	default void item(ItemStack stack, ItemDisplayContext context, int x, int y)
	{
		throw new UnsupportedOperationException();
	}
	
	default void item(ItemStack stack, ItemDisplayContext context, int x, int y, int seed)
	{
		throw new UnsupportedOperationException();
	}
	
	default void item(LivingEntity owner, Level level, ItemStack stack, ItemDisplayContext context, int x, int y, int seed)
	{
		throw new UnsupportedOperationException();
	}
	
	default void tooltip(Font font, List<Component> tooltip, boolean dropShadow, Optional<TooltipComponent> component, ExtendedClientTooltipPositioner positioner, int xo, int yo, boolean replaceExisting, Identifier style)
	{
		throw new UnsupportedOperationException();
	}
}
