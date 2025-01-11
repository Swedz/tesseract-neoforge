# Configs

NeoForge configuration files (.toml format) can be created very simply with the help of Tesseract! All you need to
do is make an interface that will act as your config, and then register it in your mod's constructor.

Here's an example of a config interface:

```java
public interface MyModConfig
{
	@ConfigKey("number")
	@ConfigComment("This is a test option that stores an integer") // optional
	@Range.Integer(min = 1, max = 100) // optional
	default int number()
	{
		// Here is where you define your default value
		return 10;
	}
	
	@ConfigKey("section")
	@SubSection
	Section section();
	
	interface Section
	{
		@ConfigKey("test")
		@ConfigComment("This is a test option that stores a boolean inside of a subsection")
		default boolean test()
		{
			return true;
		}
	}
}
```

And then in your mod constructor, you can load it like so:

```java
public MyMod(IEventBus bus, ModContainer container)
{
	MyModConfig config = new ConfigManager()
			.build(MyModConfig.class)
			.register(container, ModConfig.Type.COMMON) // use whatever config type you need
			.listenToLoad(bus)
			.config();
	
	// use your config values ...
}
```

This system also supports storing custom data types using a codec. You can register codecs on your `ConfigManager`
instance with `.codecs().register(type, codec)`. That is all! You can then use your type as the return type for your
methods in your config.

## Limitations

Do note that collections (List, Set, etc.) and maps are not natively supported as of right now. If you need to store a
collection or map of data, you will need to make your own type and codec.