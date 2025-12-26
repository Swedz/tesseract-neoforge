package net.swedz.tesseract.neoforge.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Optional;

public record ConfigManagerArg(
		Optional<ModConfigSpec> spec,
		String path
)
{
}
