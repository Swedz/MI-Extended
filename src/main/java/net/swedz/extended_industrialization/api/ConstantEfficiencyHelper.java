package net.swedz.extended_industrialization.api;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import net.swedz.extended_industrialization.EIConfig;
import net.swedz.extended_industrialization.machines.components.craft.ModularCrafterAccessBehavior;

public final class ConstantEfficiencyHelper
{
	public static long getRecipeEu(CableTier tier)
	{
		return tier.eu / 4;
	}
	
	public static long getActualMaxRecipeEu(Object blockEntity, CrafterComponent.Behavior behavior)
	{
		if(EIConfig.machineEfficiencyHack.useVoltageForEfficiency() && blockEntity instanceof CableTierHolder machine)
		{
			return ConstantEfficiencyHelper.getRecipeEu(machine.getCableTier());
		}
		return behavior.getMaxRecipeEu();
	}
	
	public static long getActualMaxRecipeEu(Object blockEntity, ModularCrafterAccessBehavior behavior)
	{
		if(EIConfig.machineEfficiencyHack.useVoltageForEfficiency() && blockEntity instanceof CableTierHolder machine)
		{
			return ConstantEfficiencyHelper.getRecipeEu(machine.getCableTier());
		}
		return behavior.getBaseMaxRecipeEu();
	}
}