package io.github.secmancer.scuffedclient.module.movement;

import com.lukflug.panelstudio.base.IToggleable;
import io.github.secmancer.scuffedclient.module.util.Module;
import net.minecraft.client.MinecraftClient;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Allows the player to sprint constantly", () -> true, true);
    }

    public IToggleable getToggle() { return this.isEnabled(); }

    @Override
    public void tick(MinecraftClient client) {
        if (isEnabled().get() && client.player != null) {
            client.player.setSprinting(true);
        }
    }
}
