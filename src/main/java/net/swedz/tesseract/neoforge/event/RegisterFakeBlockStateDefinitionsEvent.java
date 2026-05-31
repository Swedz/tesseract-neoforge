package net.swedz.tesseract.neoforge.event;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.Map;

public final class RegisterFakeBlockStateDefinitionsEvent extends Event implements IModBusEvent
{
	private final Map<Identifier, StateDefinition<Block, BlockState>> definitions;
	
	public RegisterFakeBlockStateDefinitionsEvent(Map<Identifier, StateDefinition<Block, BlockState>> definitions)
	{
		this.definitions = definitions;
	}
	
	public void register(Identifier id, StateDefinition<Block, BlockState> definition)
	{
		if(definitions.put(id, definition) != null)
		{
			throw new IllegalStateException("Block state definition already defined for id " + id);
		}
	}
}
