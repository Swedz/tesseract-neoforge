package net.swedz.tesseract.neoforge.material.part;

public interface MaterialPartExtraRegister<T>
{
	void apply(MaterialPartRegisterContext context, T holder);
}
