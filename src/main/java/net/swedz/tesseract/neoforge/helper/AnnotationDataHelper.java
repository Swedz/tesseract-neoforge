package net.swedz.tesseract.neoforge.helper;

import net.neoforged.fml.loading.modscan.ModAnnotation;
import net.neoforged.neoforgespi.language.ModFileScanData;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public final class AnnotationDataHelper
{
	public static <E extends Enum<E>> E getEnumOrDefault(ModFileScanData.AnnotationData annotationData, Class<E> enumClass, String key, E defaultValue)
	{
		ModAnnotation.EnumHolder defaultHolder = new ModAnnotation.EnumHolder(null, defaultValue.name());
		ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) annotationData.annotationData().getOrDefault(key, defaultHolder);
		return Enum.valueOf(enumClass, holder.value());
	}
	
	@SafeVarargs
	public static <E extends Enum<E>> EnumSet<E> getEnumSetOrDefault(ModFileScanData.AnnotationData annotationData, Class<E> enumClass, String key, E... defaultValues)
	{
		Object data = annotationData.annotationData().get(key);
		if(data == null)
		{
			EnumSet<E> set = EnumSet.noneOf(enumClass);
			set.addAll(Arrays.asList(defaultValues));
			return set;
		}
		else
		{
			return ((List<ModAnnotation.EnumHolder>) data).stream()
					.map((holder) -> Enum.valueOf(enumClass, holder.value()))
					.collect(Collectors.toCollection(() -> EnumSet.noneOf(enumClass)));
		}
	}
}
