package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.machines.MachineBlock;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import com.google.common.collect.Lists;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityWithGuiFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockHolderModifier;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockPropertiesModifier;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockRegistrators;

import java.util.List;
import java.util.function.Consumer;

public abstract class MachineBuilder<T extends MachineBuilder<T>>
{
	public static SingleBlockCraftingMachineBuilder singleBlockCrafting(MIHook hook,
																		String name, String englishName,
																		MachineRecipeType recipeType)
	{
		return new SingleBlockCraftingMachineBuilder(hook, name, englishName, recipeType);
	}
	
	public static SpecialMachineBuilder special(MIHook hook,
												String name, String englishName,
												boolean isMultiblock,
												MachineBlockEntityWithGuiFactory factory)
	{
		return new SpecialMachineBuilder(hook, name, englishName, isMultiblock, factory);
	}
	
	public static HatchMachineBuilder hatch(MIHook hook,
											String name, String englishName)
	{
		return new HatchMachineBuilder(hook, name, englishName);
	}
	
	protected final MIHook hook;
	
	protected final String name;
	protected final String englishName;
	
	protected MachineBlockFactory            blockFactory = MachineBlock::new;
	protected MachineBlockHolderModifier     holderModifier;
	protected MachineBlockPropertiesModifier propertiesModifier;
	
	protected final List<MachineBlockRegistrators> registrators = Lists.newArrayList();
	
	protected boolean                    defaultMineableTags = true;
	protected MachineBuiltinModelBuilder builtinModel;
	
	private boolean isRegistered;
	
	MachineBuilder(MIHook hook,
				   String name, String englishName)
	{
		Assert.noneNull(hook, name, englishName);
		this.hook = hook;
		this.name = name;
		this.englishName = englishName;
	}
	
	public T creator(MachineBlockFactory blockFactory)
	{
		Assert.notNull(blockFactory);
		this.blockFactory = blockFactory;
		return (T) this;
	}
	
	public T modify(MachineBlockHolderModifier modifier)
	{
		this.holderModifier = modifier;
		return (T) this;
	}
	
	public T properties(MachineBlockPropertiesModifier properties)
	{
		this.propertiesModifier = properties;
		return (T) this;
	}
	
	public T registrator(MachineBlockRegistrators registrator)
	{
		Assert.notNull(registrator);
		registrators.add(registrator);
		return (T) this;
	}
	
	public T excludeDefaultMineableTags()
	{
		defaultMineableTags = false;
		return (T) this;
	}
	
	public T builtinModel(MachineCasing casing, String overlayFolder, Consumer<MachineBuiltinModelBuilder> builder)
	{
		Assert.that(builtinModel == null, "Simple model has already been registered for this machine");
		Assert.noneNull(overlayFolder);
		builtinModel = new MachineBuiltinModelBuilder(casing, overlayFolder);
		if(builder != null)
		{
			builder.accept(builtinModel);
		}
		return (T) this;
	}
	
	public T builtinModel(MachineCasing casing, String overlayFolder)
	{
		return this.builtinModel(casing, overlayFolder, null);
	}
	
	protected abstract void internalRegister();
	
	public final T registerMachine()
	{
		Assert.that(!isRegistered, "This machine is already registered");
		isRegistered = true;
		this.internalRegister();
		return (T) this;
	}
}
