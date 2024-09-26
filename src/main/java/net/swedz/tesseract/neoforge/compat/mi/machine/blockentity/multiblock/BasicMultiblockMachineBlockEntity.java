package net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock;

import aztech.modern_industrialization.api.machine.component.InventoryAccess;
import aztech.modern_industrialization.api.machine.holder.MultiblockInventoryComponentHolder;
import aztech.modern_industrialization.inventory.MIInventory;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.components.ActiveShapeComponent;
import aztech.modern_industrialization.machines.components.IsActiveComponent;
import aztech.modern_industrialization.machines.components.MultiblockInventoryComponent;
import aztech.modern_industrialization.machines.components.OrientationComponent;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.models.MachineModelClientData;
import aztech.modern_industrialization.machines.multiblocks.MultiblockMachineBlockEntity;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.util.Tickable;

public abstract class BasicMultiblockMachineBlockEntity extends MultiblockMachineBlockEntity implements Tickable, MultiblockInventoryComponentHolder
{
	protected final ActiveShapeComponent         activeShape;
	protected final MultiblockInventoryComponent inventory;
	protected final IsActiveComponent            isActive;
	
	public BasicMultiblockMachineBlockEntity(BEP bep, MachineGuiParameters guiParams, ShapeTemplate[] shapeTemplates)
	{
		super(bep, guiParams, new OrientationComponent.Params(false, false, false));
		
		this.activeShape = new ActiveShapeComponent(shapeTemplates);
		this.inventory = new MultiblockInventoryComponent();
		this.isActive = new IsActiveComponent();
		
		this.registerComponents(activeShape, isActive);
	}
	
	protected void updateActive(boolean active)
	{
		isActive.updateActive(active, this);
	}
	
	@Override
	public void onMatchSuccessful()
	{
		inventory.rebuild(shapeMatcher);
	}
	
	@Override
	public void tick()
	{
		if(level.isClientSide())
		{
			return;
		}
		
		this.link();
	}
	
	@Override
	public ShapeTemplate getActiveShape()
	{
		return activeShape.getActiveShape();
	}
	
	@Override
	public MIInventory getInventory()
	{
		return MIInventory.EMPTY;
	}
	
	@Override
	public InventoryAccess getMultiblockInventoryComponent()
	{
		return inventory;
	}
	
	@Override
	protected MachineModelClientData getMachineModelData()
	{
		return new MachineModelClientData(null, orientation.facingDirection).active(isActive.isActive);
	}
}
