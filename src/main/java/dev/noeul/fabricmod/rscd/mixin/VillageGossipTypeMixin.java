package dev.noeul.fabricmod.rscd.mixin;

import net.minecraft.village.VillageGossipType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(VillageGossipType.class)
public class VillageGossipTypeMixin {
	@ModifyArgs(method = "<clinit>", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/village/VillageGossipType;<init>(Ljava/lang/String;ILjava/lang/String;IIII)V"
	))
	private static void rscd$modifyArg$_clinit_$0(Args args) {
		switch (args.<String>get(2)) {
			case "minor_positive" -> args.set(4, 200);
			case "major_positive" -> {
				args.set(4, 100);
				args.set(6, 100);
			}
		}
	}
}
