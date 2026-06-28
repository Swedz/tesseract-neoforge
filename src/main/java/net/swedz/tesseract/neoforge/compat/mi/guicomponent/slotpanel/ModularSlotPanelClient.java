package net.swedz.tesseract.neoforge.compat.mi.guicomponent.slotpanel;

import aztech.modern_industrialization.client.machines.gui.ClientComponentRenderer;
import aztech.modern_industrialization.client.machines.gui.GuiComponentClient;
import aztech.modern_industrialization.client.machines.gui.MachineScreen;
import aztech.modern_industrialization.inventory.BackgroundRenderedSlot;
import aztech.modern_industrialization.machines.gui.GuiComponent;
import aztech.modern_industrialization.util.Rectangle;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public final class ModularSlotPanelClient extends GuiComponentClient<ModularSlotPanel.Params, ModularSlotPanel.Data>
{
	public ModularSlotPanelClient(ModularSlotPanel.Params params, ModularSlotPanel.Data data)
	{
		super(params, data);
	}
	
	@Override
	public void setupMenu(GuiComponent.MenuFacade menu)
	{
		for(int i = 0; i < params.slots().size(); i++)
		{
			int slotIndex = i;
			ModularSlotPanel.Slot slot = params.slots().get(slotIndex);
			Supplier<Integer> stackLimit = () -> data.stackLimits().get(slotIndex);
			
			class ClientSlot extends SlotWithBackground implements SlotTooltip
			{
				public ClientSlot()
				{
					super(new SimpleContainer(1), 0, ModularSlotPanel.getSlotX(menu.getGuiParams()), ModularSlotPanel.getSlotY(slotIndex) + params.offsetY());
				}
				
				@Override
				public boolean mayPlace(ItemStack stack)
				{
					return slot.insertionChecker().test(stack);
				}
				
				@Override
				public int getMaxStackSize()
				{
					return stackLimit.get();
				}
				
				@Override
				public ResourceLocation getBackgroundAtlasLocation()
				{
					ResourceLocation atlas = slot.atlas();
					return atlas == null ? super.getBackgroundAtlasLocation() : atlas;
				}
				
				@Override
				public int getBackgroundU()
				{
					return this.hasItem() ? 0 : slot.u();
				}
				
				@Override
				public int getBackgroundV()
				{
					return this.hasItem() ? 0 : slot.v();
				}
				
				@Override
				public Component getTooltip()
				{
					return slot.tooltip().get();
				}
			}
			menu.addSlotToMenu(new ClientSlot(), slot.group());
		}
	}
	
	@Override
	public ClientComponentRenderer createRenderer(MachineScreen machineScreen)
	{
		return new ClientComponentRenderer()
		{
			private Rectangle getBox(int leftPos, int topPos)
			{
				return new Rectangle(leftPos + machineScreen.getGuiParams().backgroundWidth, topPos + 10 + params.offsetY(), 31, 14 + (params.slots().size() * 20));
			}
			
			@Override
			public void addExtraBoxes(List<Rectangle> rectangles, int leftPos, int topPos)
			{
				rectangles.add(this.getBox(leftPos, topPos));
			}
			
			@Override
			public void renderBackground(GuiGraphics graphics, int x, int y)
			{
				Rectangle box = this.getBox(x, y);
				int textureX = box.x() - x - box.w();
				graphics.blit(MachineScreen.BACKGROUND, box.x(), box.y(), textureX, 0, box.w(), box.h() - 4);
				graphics.blit(MachineScreen.BACKGROUND, box.x(), box.y() + box.h() - 4, textureX, 252, box.w(), 4);
			}
			
			@Override
			public boolean renderTooltip(MachineScreen screen, Font font, GuiGraphics graphics, int x, int y, int cursorX, int cursorY)
			{
				Slot slot = screen.getFocusedSlot();
				if(slot instanceof SlotTooltip tooltip)
				{
					if(!screen.getFocusedSlot().hasItem())
					{
						graphics.renderTooltip(font, tooltip.getTooltip(), cursorX, cursorY);
						return true;
					}
				}
				return false;
			}
		};
	}
	
	interface SlotTooltip
	{
		Component getTooltip();
	}
	
	public static class SlotWithBackground extends Slot implements BackgroundRenderedSlot
	{
		public SlotWithBackground(Container container, int index, int x, int y)
		{
			super(container, index, x, y);
		}
	}
}
