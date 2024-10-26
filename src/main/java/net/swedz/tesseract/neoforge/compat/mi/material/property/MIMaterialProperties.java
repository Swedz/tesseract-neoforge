package net.swedz.tesseract.neoforge.compat.mi.material.property;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.api.energy.CableTier;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;

public interface MIMaterialProperties
{
	MaterialProperty<Double> TIME_FACTOR = create("time_factor", TimeFactor.AVERAGE);
	
	interface TimeFactor
	{
		double SOFT      = 0.5;
		double AVERAGE   = 1;
		double HARD      = 2;
		double VERY_HARD = 4;
	}
	
	MaterialProperty<Long> BATTERY_CAPACITY = create("battery_capacity", 0L);
	
	static long batteryCapacity(CableTier tier)
	{
		return tier.getMaxTransfer() * 60 * 20;
	}
	
	MaterialProperty<CableTier> CABLE_TIER = create("cable_tier", CableTier.LV);
	
	static <T> MaterialProperty<T> create(String id, T defaultValue)
	{
		return new MaterialProperty<>(MI.id(id), defaultValue);
	}
}
