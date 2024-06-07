package net.swedz.tesseract.neoforge.isolatedlistener;

import net.neoforged.bus.api.Event;

public interface IsolatedListener<E extends Event>
{
	void on(E event);
}
