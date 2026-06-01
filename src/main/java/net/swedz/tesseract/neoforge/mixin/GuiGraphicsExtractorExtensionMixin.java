package net.swedz.tesseract.neoforge.mixin;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.ClientHooks;
import net.swedz.tesseract.neoforge.gui.ClientTextTooltipDropShadow;
import net.swedz.tesseract.neoforge.gui.ExtendedClientTooltipPositioner;
import net.swedz.tesseract.neoforge.gui.GuiGraphicsExtractorExtension;
import net.swedz.tesseract.neoforge.helper.gui.BlitWithLightRenderState;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Optional;

@Mixin(GuiGraphicsExtractor.class)
@Implements(@Interface(iface = GuiGraphicsExtractorExtension.class, prefix = "tesseractapi$"))
public abstract class GuiGraphicsExtractorExtensionMixin
{
	@Final
	@Shadow
	private GuiRenderState guiRenderState;
	
	@Final
	@Shadow
	private Matrix3x2fStack pose;
	
	@Shadow
	public abstract ScreenRectangle peekScissorStack();
	
	@Shadow
	public abstract void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight);
	
	@Shadow
	public abstract void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color);
	
	@Shadow
	protected abstract void setTooltipForNextFrameInternal(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, boolean replaceExisting);
	
	@Shadow
	private @Nullable Runnable deferredTooltip;
	
	@Shadow
	private ItemStack tooltipStack;
	
	@Shadow
	protected abstract void innerBlit(RenderPipeline pipeline, GpuTextureView textureView, GpuSampler sampler, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color);
	
	@Shadow
	public abstract void text(Font font, FormattedCharSequence str, int x, int y, int color, boolean dropShadow);
	
	public void tesseractapi$blitLight(RenderPipeline pipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color, int packedLight)
	{
		this.tesseractapi$blitLight(pipeline, texture, x, y, u, v, width, height, width, height, textureWidth, textureHeight, color, packedLight);
	}
	
	public void tesseractapi$blitLight(RenderPipeline pipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int packedLight)
	{
		this.tesseractapi$blitLight(pipeline, texture, x, y, u, v, width, height, width, height, textureWidth, textureHeight, packedLight);
	}
	
	public void tesseractapi$blitLight(RenderPipeline pipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int srcWidth, int srcHeight, int textureWidth, int textureHeight, int packedLight)
	{
		this.tesseractapi$blitLight(pipeline, texture, x, y, u, v, width, height, srcWidth, srcHeight, textureWidth, textureHeight, -1, packedLight);
	}
	
	public void tesseractapi$blitLight(Identifier texture, int x, int y, float u, float v, int width, int height, int srcWidth, int srcHeight, int textureWidth, int textureHeight, int color, int packedLight)
	{
		this.tesseractapi$blitLight(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, srcWidth, srcHeight, textureWidth, textureHeight, color, packedLight);
	}
	
	public void tesseractapi$blitLight(RenderPipeline pipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int srcWidth, int srcHeight, int textureWidth, int textureHeight, int color, int packedLight)
	{
		this.tesseractapi$blitLight(pipeline, texture, x, x + width, y, y + height, u / (float) textureWidth, (u + (float) srcWidth) / (float) textureWidth, v / (float) textureHeight, (v + (float) srcHeight) / (float) textureHeight, color, packedLight);
	}
	
	public void tesseractapi$blitLight(RenderPipeline pipeline, Identifier texture, int x0, int x1, int y0, int y1, float u0, float u1, float v0, float v1, int color, int packedLight)
	{
		var textureInstance = Minecraft.getInstance().getTextureManager().getTexture(texture);
		this.tesseractapi$blitLight(pipeline, textureInstance.getTextureView(), textureInstance.getSampler(), x0, y0, x1, y1, u0, u1, v0, v1, color, packedLight);
	}
	
	public void tesseractapi$blitLight(RenderPipeline pipeline, GpuTextureView textureView, GpuSampler sampler, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color, int packedLight)
	{
		guiRenderState.addGuiElement(new BlitWithLightRenderState(pipeline, TextureSetup.singleTexture(textureView, sampler), new Matrix3x2f(pose), x0, y0, x1, y1, u0, u1, v0, v1, color, packedLight, this.peekScissorStack()));
	}
	
	public void tesseractapi$blit(Identifier texture, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight, int color)
	{
		this.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight, color);
	}
	
	public void tesseractapi$blit(Identifier texture, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight, int color)
	{
		this.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, uOffset, vOffset, uWidth, vHeight, 256, 256, color);
	}
	
	public void tesseractapi$blit(Identifier texture, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight)
	{
		this.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, uOffset, vOffset, uWidth, vHeight, 256, 256);
	}
	
	public void tesseractapi$blit(RenderPipeline pipeline, GpuTextureView textureView, GpuSampler sampler, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color)
	{
		this.innerBlit(pipeline, textureView, sampler, x0, y0, x1, y1, u0, u1, v0, v1, color);
	}
	
	public void tesseractapi$item(ItemStack stack, ItemDisplayContext context, int x, int y)
	{
		this.tesseractapi$item(stack, context, x, y, 0);
	}
	
	public void tesseractapi$item(ItemStack stack, ItemDisplayContext context, int x, int y, int seed)
	{
		this.tesseractapi$item(null, null, stack, context, x, y, seed);
	}
	
	public void tesseractapi$item(LivingEntity owner, Level level, ItemStack stack, ItemDisplayContext context, int x, int y, int seed)
	{
		if(!stack.isEmpty())
		{
			TrackingItemStackRenderState itemStackRenderState = new TrackingItemStackRenderState();
			Minecraft.getInstance().getItemModelResolver().updateForTopItem(itemStackRenderState, stack, context, level, owner, seed);
			
			try
			{
				guiRenderState.addItem(new GuiItemRenderState(new Matrix3x2f(pose), itemStackRenderState, x, y, this.peekScissorStack()));
			}
			catch(Throwable ex)
			{
				CrashReport report = CrashReport.forThrowable(ex, "Rendering item");
				CrashReportCategory category = report.addCategory("Item being rendered");
				category.setDetail("Item Type", () -> String.valueOf(stack.getItem()));
				category.setDetail("Item Components", () -> String.valueOf(stack.getComponents()));
				category.setDetail("Item Foil", () -> String.valueOf(stack.hasFoil()));
				throw new ReportedException(report);
			}
		}
	}
	
	public void tesseractapi$centeredText(Font font, FormattedCharSequence text, int x, int y, int color, boolean dropShadow)
	{
		this.text(font, text, x - font.width(text) / 2, y, color, dropShadow);
	}
	
	public void tesseractapi$centeredText(Font font, Component text, int x, int y, int color, boolean dropShadow)
	{
		this.tesseractapi$centeredText(font, text.getVisualOrderText(), x, y, color, dropShadow);
	}
	
	public void tesseractapi$tooltip(Font font, List<Component> tooltip, boolean dropShadow, Optional<TooltipComponent> component, ExtendedClientTooltipPositioner positioner, int xo, int yo, int maxWidth, boolean replaceExisting, Identifier style)
	{
		List<ClientTooltipComponent> components = Lists.newArrayList();
		for(var line : tooltip)
		{
			if(line.equals(Component.empty()))
			{
				components.add(new ClientTextTooltipDropShadow(line.getVisualOrderText(), dropShadow));
				continue;
			}
			var splitLine = maxWidth > 0 ? font.split(line, maxWidth) : List.of(line.getVisualOrderText());
			for(var splitLineLine : splitLine)
			{
				components.add(new ClientTextTooltipDropShadow(splitLineLine, dropShadow));
			}
		}
		component.ifPresent((tooltipComponent) -> components.add(components.isEmpty() ? 0 : 1, ClientTooltipComponent.create(tooltipComponent)));
		this.setTooltipForNextFrameInternal(font, components, xo, yo, positioner, style, replaceExisting);
	}
	
	public void tesseractapi$tooltip(Font font, List<Component> tooltip, boolean dropShadow, Optional<TooltipComponent> component, ExtendedClientTooltipPositioner positioner, int xo, int yo, boolean replaceExisting, Identifier style)
	{
		this.tesseractapi$tooltip(font, tooltip, dropShadow, component, positioner, xo, yo, -1, replaceExisting, style);
	}
	
	@Unique
	private void setTooltipForNextFrameInternal(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ExtendedClientTooltipPositioner positioner, Identifier style, boolean replaceExisting)
	{
		if(!lines.isEmpty() &&
		   (this.deferredTooltip == null || replaceExisting))
		{
			var capturedTooltipStack = this.tooltipStack;
			this.deferredTooltip = () -> this.tooltip(font, lines, xo, yo, positioner, style, capturedTooltipStack);
		}
	}
	
	@Unique
	private void tooltip(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ExtendedClientTooltipPositioner positioner, Identifier style, ItemStack tooltipStack)
	{
		var graphics = (GuiGraphicsExtractor) (Object) this;
		
		var preEvent = ClientHooks.onRenderTooltipPre(tooltipStack, graphics, xo, yo, graphics.guiWidth(), graphics.guiHeight(), lines, font, positioner.toVanillaPositioner());
		if(!preEvent.isCanceled())
		{
			font = preEvent.getFont();
			xo = preEvent.getX();
			yo = preEvent.getY();
			
			int textWidth = 0;
			int textHeight = lines.size() == 1 ? -2 : 0;
			for(var line : lines)
			{
				int lineWidth = line.getWidth(font);
				if(lineWidth > textWidth)
				{
					textWidth = lineWidth;
				}
				textHeight += line.getHeight(font);
			}
			
			var positionedTooltip = positioner.positionTooltip(graphics.guiWidth(), graphics.guiHeight(), xo, yo, textWidth, textHeight);
			int x = positionedTooltip.x();
			int y = positionedTooltip.y();
			pose.pushMatrix();
			var textureEvent = ClientHooks.onRenderTooltipTexture(tooltipStack, graphics, x, y, preEvent.getFont(), lines, style);
			TooltipRenderUtil.extractTooltipBackground(graphics, x - positionedTooltip.paddingLeft(), y - positionedTooltip.paddingTop(), textWidth + positionedTooltip.paddingRight(), textHeight + positionedTooltip.paddingBottom(), textureEvent.getTexture());
			int localY = y;
			
			for(int index = 0; index < lines.size(); ++index)
			{
				var line = lines.get(index);
				line.extractText(graphics, font, x, localY);
				localY += line.getHeight(font) + (index == 0 ? 2 : 0);
			}
			
			localY = y;
			
			for(int index = 0; index < lines.size(); ++index)
			{
				var line = lines.get(index);
				line.extractImage(font, x, localY, textWidth, textHeight, graphics);
				localY += line.getHeight(font) + (index == 0 ? 2 : 0);
			}
			
			pose.popMatrix();
		}
	}
}
