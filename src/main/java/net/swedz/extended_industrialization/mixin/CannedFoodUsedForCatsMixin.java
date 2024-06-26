package net.swedz.extended_industrialization.mixin;

import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.level.ItemLike;
import net.swedz.extended_industrialization.EIItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({Cat.class, Ocelot.class})
public class CannedFoodUsedForCatsMixin
{
	@ModifyArgs(
			method = "<clinit>",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/crafting/Ingredient;of([Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/crafting/Ingredient;",
					ordinal = 0
			)
	)
	private static void modifyTemptIngredient(Args args)
	{
		ItemLike[] original = args.get(0);
		ItemLike[] extended = new ItemLike[original.length + 1];
		System.arraycopy(original, 0, extended, 0, original.length);
		extended[original.length] = EIItems.CANNED_FOOD;
		args.set(0, extended);
	}
}
