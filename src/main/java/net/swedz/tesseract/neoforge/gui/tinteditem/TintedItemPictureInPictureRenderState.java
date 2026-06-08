package net.swedz.tesseract.neoforge.gui.tinteditem;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.joml.Matrix3x2f;

public record TintedItemPictureInPictureRenderState(
		GuiItemRenderState state,
		int x0,
		int y0,
		int x1,
		int y1,
		int color
) implements PictureInPictureRenderState
{
	@Override
	public float scale()
	{
		return 16;
	}
	
	@Override
	public Matrix3x2f pose()
	{
		return state.pose();
	}
	
	@Override
	public ScreenRectangle scissorArea()
	{
		return state.scissorArea();
	}
	
	@Override
	public ScreenRectangle bounds()
	{
		return state.bounds();
	}
}