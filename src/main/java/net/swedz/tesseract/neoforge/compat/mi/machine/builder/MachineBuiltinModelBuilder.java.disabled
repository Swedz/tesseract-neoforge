package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.machines.models.MachineCasing;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;

public class MachineBuiltinModelBuilder
{
	private final MachineCasing casing;
	private final String        overlayFolder;
	
	private boolean front, top, side, active;
	
	private String outputTexture;
	
	MachineBuiltinModelBuilder(MachineCasing casing, String overlayFolder)
	{
		Assert.noneNull(overlayFolder);
		this.casing = casing;
		this.overlayFolder = overlayFolder;
	}
	
	public MachineBuiltinModelBuilder front(boolean front)
	{
		this.front = front;
		return this;
	}
	
	public MachineBuiltinModelBuilder front()
	{
		return this.front(true);
	}
	
	public MachineBuiltinModelBuilder top(boolean top)
	{
		this.top = top;
		return this;
	}
	
	public MachineBuiltinModelBuilder top()
	{
		return this.top(true);
	}
	
	public MachineBuiltinModelBuilder side(boolean side)
	{
		this.side = side;
		return this;
	}
	
	public MachineBuiltinModelBuilder side()
	{
		return this.side(true);
	}
	
	public MachineBuiltinModelBuilder active(boolean active)
	{
		this.active = active;
		return this;
	}
	
	public MachineBuiltinModelBuilder active()
	{
		return this.active(true);
	}
	
	public MachineBuiltinModelBuilder outputTexture(String texture)
	{
		this.outputTexture = texture;
		return this;
	}
	
	public MachineBuiltinModelBuilder outputTexture(ResourceLocation texture)
	{
		return this.outputTexture(texture == null ? null : texture.toString());
	}
	
	public MachineBuiltinModelBuilder outputTextureDefault()
	{
		return this.outputTexture((String) null);
	}
	
	public MachineBuiltinModelBuilder outputTextureItem()
	{
		return this.outputTexture(MI.id("block/overlays/output_item"));
	}
	
	public MachineBuiltinModelBuilder outputTextureFluid()
	{
		return this.outputTexture(MI.id("block/overlays/output_fluid"));
	}
	
	public MachineBuiltinModelBuilder outputTextureEnergy()
	{
		return this.outputTexture(MI.id("block/overlays/output_energy"));
	}
	
	void build(MIHook hook, String id, MachineCasing casing)
	{
		Assert.notNull(casing, "Machine casing must be provided");
		HackedMachineRegistrationHelper.addMachineModel(hook, id, casing, overlayFolder, front, top, side, active, outputTexture);
	}
	
	void build(MIHook hook, String id)
	{
		this.build(hook, id, casing);
	}
}
