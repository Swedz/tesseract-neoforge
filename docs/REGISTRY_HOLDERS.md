# Registry Holders

Provided in Tesseract are what are defined as `RegisteredObjectHolder`s, or more simply put: "registry holders". These
holders "hold" a registerable object type and provide helper methods for defining how they should be registered and
used.

Below is a brief overview of each type of holder and a simple example.

## Items

`ItemHolder<T>`s are used to define items. The `<T>` generic in this case defines the `Item` type for the item. In most
cases, this is just `Item`.

## Blocks

`BlockHolder<T>`s are used to define blocks. The `<T>` generic in this case defines the `Block` type for the block. In
most cases, this is just `Block`.

### Blocks with Items

`BlockWithItemHolder<B, I>`s are used to define blocks that also have an item. The `<B>` generic in this case defines
the `Block` type for the block, and the `<I>` generic defines the `Item` type for the item. In most cases these are
just `Block` and `BlockItem`.

Most blocks should be defined as a block with item, as the item part refers to the item used in inventories.

## Fluids

`FluidHolder<F, FT, FB, FBI>`s are used to define fluids. The generics are as follows:

- `<F>`: `Fluid`
- `<FT>`: `FluidType`
- `<FB>`: `Block` representing the fluid in world
- `<FBI>`: `BucketItem` to use for the bucket

Fluid holders are a bit more complex than the other holders. It is recommended to make a fluid holder implementation of
your own for your fluids.

### Modern Industrialization-like Fluids

If you are making an addon for MI, you can use the `MIFluidHolder` fluid holder implementation. This implementation
streamlines the process of making fluids by using MI's fluid api.

### Fluid Asset Datagen

It is strongly recommended to use the builtin datagen providers for generating fluid textures and models (including
buckets). Note that this should work properly for all kinds of `FluidHolder`s, but requires MI to function.

The below code is an example of what you can place inside of your `GatherDataEvent` listener.

```java
MIDatagenHooks.Client.includeMISprites(event);
MIDatagenHooks.Client.addTexturesHook(event, "<your mod id>", /* collection of your fluids */);
```