package net.swedz.tesseract.neoforge.helper.guigraphics;

import net.minecraft.client.renderer.LightTexture;

public interface PackedLightGuiGraphics
{
	void setPackedLight(Integer packedLight);
	
	default void resetPackedLight()
	{
		this.setPackedLight(null);
	}
	
	Integer getPackedLight();
	
	default int getPackedLightOrFull()
	{
		var packedLight = this.getPackedLight();
		return packedLight == null ? LightTexture.FULL_BRIGHT : packedLight;
	}
}
