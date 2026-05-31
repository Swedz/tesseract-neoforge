package net.swedz.tesseract.neoforge.helper.datagen;

public interface BlockAndItemModelGeneratorsByIdentifier
{
	default SimpleModelCollectorByIdentifier modelOutputById()
	{
		throw new UnsupportedOperationException();
	}
}
