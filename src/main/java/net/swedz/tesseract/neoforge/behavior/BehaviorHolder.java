package net.swedz.tesseract.neoforge.behavior;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BehaviorHolder<B extends Behavior<C>, C>
{
	protected final List<B> behaviors;
	
	public BehaviorHolder(List<B> behaviors)
	{
		this.behaviors = Collections.unmodifiableList(behaviors);
	}
	
	public List<B> behaviors()
	{
		return behaviors;
	}
	
	public Optional<B> behavior(C context)
	{
		return behaviors.stream()
				.filter((behavior) -> behavior.matches(context))
				.findFirst();
	}
}
