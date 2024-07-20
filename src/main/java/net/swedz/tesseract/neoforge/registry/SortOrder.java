package net.swedz.tesseract.neoforge.registry;

public record SortOrder(int ordinal) implements Comparable<SortOrder>
{
	public static final SortOrder UNSORTED = new SortOrder(Integer.MAX_VALUE);
	
	@Override
	public int compareTo(SortOrder other)
	{
		return Integer.compare(ordinal, other.ordinal());
	}
}
