package net.swedz.tesseract.neoforge.compat.mi.material.property;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.api.energy.CableTier;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;

public interface MIMaterialProperties
{
	MaterialProperty<Double> TIME_FACTOR = create("time_factor", 1D);
	
	MaterialProperty<Long> BATTERY_CAPACITY = create("battery_capacity", 0L);
	
	MaterialProperty<CableTier> CABLE_TIER = create("cable_tier", CableTier.LV);
	
	static <T> MaterialProperty<T> create(String id, T defaultValue)
	{
		return new MaterialProperty<>(MI.id(id), defaultValue);
	}
}
