package net.swedz.tesseract.neoforge.helper.datagen;

public interface BlockModelGeneratorsByIdentifier
{
	default BlockStateGeneratorCollectorByIdentifier blockStateOutputById()
	{
		throw new UnsupportedOperationException();
	}
}
