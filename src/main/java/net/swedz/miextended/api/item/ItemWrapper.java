package net.swedz.miextended.api.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.swedz.miextended.api.MCIdentifiable;

import java.util.function.Consumer;

public abstract class ItemWrapper<P extends Item.Properties, S extends ItemWrapper<P, S>>
{
	protected final String modId;
	
	protected MCIdentifiable identifiable;
	
	protected P properties = this.defaultProperties();
	
	protected ItemCreator<P> creator = this.defaultCreator();
	
	protected Consumer<ItemModelBuilder> modelBuilder;
	
	public ItemWrapper(String modId)
	{
		this.modId = modId;
	}
	
	protected abstract P defaultProperties();
	
	protected ItemCreator<P> defaultCreator()
	{
		return Item::new;
	}
	
	protected abstract void commonRegister();
	
	protected final S self()
	{
		return (S) this;
	}
	
	public MCIdentifiable identifiable()
	{
		return identifiable;
	}
	
	public S identifiable(String id, String englishName)
	{
		identifiable = new MCIdentifiable(id, englishName);
		return this.self();
	}
	
	public String modId()
	{
		return modId;
	}
	
	public String id(boolean includeModId)
	{
		return includeModId ? this.encloseId().toString() : identifiable.id();
	}
	
	public ResourceLocation encloseId()
	{
		return new ResourceLocation(modId, identifiable.id());
	}
	
	public ItemCreator<P> creator()
	{
		return creator;
	}
	
	public S withCreator(ItemCreator<P> creator)
	{
		this.creator = creator;
		return this.self();
	}
	
	public Item asItem()
	{
		return BuiltInRegistries.ITEM.get(this.encloseId());
	}
	
	public P properties()
	{
		return properties;
	}
	
	public S withProperties(P properties)
	{
		this.properties = properties;
		return this.self();
	}
	
	public S withProperties(Consumer<P> action)
	{
		action.accept(properties);
		return this.self();
	}
	
	public Consumer<ItemModelBuilder> modelBuilder()
	{
		return modelBuilder;
	}
	
	public S withModel(Consumer<ItemModelBuilder> builder)
	{
		this.modelBuilder = builder;
		return this.self();
	}
	
	public S withBasicModel(String texture)
	{
		return this.withModel((b) -> b
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0", new ResourceLocation(modId, "item/" + texture)));
	}
	
	public S withBasicModel()
	{
		return this.withBasicModel(identifiable.id());
	}
	
	public S register()
	{
		this.commonRegister();
		return this.self();
	}
}