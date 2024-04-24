package net.swedz.extended_industrialization.mixin.mi.hook;

import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import net.swedz.extended_industrialization.hook.mi.MIMachineHook;
import net.swedz.extended_industrialization.hook.mi.tracker.MIHookTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MIMachineRecipeTypes.class)
public class HookMachineRecipeTypesMixin
{
	@Inject(
			method = "init",
			at = @At("TAIL")
	)
	private static void init(CallbackInfo callback)
	{
		MIHookTracker.open();
		MIMachineHook.machineRecipeTypes();
		MIHookTracker.close();
	}
}