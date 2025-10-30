package io.github.secmancer.scuffedclient.mixins;

import io.github.secmancer.scuffedclient.module.render.Xray;
import io.github.secmancer.scuffedclient.module.util.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock {
    @Inject(at = @At("RETURN"), method = "shouldDrawSide(" + "Lnet/minecraft/block/BlockState;" +
            "Lnet/minecraft/world/BlockView;" +
            "Lnet/minecraft/util/math/BlockPos;" +
            "Lnet/minecraft/util/math/Direction;" +
            "Lnet/minecraft/util/math/BlockPos;" +
            ")Z",
            cancellable = true)
    private static void shouldDrawSide(BlockState state, BlockView reader, BlockPos pos, Direction face, BlockPos blockPos, CallbackInfoReturnable<Boolean> ci) {
        if(ModuleManager.xray.isEnabled().get() && MinecraftClient.getInstance().player != null) {
            boolean shouldXray = Xray.isXRayBlock(state.getBlock());
            ci.setReturnValue(shouldXray);
        }
    }
}
