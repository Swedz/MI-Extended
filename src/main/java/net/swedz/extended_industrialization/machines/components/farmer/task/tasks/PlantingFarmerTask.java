package net.swedz.extended_industrialization.machines.components.farmer.task.tasks;

import aztech.modern_industrialization.machines.components.MultiblockInventoryComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.swedz.extended_industrialization.machines.components.farmer.FarmerComponentPlantableStacks;
import net.swedz.extended_industrialization.machines.components.farmer.PlantableConfigurableItemStack;
import net.swedz.extended_industrialization.machines.components.farmer.block.FarmerBlock;
import net.swedz.extended_industrialization.machines.components.farmer.block.FarmerBlockMap;
import net.swedz.extended_industrialization.machines.components.farmer.block.FarmerTile;
import net.swedz.extended_industrialization.machines.components.farmer.task.FarmerTask;
import net.swedz.extended_industrialization.machines.components.farmer.task.FarmerTaskType;

import java.util.List;

public final class PlantingFarmerTask extends FarmerTask
{
	public PlantingFarmerTask(MultiblockInventoryComponent inventory, FarmerBlockMap blockMap, FarmerComponentPlantableStacks plantableStacks, int maxOperations, int processInterval)
	{
		super(FarmerTaskType.PLANTING, inventory, blockMap, plantableStacks, maxOperations, processInterval);
	}
	
	@Override
	protected boolean run()
	{
		List<PlantableConfigurableItemStack> plantables = plantableStacks.getItems();
		plantables.removeIf((plantable) -> !plantable.isPlantable() || (!plantingMode.includeEmptyStacks() && plantable.getStack().isEmpty()));
		
		if(plantables.size() == 0)
		{
			return false;
		}
		
		int blockIndex = 0;
		for(FarmerTile tile : blockMap)
		{
			FarmerBlock crop = tile.crop();
			
			int index = plantingMode.index(tile, plantables);
			PlantableConfigurableItemStack plantable = plantables.get(index);
			if(tile.canBePlantedOnBy(level, plantable.asPlantable()) && !plantable.getStack().isEmpty())
			{
				BlockPos pos = crop.pos();
				BlockState state = crop.state(level);
				if(state.isAir())
				{
					BlockState plantState = plantable.getPlant(level, pos);
					
					plantable.getStack().decrement(1);
					
					crop.setBlock(level, plantState, 1 | 2, GameEvent.BLOCK_PLACE, plantState);
					
					if(operations.operate())
					{
						return true;
					}
				}
			}
			blockIndex++;
		}
		
		return operations.didOperate();
	}
}