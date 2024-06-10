# Modern Industrialization Hooks
Normally making addons for MI is nontrivial, but this system aims to lower the barrier of entry significantly for those who may want to make them.

## Registering a Hook
First you need to register your mod with the hook system. To do so, you must provide a hook registry and a hook listener.

To register your mod's hook registry and listener, use the code below (of course, replacing the parameters with your own). Only one hook can be registered for a given mod id.

```java
MIHooks.register("<your mod id>", new MyMIHookRegistry(), new MyMIHookListener());
```

### Hook Registries
Hook registries are defined by creating an implementation of the `MIHookRegistry` interface. This is used by the hook system to direct blocks, block entities, items, and recipe types to your own defined registries.

Although it is not required to, it is recommended to take the registered objects provided in the `on____Register` methods and save them to a collection of some kind in your codebase. These methods will be called whenever something has been registered by your listener.

Note that blocks and items registered through this system will be created and provided to you in `BlockHolder`s and `ItemHolder`s respectively. These are registerable object wrappers that are futher explained in [REGISTRY_HELPERS.md](REGISTRY_HOLDERS.md). If you have your own system you would like to use for things like this, feel free to convert the objects provided to you to your own versions.

If you don't use a registry in your listener, it is "safe" to return `null` or throw an exception. For example, it is not required to create a block registry for this if you do not register any blocks (this includes machines). Additionally, if you do not use any of the registries in your listener, you can use `MIHookRegistry.NONE` as your registry to simply return `null` for all registries.

### Hook Listeners
Hook listeners are defined by creating an implementation of the `MIHookListener` interface. None of the methods require implementation, so you only need to implement the methods you need for your hook. These methods will be called by the hook system to request any additions.

The method names are rather intuitive, and they provide hook contexts to make registering various machines, recipe types, or many other MI-related features rather trivial.

### Working Example
For a working example of a registry and listener, see [my implementation in Extended Industrialization](https://github.com/Swedz/Extended-Industrialization/tree/master/src/main/java/net/swedz/extended_industrialization/compat/mi).

## Hook Asset Datagen
It is strongly recommended to use datagen for creating assets for things registered by your hook listener. For cases that are not listed below, see [my implementation in Extended Industrialization](https://github.com/Swedz/Extended-Industrialization/tree/master/src/main/java/net/swedz/extended_industrialization/datagen).

### Registering Finicky-ness
There are some cases where some assets must still get registered under MI's namespace. Aside from minor annoyance, this isn't really that much of an issue.

#### Language
If you create any machine recipe types in your hook listener, you may notice that the english name you provided is not displaying in your recipe indexer (JEI/REI/EMI). This can be resolved by adding the following line of code to your `GatherDataEvent` listener and running your datagen.

```java
MIDatagenHooks.Client.addLanguageHook(event, "<your mod id>");
```

#### Machine Casing Models
If you create any machine casings in your hook listener, you may notice that the texture appears broken. This can be resolved by adding the following line of code to your `GatherDataEvent` listener and running your datagen.

```java
MIDatagenHooks.Client.addMachineCasingModelsHook(event, "<your mod id>");
```