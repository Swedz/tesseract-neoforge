package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.machines.models.MachineCasing;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;

public class MachineBuiltinModelBuilder
{
	private final MachineCasing casing;
	private final String        overlayFolder;
	
	private boolean front, top, side, active;
	
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
	
	public MachineBuiltinModelBuilder top(boolean top)
	{
		this.top = top;
		return this;
	}
	
	public MachineBuiltinModelBuilder side(boolean side)
	{
		this.side = side;
		return this;
	}
	
	public MachineBuiltinModelBuilder active(boolean active)
	{
		this.active = active;
		return this;
	}
	
	void build(MIHook hook, String id, MachineCasing casing)
	{
		Assert.notNull(casing, "Machine casing must be provided");
		HackedMachineRegistrationHelper.addMachineModel(hook, id, casing, overlayFolder, front, top, side, active);
	}
	
	void build(MIHook hook, String id)
	{
		this.build(hook, id, casing);
	}
}
