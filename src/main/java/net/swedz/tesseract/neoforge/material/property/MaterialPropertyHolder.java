package net.swedz.tesseract.neoforge.material.property;

import java.util.Optional;

public interface MaterialPropertyHolder
{
	<T> boolean has(MaterialProperty<T> property);
	
	<T> MaterialPropertyHolder set(MaterialProperty<T> property, T value);
	
	<T> MaterialPropertyHolder setOptional(MaterialProperty<Optional<T>> property, T value);
	
	<T> T get(MaterialProperty<T> property);
}
