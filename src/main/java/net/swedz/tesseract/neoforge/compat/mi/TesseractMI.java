package net.swedz.tesseract.neoforge.compat.mi;

import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied.EuCostTransformer;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.HatchMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineCasingsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineProcessConditionsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineRecipeTypesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MultiblockMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockCraftingMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockSpecialMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.tooltip.MIParser;
import net.swedz.tesseract.neoforge.datagen.client.LanguageDatagenProvider;
import net.swedz.tesseract.neoforge.lang.LangManager;

public final class TesseractMI
{
	static
	{
		setupText();
	}
	
	public static void init(String modId)
	{
		MIHooks.triggerHookListeners(modId, (hook, listener) ->
		{
			listener.machineProcessConditions(new MachineProcessConditionsMIHookContext(hook));
			
			listener.machineRecipeTypes(new MachineRecipeTypesMIHookContext(hook));
			
			listener.machineCasings(new MachineCasingsMIHookContext(hook));
			
			listener.hatches(new HatchMIHookContext(hook));
			listener.singleBlockSpecialMachines(new SingleBlockSpecialMachinesMIHookContext(hook));
			listener.singleBlockCraftingMachines(new SingleBlockCraftingMachinesMIHookContext(hook));
			listener.multiblockMachines(new MultiblockMachinesMIHookContext(hook));
		});
	}
	
	private static TesseractMIText TEXT;
	
	public static TesseractMIText text()
	{
		Assert.notNull(TEXT, "Text not loaded yet");
		return TEXT;
	}
	
	private static void setupText()
	{
		var instance = new LangManager(Tesseract.ID)
				.style("tooltip", Style.EMPTY.withColor(0xA9A9A9).withItalic(false))
				.defaultParser(TesseractMIText.TieredMachineRecipeType.class, MIParser.MACHINE_RECIPE_TYPE_PARSER)
				.defaultParser(EuCostTransformer.class, MIParser.EU_COST_TRANSFORMER_PARSER)
				.build(TesseractMIText.class)
				.load();
		LanguageDatagenProvider.include(instance);
		TEXT = instance.lang();
	}
}
