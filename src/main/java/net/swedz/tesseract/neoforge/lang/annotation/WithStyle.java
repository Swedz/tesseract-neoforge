package net.swedz.tesseract.neoforge.lang.annotation;

import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.lang.LangManager;
import net.swedz.tesseract.neoforge.lang.exception.UndefinedStyleException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface WithStyle
{
	/**
	 * <p>Defines the chat style to use for this method. Styles are defined with
	 * {@link LangManager#style(String, Style)}. If no style is defined for
	 * this key, an {@link UndefinedStyleException} will be thrown on initialization.</p>
	 *
	 * @return the chat style key
	 */
	String value() default "default";
}
