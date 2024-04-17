package net.swedz.miextended;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum MIEText
{
	SOLAR_EFFICIENCY("Solar Efficiency : %d %%"),
	WATER_PUMP_ENVIRONMENT_0("Invalid Pump Environment"),
	WATER_PUMP_ENVIRONMENT_1("Must be in Ocean or River biome.");
	
	private final String englishText;
	
	MIEText(String englishText)
	{
		this.englishText = englishText;
	}
	
	public String englishText()
	{
		return englishText;
	}
	
	public String getTranslationKey()
	{
		return "text.%s.%s".formatted(MIExtended.ID, this.name().toLowerCase());
	}
	
	public MutableComponent text()
	{
		return Component.translatable(this.getTranslationKey());
	}
	
	public MutableComponent text(Object... args)
	{
		return Component.translatable(this.getTranslationKey(), args);
	}
}