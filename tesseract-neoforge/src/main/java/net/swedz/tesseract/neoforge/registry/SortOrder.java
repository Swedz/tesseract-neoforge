package net.swedz.tesseract.neoforge.registry;

public record SortOrder(int ordinal)
{
	public static final SortOrder UNSORTED = new SortOrder(Integer.MAX_VALUE);
}
