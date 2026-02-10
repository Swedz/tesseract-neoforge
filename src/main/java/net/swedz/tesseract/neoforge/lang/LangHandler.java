package net.swedz.tesseract.neoforge.lang;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.MutableComponent;
import net.swedz.tesseract.api.Assert;
import net.swedz.tesseract.interfaceproxy.InterfaceProxyHandler;
import net.swedz.tesseract.neoforge.helper.NamingConventionHelper;
import net.swedz.tesseract.neoforge.lang.annotation.LangKey;
import net.swedz.tesseract.neoforge.lang.annotation.LangKeyPattern;
import net.swedz.tesseract.neoforge.lang.annotation.Parsed;
import net.swedz.tesseract.neoforge.lang.annotation.TextSubSection;
import net.swedz.tesseract.neoforge.lang.annotation.WithStyle;
import net.swedz.tesseract.neoforge.lang.parser.LangEntryParser;
import net.swedz.tesseract.neoforge.lang.parser.ParserProvider;
import net.swedz.tesseract.neoforge.lang.placeholder.LangEntryPlaceholder;
import net.swedz.tesseract.neoforge.lang.style.LangEntryStyle;
import net.swedz.tesseract.neoforge.lang.style.StyleProvider;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LangHandler extends InterfaceProxyHandler<LangEntry<?>>
{
	private final LangManager manager;
	
	private String subSectionPrefix = "";
	
	public LangHandler(LangManager manager)
	{
		this.manager = manager;
	}
	
	private void setSubSectionPrefix(String subSectionPrefix)
	{
		Assert.notNull(subSectionPrefix);
		subSectionPrefix = subSectionPrefix.trim();
		Assert.that(!subSectionPrefix.isBlank(), "TextSubSection must be provided");
		this.subSectionPrefix = subSectionPrefix + ".";
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
		prefix += subSectionPrefix;
		
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
	
	private static boolean includeFallback(Class<?> proxyClass, LangKey langKey)
	{
		if(proxyClass.isAnnotationPresent(LangKeyPattern.class))
		{
			var proxyAnnotation = proxyClass.getAnnotation(LangKeyPattern.class);
			if(proxyAnnotation.includeFallback().length > 0)
			{
				return proxyAnnotation.includeFallback()[0];
			}
		}
		return langKey.includeFallback();
	}
	
	private static final Pattern PLACEHOLDER_BLOCK_PATTERN = Pattern.compile("""
			(\\\\<(?<escaped>[^<>]+)\\\\>)\
			|\
			((?<!\\\\)<(?<block>[^<>]+)(?<!\\\\)>)\
			|\
			(%(\\d+\\$)?.?[0-9a-zA-Z]+)""");
	
	private record PlaceholdersResult(
			String text,
			LangEntryPlaceholder[] providers
	)
	{
	}
	
	private static String replaceInString(String full, String insert, int start, int end)
	{
		return full.substring(0, start) + insert + full.substring(end);
	}
	
	private static Matcher rematch(String updatedText, int index)
	{
		var matcher = PLACEHOLDER_BLOCK_PATTERN.matcher(updatedText);
		matcher.find(index);
		return matcher;
	}
	
	private PlaceholdersResult getPlaceholders(Method method, LangKey langKey)
	{
		if(langKey.text().length == 0 ||
		   !langKey.placeholders())
		{
			return new PlaceholdersResult(null, new LangEntryPlaceholder[method.getParameterCount()]);
		}
		
		var context = LangContext.of(method, manager::getStyle, manager::getParser);
		
		var text = langKey.text()[0];
		var replacedText = text;
		
		List<LangEntryPlaceholder> placeholders = Lists.newArrayList();
		
		var matcher = PLACEHOLDER_BLOCK_PATTERN.matcher(text);
		while(matcher.find())
		{
			var escapedBlock = matcher.group("escaped");
			if(escapedBlock != null)
			{
				int startIndex = matcher.start("escaped");
				int endIndex = matcher.end("escaped");
				replacedText = replaceInString(replacedText, "<" + escapedBlock + ">", startIndex - 2, endIndex + 2);
				
				matcher = rematch(replacedText, startIndex - 2);
				
				continue;
			}
			
			var block = matcher.group("block");
			if(block != null)
			{
				var placeholder = manager.getPlaceholder(block);
				if(placeholder.isEmpty())
				{
					continue;
				}
				placeholders.add(new LangEntryPlaceholder(context, block, placeholder.get()));
				
				int startIndex = matcher.start("block");
				int endIndex = matcher.end("block");
				replacedText = replaceInString(replacedText, "%s", startIndex - 1, endIndex + 1);
				
				matcher = rematch(replacedText, startIndex - 1);
			}
			else
			{
				placeholders.add(null);
			}
		}
		
		return new PlaceholdersResult(replacedText, placeholders.toArray(LangEntryPlaceholder[]::new));
	}
	
	@Override
	protected Optional<LangEntry<?>> generate(Class<?> proxyClass, Object proxy, Method method)
	{
		var methodSignature = method.toGenericString();
		if(method.isAnnotationPresent(TextSubSection.class))
		{
			var textSubSection = method.getAnnotation(TextSubSection.class);
			var section = textSubSection.value();
			if(section.isEmpty())
			{
				section = NamingConventionHelper.fromCamelCaseToSnakeCase(method);
			}
			
			var instance = manager.build(method.getReturnType());
			instance.handler().setSubSectionPrefix(subSectionPrefix + section);
			instance.load();
			return Optional.of(new LangEntry.SubSection(instance.handler(), instance.lang()));
		}
		else if(method.isAnnotationPresent(LangKey.class))
		{
			var langKey = method.getAnnotation(LangKey.class);
			if(method.getReturnType().equals(MutableComponent.class))
			{
				var key = this.createLangKey(proxyClass, method);
				var style = this.getStyle(method.getAnnotation(WithStyle.class));
				var parsers = this.getParsers(method);
				var placeholders = this.getPlaceholders(method, langKey);
				var entry = new LangEntry.Text(
						key,
						placeholders.text(),
						includeFallback(proxyClass, langKey),
						new LangEntryStyle(LangContext.of(method, manager::getStyle, manager::getParser), style),
						parsers,
						placeholders.providers()
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
