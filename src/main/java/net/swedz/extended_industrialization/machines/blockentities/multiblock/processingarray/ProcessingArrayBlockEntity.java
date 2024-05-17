package net.swedz.extended_industrialization.machines.blockentities.multiblock.processingarray;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.MIBlock;
import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.guicomponents.ShapeSelection;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.models.MachineCasings;
import aztech.modern_industrialization.machines.multiblocks.HatchFlags;
import aztech.modern_industrialization.machines.multiblocks.HatchType;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.multiblocks.SimpleMember;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.swedz.extended_industrialization.machines.blockentities.multiblock.multiplied.ElectricMultipliedCraftingMultiblockBlockEntity;
import net.swedz.extended_industrialization.machines.components.craft.processingarray.ProcessingArrayMachineComponent;
import net.swedz.extended_industrialization.machines.guicomponents.processingarraymachineslot.ProcessingArrayMachineSlot;
import net.swedz.extended_industrialization.machines.multiblock.members.PredicateSimpleMember;
import net.swedz.extended_industrialization.registry.tags.EITags;
import net.swedz.extended_industrialization.text.EIText;

import java.util.stream.IntStream;

public final class ProcessingArrayBlockEntity extends ElectricMultipliedCraftingMultiblockBlockEntity
{
	private final ProcessingArrayMachineComponent machines;
	
	public ProcessingArrayBlockEntity(BEP bep)
	{
		super(bep, "processing_array", SHAPE_TEMPLATES, null, null, MachineTier.LV);
		
		this.machines = new ProcessingArrayMachineComponent();
		
		this.registerComponents(machines);
		
		this.registerGuiComponent(new ProcessingArrayMachineSlot.Server(
				this,
				() -> this.getMachineStackSize(activeShape.getActiveShapeIndex()),
				machines
		));
		
		this.registerGuiComponent(new ShapeSelection.Server(
				new ShapeSelection.Behavior()
				{
					@Override
					public void handleClick(int line, int delta)
					{
						int newShapeIndex = Mth.clamp(activeShape.getActiveShapeIndex() + delta, 0, SHAPE_TEMPLATES.length - 1);
						int newMachineStackSize = ProcessingArrayBlockEntity.this.getMachineStackSize(newShapeIndex);
						if(newMachineStackSize < machines.getMachines().getCount())
						{
							return;
						}
						activeShape.incrementShape(ProcessingArrayBlockEntity.this, delta);
					}
					
					@Override
					public int getCurrentIndex(int line)
					{
						return activeShape.getActiveShapeIndex();
					}
				},
				new ShapeSelection.LineInfo(
						SPLIT,
						IntStream.range(0, SPLIT).map(this::getMachineStackSize).mapToObj(EIText.PROCESSING_ARRAY_SIZE::text).toList(),
						false
				)
		));
	}
	
	@Override
	protected MachineRecipeType getRecipeType()
	{
		return machines.getMachineRecipeType();
	}
	
	@Override
	protected int getMaxMultiplier()
	{
		return machines.getMachineCount();
	}
	
	private int getMachineStackSize(int sizeIndex)
	{
		return (int) (BASE_MACHINES * Math.pow(MULT_MACHINES, sizeIndex));
	}
	
	private static final int MAX_MACHINES  = 64;
	private static final int SPLIT         = 4;
	private static final int BASE_MACHINES = 8;
	private static final int MULT_MACHINES = 2;
	
	private static final ShapeTemplate[] SHAPE_TEMPLATES;
	
	static
	{
		SHAPE_TEMPLATES = new ShapeTemplate[SPLIT];
		
		SimpleMember casing = SimpleMember.forBlock(MIBlock.BLOCK_DEFINITIONS.get(MI.id("clean_stainless_steel_machine_casing")));
		SimpleMember pipe = SimpleMember.forBlock(MIBlock.BLOCK_DEFINITIONS.get(MI.id("stainless_steel_machine_casing_pipe")));
		SimpleMember glass = new PredicateSimpleMember((state) -> state.is(EITags.blockForge("glass")), Blocks.GLASS);
		HatchFlags front = new HatchFlags.Builder().with(HatchType.ENERGY_INPUT).build();
		HatchFlags top = new HatchFlags.Builder().with(HatchType.ITEM_INPUT, HatchType.FLUID_INPUT).build();
		HatchFlags bottom = new HatchFlags.Builder().with(HatchType.ITEM_OUTPUT, HatchType.FLUID_OUTPUT).build();
		
		for(int
			i = 0, size = 3, machines = BASE_MACHINES;
			i < SPLIT && machines <= MAX_MACHINES;
			i++, size += 2, machines *= MULT_MACHINES)
		{
			ShapeTemplate.Builder builder = new ShapeTemplate.Builder(MachineCasings.CLEAN_STAINLESS_STEEL);
			for(int z = 0; z < size; z++)
			{
				boolean isFront = z == 0;
				for(int x = -1; x <= 1; x++)
				{
					for(int y = -1; y <= 1; y++)
					{
						boolean isTop = y == 1;
						boolean isBottom = y == -1;
						boolean isCenter = x == 0 && y == 0;
						boolean isGlass = x != 0 && y == 0;
						builder.add(x, y, z, isCenter ? pipe : isGlass ? glass : casing, isFront ? front : isTop ? top : isBottom ? bottom : null);
					}
				}
			}
			SHAPE_TEMPLATES[i] = builder.build();
		}
	}
	
	public static void registerReiShapes()
	{
		for(ShapeTemplate shapeTemplate : SHAPE_TEMPLATES)
		{
			ReiMachineRecipes.registerMultiblockShape("processing_array", shapeTemplate);
		}
	}
}
