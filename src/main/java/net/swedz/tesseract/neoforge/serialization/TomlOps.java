package net.swedz.tesseract.neoforge.serialization;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.NullObject;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * DynamicOps for using {@link com.mojang.serialization.Codec}s to load objects from configs.
 *
 * @author <a href="https://github.com/Commoble">Commoble</a>
 * @see <a href="https://github.com/Commoble/databuddy/blob/dc623ae7f11e6f2113e94e8e4ed18cca2dfedafa/src/main/java/net/commoble/databuddy/config/ConfigHelper.java#L208-L426">net.commoble.databuddy.config.ConfigHelper.TomlConfigOps</a>
 */
public final class TomlOps implements DynamicOps<Object>
{
	public static final TomlOps INSTANCE = new TomlOps();
	
	private TomlOps()
	{
	}
	
	@Override
	public Object empty()
	{
		return NullObject.NULL_OBJECT;
	}
	
	@Override
	public <U> U convertTo(DynamicOps<U> outOps, Object input)
	{
		return switch (input)
		{
			case Config __ -> this.convertMap(outOps, input);
			case Collection __ -> this.convertList(outOps, input);
			case null -> outOps.empty();
			case NullObject __ -> outOps.empty();
			case Enum inputEnum -> outOps.createString(inputEnum.name());
			case Temporal __ -> outOps.createString(input.toString());
			case String string -> outOps.createString(string);
			case Boolean bool -> outOps.createBoolean(bool);
			case Number number -> outOps.createNumeric(number);
			default ->
					throw new UnsupportedOperationException("TomlConfigOps was unable to convert toml value: " + input);
		};
	}
	
	@Override
	public DataResult<Number> getNumberValue(Object input)
	{
		return input instanceof Number number
				? DataResult.success(number)
				: DataResult.error(() -> "Not a number: " + input);
	}
	
	@Override
	public DataResult<Boolean> getBooleanValue(Object input)
	{
		if(input instanceof Boolean bool)
		{
			return DataResult.success(bool);
		}
		// ensures we don't reset old configs that were serializing 1/0 for bool fields
		else if(input instanceof Number number)
		{
			return DataResult.success(number.intValue() > 0);
		}
		else
		{
			return DataResult.error(() -> "Not a boolean: " + input);
		}
	}
	
	@Override
	public Object createBoolean(boolean value)
	{
		return value;
	}
	
	@Override
	public Object createNumeric(Number i)
	{
		return i;
	}
	
	@Override
	public DataResult<String> getStringValue(Object input)
	{
		if(input instanceof Config || input instanceof Collection)
		{
			return DataResult.error(() -> "Not a string: " + input);
		}
		else
		{
			return DataResult.success(String.valueOf(input));
		}
	}
	
	@Override
	public Object createString(String value)
	{
		return value;
	}
	
	@Override
	public DataResult<Object> mergeToList(Object list, List<Object> values)
	{
		// default mergeToList returns the null object if list is empty;
		// toml doesn't support null values so we need to convert to an empty list
		return DynamicOps.super.mergeToList(list, values).map(obj -> obj == this.empty() ? Lists.newArrayList() : obj);
	}
	
	@Override
	public DataResult<Object> mergeToList(Object list, Object value)
	{
		if(!(list instanceof Collection) && list != this.empty())
		{
			return DataResult.error(() -> "mergeToList called with not a list: " + list, list);
		}
		Collection<Object> result = Lists.newArrayList();
		if(list != this.empty())
		{
			Collection<Object> listAsCollection = (Collection<Object>) list;
			result.addAll(listAsCollection);
		}
		result.add(value);
		return DataResult.success(result);
	}
	
	@Override
	public DataResult<Object> mergeToMap(Object map, Object key, Object value)
	{
		if(!(map instanceof Config) && map != this.empty())
		{
			return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
		}
		DataResult<String> stringResult = this.getStringValue(key);
		Optional<DataResult.Error<String>> badResult = stringResult.error();
		if(badResult.isPresent())
		{
			return DataResult.error(() -> "key is not a string: " + key, map);
		}
		return stringResult.flatMap(s ->
		{
			Config output = TomlFormat.newConfig();
			if(map != this.empty())
			{
				Config oldConfig = (Config) map;
				output.addAll(oldConfig);
			}
			output.add(s, value);
			return DataResult.success(output);
		});
	}
	
	@Override
	public DataResult<Stream<Pair<Object, Object>>> getMapValues(Object input)
	{
		if(!(input instanceof Config config))
		{
			return DataResult.error(() -> "Not a Config: " + input);
		}
		return DataResult.success(config.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())));
	}
	
	@Override
	public Object createMap(Stream<Pair<Object, Object>> map)
	{
		Config result = TomlFormat.newConfig();
		map.forEach(p -> result.add(this.getStringValue(p.getFirst()).getOrThrow(), p.getSecond()));
		return result;
	}
	
	@Override
	public DataResult<Stream<Object>> getStream(Object input)
	{
		if(input instanceof Collection collection)
		{
			return DataResult.success(collection.stream());
		}
		return DataResult.error(() -> "Not a collection: " + input);
	}
	
	@Override
	public Object createList(Stream<Object> input)
	{
		return input.toList();
	}
	
	@Override
	public Object remove(Object input, String key)
	{
		if(input instanceof Config oldConfig)
		{
			Config result = TomlFormat.newConfig();
			oldConfig.entrySet().stream()
					.filter(entry -> !Objects.equals(entry.getKey(), key))
					.forEach(entry -> result.add(entry.getKey(), entry.getValue()));
			return result;
		}
		return input;
	}
	
	@Override
	public String toString()
	{
		return "TOML";
	}
}
