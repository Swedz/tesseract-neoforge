package net.swedz.tesseract.neoforge.compat.mi.material.property;

import aztech.modern_industrialization.MI;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;

public interface MIMaterialProperties
{
	MaterialProperty<Long> BATTERY_CAPACITY = create("battery_capacity", 0L);
	
	static <T> MaterialProperty<T> create(String id, T defaultValue)
	{
		return new MaterialProperty<>(MI.id(id), defaultValue);
	}
}
