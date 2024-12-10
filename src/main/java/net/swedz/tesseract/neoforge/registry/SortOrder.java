package net.swedz.tesseract.neoforge.registry;

import java.util.Arrays;
import java.util.Objects;

public record SortOrder(int ordinal, Comparable... comparables) implements Comparable<SortOrder>
{
	public SortOrder(int ordinal)
	{
		this(ordinal, ordinal);
	}
	
	public static final SortOrder UNSORTED = new SortOrder(Integer.MAX_VALUE);
	
	public SortOrder and(Comparable another)
	{
		Comparable[] newComparables = Arrays.copyOf(comparables, comparables.length + 1);
		newComparables[comparables.length] = another;
		return new SortOrder(ordinal, newComparables);
	}
	
	@Override
	public int compareTo(SortOrder other)
	{
		int index;
		for(index = 0; index < Math.min(other.comparables().length, comparables.length); ++index)
		{
			int comp = comparables[index].compareTo(other.comparables()[index]);
			if(comp != 0)
			{
				return comp;
			}
		}
		if(index < comparables.length)
		{
			return 1;
		}
		else if(index < other.comparables().length)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(this == other)
		{
			return true;
		}
		if(other == null || getClass() != other.getClass())
		{
			return false;
		}
		return ordinal == ((SortOrder) other).ordinal;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(ordinal);
	}
}
