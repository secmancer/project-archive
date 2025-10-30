package io.github.secmancer.scuffedclient.mixins;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import io.github.secmancer.scuffedclient.module.util.ModuleManager;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlockState.class)
public class MixinAbstractBlockState extends State<Block, BlockState> {
    private MixinAbstractBlockState(Block object, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec)
    {
        super(object, immutableMap, mapCodec);
    }

    @Inject(at = @At("TAIL"), method = { "getAmbientOcclusionLightLevel(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F"}, cancellable = true)
    private void onGetAmbientOcclusionLightLevel(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<Float> cir)
    {
        if (!ModuleManager.xray.isEnabled().get() && MinecraftClient.getInstance().player != null)
            return;
        cir.setReturnValue(1F);
    }
}
