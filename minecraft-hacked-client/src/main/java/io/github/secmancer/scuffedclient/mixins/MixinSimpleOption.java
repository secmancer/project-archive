package io.github.secmancer.scuffedclient.mixins;

import io.github.secmancer.scuffedclient.util.option.SimpleForceValueOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(SimpleOption.class)
public class MixinSimpleOption<T> implements SimpleForceValueOption<T>
{
    @Shadow
    T value;
    @Shadow
    @Final
    private Consumer<T> changeCallback;
    @Override
    public void scuffedclient$forceSetValue(T newValue)
    {
        if(!MinecraftClient.getInstance().isRunning())
        {
            value = newValue;
            return;
        }
        if(!value.equals(newValue))
        {
            value = newValue;
            changeCallback.accept(value);
        }
    }
}
