package net.swedz.tesseract.neoforge.helper;

import net.minecraft.world.item.DyeColor;

public final class ColorHelper
{
	/**
	 * Get the vibrant color for a {@link DyeColor}. This is different from the built-in {@link DyeColor#getTextColor()} because it actually matches the color of the dye texture itself.
	 * <br><br>
	 * As a failsafe if {@link DyeColor} ever becomes an extensible enum (at the time of writing this documentation it is not but I wouldn't be surprised if it got added), {@link DyeColor#getTextColor()} is used for any non-vanilla colors.
	 *
	 * @param dyeColor the dye color
	 * @return the vibrant color as an integer
	 */
	public static int getVibrantColor(DyeColor dyeColor)
	{
		return 0xFF000000 | switch (dyeColor)
		{
			case WHITE -> 0xFFFFFF;
			case ORANGE -> 0xFF7F00;
			case MAGENTA -> 0xFF00FF;
			case LIGHT_BLUE -> 0x00FFFF;
			case YELLOW -> 0xFFFF00;
			case LIME -> 0x00FF00;
			case PINK -> 0xFFB4FF;
			case GRAY -> 0x555555;
			case LIGHT_GRAY -> 0xAAAAAA;
			case CYAN -> 0x00AAAA;
			case PURPLE -> 0xAA00AA;
			case BLUE -> 0x0000FF;
			case BROWN -> 0x8B4513;
			case GREEN -> 0x00AA00;
			case RED -> 0xFF0000;
			case BLACK -> 0x000000;
			default -> dyeColor.getTextColor() & 0x00FFFFFF;
		};
	}
}
