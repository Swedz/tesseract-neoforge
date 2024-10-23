package net.swedz.tesseract.neoforge.compat.mi.material.property;

import net.swedz.tesseract.neoforge.material.property.MaterialProperty;

public interface MIMaterialProperties
{
	MaterialProperty<Long> BATTERY_CAPACITY = new MaterialProperty<>("battery_capacity", 0L);
}
