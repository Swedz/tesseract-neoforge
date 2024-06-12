package net.swedz.tesseract.neoforge.localizedlistener;

import net.neoforged.bus.api.Event;

public interface LocalizedListener<E extends Event>
{
	void on(E event);
}
