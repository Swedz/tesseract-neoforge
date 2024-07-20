package net.swedz.tesseract.neoforge.compat.mi.hook.context.machine;

import aztech.modern_industrialization.machines.MachineBlockEntity;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;

public class MachineMIHookContext implements MIHookContext
{
	private final MachineBlockEntity machineBlockEntity;
	
	public MachineMIHookContext(MachineBlockEntity machineBlockEntity)
	{
		this.machineBlockEntity = machineBlockEntity;
	}
	
	public MachineBlockEntity getMachineBlockEntity()
	{
		return machineBlockEntity;
	}
}
