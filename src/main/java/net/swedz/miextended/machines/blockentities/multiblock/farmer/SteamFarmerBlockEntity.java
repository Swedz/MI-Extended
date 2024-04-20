package net.swedz.miextended.machines.blockentities.multiblock.farmer;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.helper.SteamHelper;
import aztech.modern_industrialization.util.Simulation;
import net.swedz.miextended.machines.components.farmer.FarmerComponent;

public final class SteamFarmerBlockEntity extends FarmerBlockEntity
{
	public SteamFarmerBlockEntity(BEP bep)
	{
		super(bep, "steam_farmer", 8, FarmerComponent.PlantingMode.AS_NEEDED);
	}
	
	@Override
	public long consumeEu(long max)
	{
		return SteamHelper.consumeSteamEu(inventory.getFluidInputs(), max, Simulation.ACT);
	}
}