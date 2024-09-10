# Behaviors

When you need something that has multiple behaviors that provide functionality based on a context. The behavior
registry system is built to allow multiple different sources (typically mods) to provide said functionalities.

## Creating a Behavior Registry

Behavior registries will hold all of your behaviors. You cannot access the registry directly, but you can create a
holder that will contain instances of all of the behaviors registered into the registry. You can make your own
holder to allow for helper methods or other operations, or you can just use a standard `BehaviorHolder`. Registries
can be stored however you'd like, but the simplest way is to use a static field with a method to register your
behaviors and a static block to load your default behaviors (if you have any). This way, other sources can add to
your registry. Although, if you don't want that, you can restrict access to this too.

```java
private static final BehaviorRegistry<BehaviorHolder<Behavior, Context>, Behavior, Context> REGISTRY = BehaviorRegistry.create();

public static void registerBehavior(Supplier<Behavior> creator)
{
	REGISTRY.register(creator);
}

static
{
	registerBehavior(MyBehavior::new);
	registerBehavior(MyOtherBehavior::new);
}
```

Then, when you want to start using your behavior registry, you can create a `BehaviorHolder` using
`REGISTRY.createHolder()`. You shouldn't create new holders every time you need to access the registry, but instead
cache it and reference them as needed for a given instance. Each holder has its own instances of each behavior, so
you can use variables within your behavior without conflicting with other holder instances. You can fetch behaviors
from your holder using `BehaviorHolder#behaviors` and `BehaviorHolder#behavior(Context)`.

## Creating Behaviors

Behaviors can work however you'd like, and are completely dependent on your use case. By default, there is only
the `boolean match(Context)` method that is used to check whether a given context fits for this behavior. You also
need to create a behavior context that contains the data relevant to your behaviors for this registry.

No two behaviors should match the same context, as the `BehaviorHolder#behavior(Context)` simply grabs the first
matching behavior. If you want to allow for multiple behaviors to match the same context, you will need to make your
own `BehaviorHolder` type and supply the creator for it in the `BehaviorRegistry.create(...)` method.