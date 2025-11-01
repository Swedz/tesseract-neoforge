# Translatable Texts

Tesseract provides a clean, highly customizable, and ultimately very simple solution for managing translatable texts in
your mods. This system lets you create an interface with methods that return `MutableComponent` and are annotated with
`@LangKey` to define each language entry.

Here's an example text interface:

```java
public interface MyModText
{
	@LangKey(text = "This is a test message.")
	MutableComponent testMessage();
}
```

And then in your mod constructor, you can load it like so:

```java
public MyMod(IEventBus bus, ModContainer container)
{
	MyModText text = new LangManager("my_mod")
			.build(MyModText.class)
			.load()
			.lang();
	
	// use your language entries ...
}
```

This system also supports parameters, styling those parameters, and styling the language entries as a whole.

Parameters can be added to any language entry method as you would any ordinary method. If you need to use a specific
parser, you can annotate a parameter with the `@Parsed` annotation to specify the name of the parser you registered on
your manager.

Styles are able to be applied to language entries simply by attaching the `@WithStyle` annotation either to a parameter
or to the method itself.

For more details on how these annotations work, please read the javadocs in the sources of the annotation in question.