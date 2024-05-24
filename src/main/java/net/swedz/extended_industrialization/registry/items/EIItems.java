package net.swedz.extended_industrialization.registry.items;

import aztech.modern_industrialization.api.energy.CableTier;
import com.google.common.collect.Sets;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.extended_industrialization.EI;
import net.swedz.extended_industrialization.registry.api.CommonCapabilities;
import net.swedz.extended_industrialization.registry.api.CommonModelBuilders;
import net.swedz.extended_industrialization.registry.items.items.ElectricToolItem;
import net.swedz.extended_industrialization.registry.items.items.PhotovoltaicCellItem;
import net.swedz.extended_industrialization.registry.items.items.SteamChainsawItem;
import net.swedz.extended_industrialization.registry.items.items.TinCanFoodItem;
import net.swedz.extended_industrialization.registry.tags.EITags;

import java.util.Set;
import java.util.function.Function;

public final class EIItems
{
	public static final class Registry
	{
		public static final  DeferredRegister.Items ITEMS   = DeferredRegister.createItems(EI.ID);
		private static final Set<ItemHolder>        HOLDERS = Sets.newHashSet();
		
		private static void init(IEventBus bus)
		{
			ITEMS.register(bus);
		}
		
		public static void include(ItemHolder holder)
		{
			HOLDERS.add(holder);
		}
	}
	
	public static void init(IEventBus bus)
	{
		Registry.init(bus);
	}
	
	public static final ItemHolder<SteamChainsawItem> STEAM_CHAINSAW       = create("steam_chainsaw", "Steam Chainsaw", SteamChainsawItem::new, SortOrder.GEAR).tag(ItemTags.AXES).withModel(CommonModelBuilders::handheld).register();
	public static final ItemHolder<ElectricToolItem>  ELETRIC_CHAINSAW     = create("electric_chainsaw", "Electric Chainsaw", (p) -> new ElectricToolItem(p, true), SortOrder.GEAR).tag(ItemTags.AXES).withCapabilities(CommonCapabilities::simpleEnergyItem).withModel(CommonModelBuilders::handheld).register();
	public static final ItemHolder<ElectricToolItem>  ELETRIC_MINING_DRILL = create("electric_mining_drill", "Electric Mining Drill", (p) -> new ElectricToolItem(p, false), SortOrder.GEAR).tag(ItemTags.PICKAXES, ItemTags.SHOVELS).withCapabilities(CommonCapabilities::simpleEnergyItem).withModel(CommonModelBuilders::handheld).register();
	
	public static final ItemHolder<TinCanFoodItem> CANNED_FOOD = create("canned_food", "Canned Food", TinCanFoodItem::new, SortOrder.OTHER_GEAR).withProperties((p) -> p.food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().fast().build())).withModel(CommonModelBuilders::generated).register();
	public static final ItemHolder<Item>           TIN_CAN     = create("tin_can", "Tin Can", Item::new, SortOrder.OTHER_GEAR).withModel(CommonModelBuilders::generated).register();
	
	public static final ItemHolder<Item> NETHERITE_ROTARY_BLADE = create("netherite_rotary_blade", "Netherite Rotary Blade", Item::new, SortOrder.PARTS).withModel(CommonModelBuilders::generated).register();
	public static final ItemHolder<Item> STEEL_COMBINE          = create("steel_combine", "Steel Combine", Item::new, SortOrder.PARTS).withModel(CommonModelBuilders::generated).register();
	
	public static final ItemHolder<Item> MULCH          = create("mulch", "Mulch", Item::new, SortOrder.RESOURCES).withProperties((p) -> p.food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).fast().build())).withModel(CommonModelBuilders::generated).register();
	public static final ItemHolder<Item> NETHERITE_DUST = create("netherite_dust", "Netherite Dust", Item::new, SortOrder.RESOURCES).tag(EITags.itemForge("dusts"), EITags.itemForge("dusts/netherite")).withModel(CommonModelBuilders::generated).register();
	
	public static final ItemHolder<PhotovoltaicCellItem> LV_PHOTOVOLTAIC_CELL = createPhotovoltaicCell("lv", "LV", CableTier.LV, 16, 10 * 60 * 20);
	public static final ItemHolder<PhotovoltaicCellItem> MV_PHOTOVOLTAIC_CELL = createPhotovoltaicCell("mv", "MV", CableTier.MV, 64, 10 * 60 * 20);
	public static final ItemHolder<PhotovoltaicCellItem> HV_PHOTOVOLTAIC_CELL = createPhotovoltaicCell("hv", "HV", CableTier.HV, 256, 10 * 60 * 20);
	/*public static final ItemHolder<PhotovoltaicCellItem> EV_PHOTOVOLTAIC_CELL        = createPhotovoltaicCell("ev", "EV", CableTier.EV, 1024, 10 * 60 * 20);
	public static final ItemHolder<PhotovoltaicCellItem> PERFECTED_PHOTOVOLTAIC_CELL = createPhotovoltaicCell("perfected", "Perfected", CableTier.SUPERCONDUCTOR, 4096, 0);*/
	
	public static Set<ItemHolder> values()
	{
		return Set.copyOf(Registry.HOLDERS);
	}
	
	public static ItemHolder valueOf(String id)
	{
		return Registry.HOLDERS.stream()
				.filter((holder) -> holder.identifier().id().equals(id))
				.findFirst()
				.orElseThrow();
	}
	
	public static <Type extends Item> ItemHolder<Type> create(String id, String englishName, Function<Item.Properties, Type> creator, SortOrder sortOrder)
	{
		ItemHolder<Type> holder = new ItemHolder<>(EI.id(id), englishName, Registry.ITEMS, creator).sorted(sortOrder);
		Registry.include(holder);
		return holder;
	}
	
	public static ItemHolder<PhotovoltaicCellItem> createPhotovoltaicCell(String id, String name, CableTier tier, int euPerTick, int durationTicks)
	{
		return create("%s_photovoltaic_cell".formatted(id), "%s Photovoltaic Cell".formatted(name), (p) -> new PhotovoltaicCellItem(p, tier, euPerTick, durationTicks), SortOrder.PARTS)
				.withProperties((p) -> p.stacksTo(1))
				.tag(EITags.item("photovoltaic_cell"))
				.withModel(CommonModelBuilders::generated)
				.register();
	}
}
