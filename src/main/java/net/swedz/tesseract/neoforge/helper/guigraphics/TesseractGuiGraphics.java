package net.swedz.tesseract.neoforge.helper.guigraphics;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.swedz.tesseract.neoforge.api.Assert;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class TesseractGuiGraphics implements BlitGuiGraphics, FillGuiGraphics, StringGuiGraphics, TooltipGuiGraphics, ItemGuiGraphics, PackedLightGuiGraphics
{
	private final TesseractGuiGraphics parent;
	private final GuiGraphics          internal;
	
	private boolean             batching;
	private List<Integer>       batchOrder     = Lists.newArrayList();
	private Map<Integer, Batch> batches        = Maps.newHashMap();
	private List<Runnable>      delayedRenders = Lists.newArrayList();
	
	private record Batch(ResourceLocation[] textures, TextureShaderConfiguration shader, List<DrawAction> draws)
	{
	}
	
	private int[] color     = new int[]{255, 255, 255, 255};
	private int[] lastColor = new int[]{255, 255, 255, 255};
	
	private TextureShaderConfiguration textureShader = TextureShaderConfiguration.DEFAULT;
	private ResourceLocation[]         textures      = new ResourceLocation[]{MissingTextureAtlasSprite.getLocation()};
	private List<DrawAction>           textureBatch;
	
	private Integer packedLight;
	
	private Font font = Minecraft.getInstance().font;
	
	private boolean textDropShadow = true;
	
	private boolean           tooltipFirstLinePadded   = true;
	private BackgroundPadding tooltipBackgroundPadding = new BackgroundPadding();
	
	private TesseractGuiGraphics(TesseractGuiGraphics parent, GuiGraphics internal)
	{
		this.parent = parent;
		this.internal = internal;
	}
	
	public TesseractGuiGraphics(GuiGraphics internal)
	{
		this(null, internal);
	}
	
	/**
	 * Returns the vanilla {@link GuiGraphics} instance wrapped by this instance.
	 *
	 * @return the vanilla {@link GuiGraphics}
	 * @deprecated Try to avoid using this as much as possible. If there is something needed in {@link GuiGraphics} not
	 * supported by this wrapper, add that support instead!
	 */
	@Deprecated
	@Override
	public GuiGraphics internal()
	{
		return internal;
	}
	
	@Override
	public int guiWidth()
	{
		return internal.guiWidth();
	}
	
	@Override
	public int guiHeight()
	{
		return internal.guiHeight();
	}
	
	public PoseStack pose()
	{
		return internal.pose();
	}
	
	public MultiBufferSource.BufferSource bufferSource()
	{
		return internal.bufferSource();
	}
	
	public void reset()
	{
		this.resetColor();
		this.resetTextureShader();
		this.resetTooltipBackgroundPadding();
	}
	
	public TesseractGuiGraphics inner()
	{
		return new TesseractGuiGraphics(this, internal);
	}
	
	public TesseractGuiGraphics end()
	{
		Assert.notNull(parent, "Cannot close outermost graphic instance");
		if(batching)
		{
			parent.delayed(this::drawBatches);
		}
		return parent;
	}
	
	public void enableBatching()
	{
		batching = true;
	}
	
	private void disableBatching()
	{
		batching = false;
		batchOrder = Lists.newArrayList();
		batches = Maps.newHashMap();
		delayedRenders = Lists.newArrayList();
	}
	
	public void drawBatches()
	{
		Assert.that(batching, "Batching has not been enabled");
		
		// Set batching to false so that any delayed renders that contain batching conditionals do not try to add to a batch that no longer exists
		batching = false;
		
		for(var texture : batchOrder)
		{
			var batchInstance = batches.get(texture);
			var draws = batchInstance.draws();
			if(!draws.isEmpty())
			{
				var shader = batchInstance.shader();
				var batch = GuiGraphicsBatch.start(internal, batchInstance.textures(), shader.shader(), shader.mode(), shader.format(), shader.extraSetup());
				for(var draw : draws)
				{
					draw.addVertexes(batch);
				}
				batch.end();
			}
		}
		
		for(var render : delayedRenders)
		{
			render.run();
		}
		
		this.disableBatching();
	}
	
	@Override
	public int[] getColor()
	{
		return color;
	}
	
	@Override
	public void setColorInt(int red, int green, int blue, int alpha)
	{
		lastColor = color;
		color = new int[]{red, green, blue, alpha};
	}
	
	@Override
	public void revertColor()
	{
		color = lastColor;
		lastColor = new int[]{255, 255, 255, 255};
	}
	
	@Override
	public TextureShaderConfiguration getTextureShader()
	{
		return textureShader;
	}
	
	@Override
	public void setTextureShader(TextureShaderConfiguration textureShader)
	{
		if(textureShader == null)
		{
			textureShader = TextureShaderConfiguration.DEFAULT;
		}
		this.textureShader = textureShader;
	}
	
	@Override
	public ResourceLocation[] getTextures()
	{
		return textures;
	}
	
	@Override
	public void setTextures(ResourceLocation... textures)
	{
		if(textures == null)
		{
			textures = new ResourceLocation[]{MissingTextureAtlasSprite.getLocation()};
		}
		else
		{
			for(int i = 0; i < textures.length; i++)
			{
				if(textures[i] == null)
				{
					textures[i] = MissingTextureAtlasSprite.getLocation();
				}
			}
		}
		this.textures = textures;
		textureBatch = null;
	}
	
	private void addToTextureBatch(DrawAction draw)
	{
		Assert.that(batching, "Batching has not been enabled");
		
		if(textureBatch == null)
		{
			int textureIndex = Arrays.hashCode(textures);
			var batchInstance = batches.get(textureIndex);
			List<DrawAction> draws;
			if(batchInstance == null)
			{
				draws = Lists.newArrayList();
				batchOrder.add(textureIndex);
				batches.put(textureIndex, new Batch(textures, textureShader, draws));
			}
			else
			{
				draws = batchInstance.draws();
			}
			textureBatch = draws;
		}
		
		textureBatch.add(draw);
	}
	
	@Override
	public void setPackedLight(Integer packedLight)
	{
		this.packedLight = packedLight;
	}
	
	@Override
	public Integer getPackedLight()
	{
		return packedLight;
	}
	
	@Override
	public Font getFont()
	{
		return font;
	}
	
	@Override
	public void setFont(Font font)
	{
		if(font == null)
		{
			font = Minecraft.getInstance().font;
		}
		this.font = font;
	}
	
	@Override
	public boolean isStringDropShadow()
	{
		return textDropShadow;
	}
	
	@Override
	public void setStringDropShadow(boolean textDropShadow)
	{
		this.textDropShadow = textDropShadow;
	}
	
	@Override
	public boolean isTooltipFirstLinePadded()
	{
		return tooltipFirstLinePadded;
	}
	
	@Override
	public void setTooltipFirstLinePadded(boolean padded)
	{
		tooltipFirstLinePadded = padded;
	}
	
	@Override
	public BackgroundPadding getTooltipBackgroundPadding()
	{
		return tooltipBackgroundPadding;
	}
	
	@Override
	public void setTooltipBackgroundPadding(BackgroundPadding padding)
	{
		tooltipBackgroundPadding = padding;
	}
	
	public void delayed(Runnable runnable)
	{
		if(batching)
		{
			delayedRenders.add(runnable);
		}
		else
		{
			runnable.run();
		}
	}
	
	private void addVertex(Matrix4f pose, VertexConsumer buffer, float x, float y, float z, Float u, Float v, int red, int green, int blue, int alpha, Integer packedLight)
	{
		var vertex = buffer.addVertex(pose, x, y, z).setColor(red, green, blue, alpha);
		if(u != null && v != null)
		{
			vertex.setUv(u, v);
		}
		if(packedLight != null)
		{
			vertex.setLight(packedLight);
		}
	}
	
	@ApiStatus.Internal
	@Override
	public void innerBlit(int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV)
	{
		int red = color[0];
		int green = color[1];
		int blue = color[2];
		int alpha = color[3];
		var packedLight = this.getPackedLight();
		
		final DrawAction draw = (pose, buffer) ->
		{
			this.addVertex(pose, buffer, (float) x1, (float) y1, (float) blitOffset, minU, minV, red, green, blue, alpha, packedLight);
			this.addVertex(pose, buffer, (float) x1, (float) y2, (float) blitOffset, minU, maxV, red, green, blue, alpha, packedLight);
			this.addVertex(pose, buffer, (float) x2, (float) y2, (float) blitOffset, maxU, maxV, red, green, blue, alpha, packedLight);
			this.addVertex(pose, buffer, (float) x2, (float) y1, (float) blitOffset, maxU, minV, red, green, blue, alpha, packedLight);
		};
		
		if(batching)
		{
			this.addToTextureBatch(draw);
		}
		else
		{
			var batch = GuiGraphicsBatch.start(internal, textures, textureShader.shader(), textureShader.mode(), textureShader.format(), textureShader.extraSetup());
			draw.addVertexes(batch);
			batch.end();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void internalFill(RenderType renderType, int minX, int minY, int maxX, int maxY, int z, int color, Integer packedLight)
	{
		var pose = this.pose().last().pose();
		if(minX < maxX)
		{
			int flip = minX;
			minX = maxX;
			maxX = flip;
		}
		
		if(minY < maxY)
		{
			int flip = minY;
			minY = maxY;
			maxY = flip;
		}
		
		int red = FastColor.ARGB32.red(color);
		int green = FastColor.ARGB32.green(color);
		int blue = FastColor.ARGB32.blue(color);
		int alpha = FastColor.ARGB32.alpha(color);
		
		var buffer = this.bufferSource().getBuffer(renderType);
		this.addVertex(pose, buffer, minX, minY, z, null, null, red, green, blue, alpha, packedLight);
		this.addVertex(pose, buffer, minX, maxY, z, null, null, red, green, blue, alpha, packedLight);
		this.addVertex(pose, buffer, maxX, maxY, z, null, null, red, green, blue, alpha, packedLight);
		this.addVertex(pose, buffer, maxX, minY, z, null, null, red, green, blue, alpha, packedLight);
		internal.flushIfUnmanaged();
	}
	
	@Override
	public void fill(RenderType renderType, int minX, int minY, int maxX, int maxY, int z)
	{
		var color = this.getColorARGB();
		var packedLight = this.getPackedLight();
		this.delayed(() -> this.internalFill(renderType, minX, minY, maxX, maxY, z, color, packedLight));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int drawString(FormattedCharSequence text, float x, float y)
	{
		int result = font.drawInBatch(text, x, y, this.getColorARGB(), this.isStringDropShadow(), this.pose().last().pose(), internal.bufferSource(), Font.DisplayMode.NORMAL, 0, this.getPackedLightOrFull());
		internal.flushIfUnmanaged();
		return result;
	}
}
