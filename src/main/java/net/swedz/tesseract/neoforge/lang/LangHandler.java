package net.swedz.tesseract.neoforge.lang;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.helper.NamingConventionHelper;
import net.swedz.tesseract.neoforge.interfaceproxy.InterfaceProxyHandler;
import net.swedz.tesseract.neoforge.lang.annotation.LangKey;
import net.swedz.tesseract.neoforge.lang.annotation.LangKeyPattern;
import net.swedz.tesseract.neoforge.lang.annotation.Parsed;
import net.swedz.tesseract.neoforge.lang.annotation.WithStyle;
import net.swedz.tesseract.neoforge.lang.parser.LangEntryParser;
import net.swedz.tesseract.neoforge.lang.parser.ParserProvider;
import net.swedz.tesseract.neoforge.lang.style.LangEntryStyle;
import net.swedz.tesseract.neoforge.lang.style.StyleProvider;

import java.lang.reflect.Method;
import java.util.Optional;

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
	
	private StyleProvider getStyle(WithStyle annotationStyle)
	{
		return annotationStyle != null ?
				manager.getStyle(annotationStyle.value()) :
				manager.getStyle("default");
	}
	
	private LangEntryParser[] getParsers(Method method)
	{
		LangEntryParser[] parsers = new LangEntryParser[method.getParameterCount()];
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
			
			if(param.isAnnotationPresent(WithStyle.class))
			{
				var annotationStyle = param.getAnnotation(WithStyle.class);
				final ParserProvider finalParser = parser;
				parser = (context, value) ->
				{
					var style = this.getStyle(annotationStyle);
					return finalParser.parse(context, value).copy().withStyle(style == null ? null : style.get(context));
				};
			}
			
			var context = LangContext.of(param, manager::getStyle, manager::getParser);
			
			parsers[index] = new LangEntryParser(context, parser);
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
						new LangEntryStyle(LangContext.of(method, manager::getStyle, manager::getParser), style),
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
