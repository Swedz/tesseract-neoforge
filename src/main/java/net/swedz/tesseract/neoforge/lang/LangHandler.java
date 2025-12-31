package net.swedz.tesseract.neoforge.lang;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.helper.NamingConventionHelper;
import net.swedz.tesseract.neoforge.interfaceproxy.InterfaceProxyHandler;
import net.swedz.tesseract.neoforge.lang.annotation.LangKey;
import net.swedz.tesseract.neoforge.lang.annotation.LangKeyPattern;
import net.swedz.tesseract.neoforge.lang.annotation.Parsed;
import net.swedz.tesseract.neoforge.lang.annotation.WithStyle;
import net.swedz.tesseract.neoforge.lang.exception.UndefinedParserException;
import net.swedz.tesseract.neoforge.lang.exception.UndefinedStyleException;
import net.swedz.tesseract.neoforge.tooltip.Parser;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;

public final class LangHandler extends InterfaceProxyHandler<Component, LangEntry>
{
	private final LangManager manager;
	
	public LangHandler(LangManager manager)
	{
		this.manager = manager;
	}
	
	private String createLangKey(Class<?> langClass, Method method)
	{
		var annotation = method.getAnnotation(LangKey.class);
		Assert.notNull(annotation);
		
		if(!annotation.value().isEmpty())
		{
			return annotation.value().replace("{}", manager.modId());
		}
		
		var prefix = langClass.isAnnotationPresent(LangKeyPattern.class) ?
				langClass.getAnnotation(LangKeyPattern.class).value() :
				"text.{}.";
		prefix = prefix.replace("{}", manager.modId());
		
		var key = !annotation.key().isEmpty() ?
				annotation.key() :
				NamingConventionHelper.fromCamelCaseToSnakeCase(method);
		return prefix + key;
	}
	
	private Supplier<Style> getStyle(WithStyle annotationStyle)
	{
		if(annotationStyle != null)
		{
			var style = manager.getStyle(annotationStyle.value());
			if(style == null)
			{
				throw new UndefinedStyleException(annotationStyle.value());
			}
			return style;
		}
		return manager.getStyle("default");
	}
	
	private Supplier<Parser<?>>[] getParsers(Method method)
	{
		Supplier<Parser<?>>[] parsers = new Supplier[method.getParameterCount()];
		for(int index = 0; index < method.getParameterCount(); index++)
		{
			var param = method.getParameters()[index];
			var paramType = param.getType();
			
			String parserKey;
			if(param.isAnnotationPresent(Parsed.class))
			{
				var annotationParsed = param.getAnnotation(Parsed.class);
				parserKey = annotationParsed.value();
			}
			else
			{
				parserKey = "default";
			}
			
			var parser = manager.getParser(parserKey, paramType);
			if(parser == null)
			{
				if(parserKey.equals("default"))
				{
					parser = () -> Parser.OBJECT;
				}
				else
				{
					throw new UndefinedParserException(parserKey);
				}
			}
			
			if(param.isAnnotationPresent(WithStyle.class))
			{
				var annotationStyle = param.getAnnotation(WithStyle.class);
				final Supplier<Parser> finalParser = parser::get;
				parser = () -> (value) ->
				{
					var style = this.getStyle(annotationStyle);
					return finalParser.get().parse(value).copy().withStyle(style == null ? null : style.get());
				};
			}
			
			parsers[index] = parser;
		}
		return parsers;
	}
	
	private static boolean includeFallback(Class<?> proxyClass, LangKey methodAnnotation)
	{
		if(proxyClass.isAnnotationPresent(LangKeyPattern.class))
		{
			var proxyAnnotation = proxyClass.getAnnotation(LangKeyPattern.class);
			if(proxyAnnotation.includeFallback().length > 0)
			{
				return proxyAnnotation.includeFallback()[0];
			}
		}
		return methodAnnotation.includeFallback();
	}
	
	@Override
	protected Optional<LangEntry> generate(Class<?> proxyClass, Object proxy, Method method)
	{
		if(method.isAnnotationPresent(LangKey.class))
		{
			var annotation = method.getAnnotation(LangKey.class);
			var methodSignature = method.toGenericString();
			if(method.getReturnType().equals(MutableComponent.class))
			{
				var key = this.createLangKey(proxyClass, method);
				var style = this.getStyle(method.getAnnotation(WithStyle.class));
				var parsers = this.getParsers(method);
				var entry = new LangEntry(
						key,
						annotation.text().length == 0 ? null : annotation.text()[0],
						includeFallback(proxyClass, annotation),
						style,
						parsers
				);
				return Optional.of(entry);
			}
			else
			{
				throw new IllegalStateException("Method %s does not return MutableComponent".formatted(methodSignature));
			}
		}
		return Optional.empty();
	}
}
