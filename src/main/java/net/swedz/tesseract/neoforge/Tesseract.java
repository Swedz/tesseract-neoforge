package net.swedz.tesseract.neoforge;

import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.swedz.tesseract.api.Assert;
import net.swedz.tesseract.neoforge.datagen.client.LanguageDatagenProvider;
import net.swedz.tesseract.neoforge.event.ItemHurtEvent;
import net.swedz.tesseract.neoforge.item.ArmorTickHandler;
import net.swedz.tesseract.neoforge.item.ArmorUnequippedHandler;
import net.swedz.tesseract.neoforge.item.ItemHurtHandler;
import net.swedz.tesseract.neoforge.lang.LangManager;
import net.swedz.tesseract.neoforge.proxy.Proxies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Tesseract.ID)
public final class Tesseract
{
	public static final String ID   = "tesseract_api";
	public static final String NAME = "Tesseract API";
	
	public static Identifier id(String path)
	{
		return Identifier.fromNamespaceAndPath(ID, path);
	}
	
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	
	public Tesseract(IEventBus bus)
	{
		Proxies.initialize();
		
		setupText();
		
		// TODO wait for MI
		/*if(ModLoadedHelper.isLoaded("modern_industrialization"))
		{
			try
			{
				Class.forName("net.swedz.tesseract.neoforge.compat.mi.TesseractMI");
			}
			catch (ClassNotFoundException ignored)
			{
			}
			TesseractMILootConditions.init(bus);
			MIHookTracker.registerRecipeCategoryForMachines(bus);
		}*/
		
		bus.addListener(GatherDataEvent.Client.class, (event) ->
				event.getGenerator().addProvider(true, new LanguageDatagenProvider(event)));
		
		NeoForge.EVENT_BUS.addListener(EntityTickEvent.Pre.class, (event) ->
		{
			if(event.getEntity() instanceof LivingEntity entity)
			{
				for(EquipmentSlot slot : EquipmentSlot.values())
				{
					if(slot.isArmor())
					{
						ItemStack stack = entity.getItemBySlot(slot);
						if(!stack.isEmpty() &&
						   stack.getItem() instanceof ArmorTickHandler handler)
						{
							handler.armorTick(entity, slot, stack);
						}
					}
				}
			}
		});
		
		NeoForge.EVENT_BUS.addListener(LivingEquipmentChangeEvent.class, (event) ->
		{
			if(!event.getFrom().isEmpty() &&
			   event.getFrom().getItem() instanceof ArmorUnequippedHandler handler)
			{
				handler.onUnequipArmor(event.getEntity(), event.getSlot(), event.getFrom(), event.getTo());
			}
		});
		
		NeoForge.EVENT_BUS.addListener(ItemHurtEvent.class, (event) ->
		{
			if(event.getItemStack().getItem() instanceof ItemHurtHandler handler)
			{
				handler.onHurt(event.getEntity(), event.getItemStack(), event.getDamageAmount());
			}
		});
	}
	
	private static TesseractText TEXT;
	
	public static TesseractText text()
	{
		Assert.notNull(TEXT, "Text not loaded yet");
		return TEXT;
	}
	
	private static void setupText()
	{
		var instance = new LangManager(ID)
				.style("tooltip", Style.EMPTY.withColor(0xA9A9A9).withItalic(false))
				.build(TesseractText.class)
				.load();
		LanguageDatagenProvider.include(instance);
		TEXT = instance.lang();
	}
}
