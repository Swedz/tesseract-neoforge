package net.swedz.tesseract.neoforge.behavior;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class BehaviorRegistry<H extends BehaviorHolder<B, C>, B extends Behavior<C>, C>
{
	private final Function<List<B>, H> holderCreator;
	
	private final List<Supplier<B>> behaviorCreators = Lists.newArrayList();
	
	private BehaviorRegistry(Function<List<B>, H> holderCreator)
	{
		this.holderCreator = holderCreator;
	}
	
	public static <H extends BehaviorHolder<B, C>, B extends Behavior<C>, C> BehaviorRegistry<H, B, C> create(Function<List<B>, H> holderCreator)
	{
		return new BehaviorRegistry<>(holderCreator);
	}
	
	public static <B extends Behavior<C>, C> BehaviorRegistry<BehaviorHolder<B, C>, B, C> create()
	{
		return new BehaviorRegistry<>(BehaviorHolder::new);
	}
	
	public void register(Supplier<B> creator)
	{
		behaviorCreators.add(creator);
	}
	
	public H createHolder()
	{
		return holderCreator.apply(behaviorCreators.stream().map(Supplier::get).toList());
	}
}
