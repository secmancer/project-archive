package io.github.secmancer.scuffedclient.module.misc;

import com.lukflug.panelstudio.base.IToggleable;
import io.github.secmancer.scuffedclient.module.util.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;

public class AutoFish extends Module
{
    public static int autoFishTimer = 0;

    public AutoFish() {
        super("AutoFish", "Module that allows player to cast and recast their fishing rod.", () -> true, true);
    }

    public IToggleable getToggle() { return this.isEnabled(); }

    @Override
    public void tick(MinecraftClient client){
        if (autoFishTimer > 0) {
            autoFishTimer--;
        }

        if (autoFishTimer == 0 && isEnabled().get()) {
            if (client.interactionManager != null) {
                client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
                autoFishTimer = -1;
            }
        }
    }
}
