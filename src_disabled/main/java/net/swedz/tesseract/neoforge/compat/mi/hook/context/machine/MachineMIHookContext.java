package net.swedz.tesseract.neoforge.compat.mi.hook.context.machine;

import aztech.modern_industrialization.machines.MachineBlockEntity;

public class MachineMIHookContext
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
