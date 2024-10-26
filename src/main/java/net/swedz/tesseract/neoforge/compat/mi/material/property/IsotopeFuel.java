package net.swedz.tesseract.neoforge.compat.mi.material.property;

import aztech.modern_industrialization.nuclear.INeutronBehaviour;
import aztech.modern_industrialization.nuclear.IsotopeParams;
import net.swedz.tesseract.neoforge.material.Material;

import static net.swedz.tesseract.neoforge.compat.mi.material.property.MIMaterialProperties.*;

public class IsotopeFuel extends IsotopeParams
{
	public final int maxTemp;
	public final double neutronsMultiplication;
	public final double directEnergyFactor;
	public final int tempLimitLow;
	public final int tempLimitHigh;
	
	public IsotopeFuel(double thermalAbsorbProba, double thermalScatterings, int maxTemp, int tempLimitLow, int tempLimitHigh,
					   double neutronsMultiplication, double directEnergyFactor)
	{
		super(
				thermalAbsorbProba,
				INeutronBehaviour.reduceCrossProba(thermalAbsorbProba, 0.1),
				thermalScatterings,
				INeutronBehaviour.reduceCrossProba(thermalScatterings, 0.5)
		);
		this.maxTemp = maxTemp;
		this.neutronsMultiplication = neutronsMultiplication;
		this.directEnergyFactor = directEnergyFactor;
		this.tempLimitLow = tempLimitLow;
		this.tempLimitHigh = tempLimitHigh;
	}
	
	public static IsotopeFuel mix(IsotopeFuel a, IsotopeFuel b, double factor)
	{
		factor = 1 - factor;
		
		double newThermalAbsorptionProba = INeutronBehaviour.probaFromCrossSection(mix(a.thermalAbsorption, b.thermalAbsorption, factor));
		double newScatteringProba = INeutronBehaviour.probaFromCrossSection(mix(a.thermalScattering, b.thermalScattering, factor));
		double newNeutronMultiplicationFactor = mix(a.neutronsMultiplication, b.neutronsMultiplication, factor);
		
		double totalEnergy = mix(a.neutronsMultiplication * (1 + a.directEnergyFactor), b.neutronsMultiplication * (1 + b.directEnergyFactor), factor);
		
		int newMaxTemp = (int) mix(a.maxTemp, b.maxTemp, factor);
		int newTempLimitLow = (int) mix(a.tempLimitLow, b.tempLimitLow, factor);
		int newTempLimitHigh = (int) mix(a.tempLimitHigh, b.tempLimitHigh, factor);
		
		double newDirectEnergyFactor = (totalEnergy / newNeutronMultiplicationFactor) - 1;
		
		return new IsotopeFuel(
				newThermalAbsorptionProba,
				newScatteringProba, newMaxTemp, newTempLimitLow, newTempLimitHigh,
				newNeutronMultiplicationFactor, newDirectEnergyFactor
		);
	}
	
	public static IsotopeFuel mix(Material a, Material b, double factor)
	{
		return mix(a.getOrThrow(ISOTOPE), b.getOrThrow(ISOTOPE), factor);
	}
	
	private static double mix(double a, double b, double r)
	{
		return r * a + (1 - r) * b;
	}
}
