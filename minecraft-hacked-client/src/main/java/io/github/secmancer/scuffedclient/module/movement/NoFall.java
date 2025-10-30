package io.github.secmancer.scuffedclient.module.movement;

import com.lukflug.panelstudio.base.IToggleable;
import io.github.secmancer.scuffedclient.module.util.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall", "Disables fall damage", () -> true, true);
    }

    public IToggleable getToggle() { return this.isEnabled(); }


    @Override
    public void tick(MinecraftClient client) {
        if (isEnabled().get() && client.player != null) {
            if (client.player.fallDistance > 2.5f) {
                client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
            }
        }
    }
}
