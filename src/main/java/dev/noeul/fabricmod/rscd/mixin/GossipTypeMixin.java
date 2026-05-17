package dev.noeul.fabricmod.rscd.mixin;

import net.minecraft.world.entity.ai.gossip.GossipType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GossipType.class)
public abstract class GossipTypeMixin {
	@Shadow @Final public static GossipType MINOR_POSITIVE;
	@Shadow @Final public static GossipType MAJOR_POSITIVE;

	@Shadow @Final @Mutable public int max;
	@Shadow @Final @Mutable public int decayPerTransfer;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void rscd$restoreStackableCuringDiscount(CallbackInfo ci) {
		((GossipTypeMixin) (Object) MINOR_POSITIVE).max = 200;

		GossipTypeMixin majorPositive = (GossipTypeMixin) (Object) MAJOR_POSITIVE;
		majorPositive.max = 100;
		majorPositive.decayPerTransfer = 100;
	}
}
