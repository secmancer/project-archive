package io.github.secmancer.scuffedclient.mixins;

import io.github.secmancer.scuffedclient.module.util.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public class MixinFishingBobberEntity
{
    @Shadow
    private boolean caughtFish;

    @Inject(method = "onTrackedDataSet", at = @At("TAIL"))
    public void onTrackedDataSet(TrackedData<?> data, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (caughtFish && ModuleManager.autoFish.isEnabled().get()) {
            ModuleManager.setAutoFishTimer(10);
            if (client.interactionManager != null) {
                client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            }
        }
    }
}
