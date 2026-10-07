package net.swedz.tesseract.neoforge.lang.annotation;

import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.lang.LangManager;
import net.swedz.tesseract.neoforge.lang.exception.UndefinedStyleException;
import net.swedz.tesseract.neoforge.tooltip.Parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface WithStyleSeparator
{
	/**
	 * <p>Defines the chat style to use for this parameter's separators. Styles are defined with
	 * {@link LangManager#style(String, Style)}. If no style is defined for this key, an
	 * {@link UndefinedStyleException} will be thrown on initialization.</p>
	 *
	 * <p>Only used if the parser implemented for the type this annotation is applied to makes use of the
	 * {@link Parser#COMPONENTS_COMMA_SEPARATED} parser provider.</p>
	 *
	 * @return the chat style key
	 * @see WithStyle
	 */
	String separator() default "default";
	
	/**
	 * <p>Defines the chat style to use for this parameter's elements. Styles are defined with
	 * {@link LangManager#style(String, Style)}. If no style is defined for this key, an
	 * {@link UndefinedStyleException} will be thrown on initialization.</p>
	 *
	 * <p>Only used if the parser implemented for the type this annotation is applied to makes use of the
	 * {@link Parser#COMPONENTS_COMMA_SEPARATED} parser provider.</p>
	 *
	 * @return the chat style key
	 * @see WithStyle
	 */
	String element() default "default";
}
