package net.swedz.tesseract.neoforge.material.part;

public interface MaterialPartAction<T>
{
	void apply(MaterialPartRegisterContext context, T holder);
}
