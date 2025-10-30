package io.github.secmancer.scuffedclient.module.movement;

import com.lukflug.panelstudio.base.IToggleable;
import io.github.secmancer.scuffedclient.module.util.Module;
import io.github.secmancer.scuffedclient.module.util.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class BoatFly extends Module {
    private int ticks = 0;
    private double acceleration = 0.1;

    public BoatFly() {
        super("Flying", "Allows the player to fly in a boat", () -> true, true);
    }

    public IToggleable getToggle() { return this.isEnabled(); }

    @Override
    public void tick(MinecraftClient client) {
        if (isEnabled().get() && client.player != null) {
            if (client.player.hasVehicle()) {
                Vec3d vel = client.player.getVelocity();
                boolean upKey = client.options.jumpKey.isPressed();
                boolean forwardKey = client.options.forwardKey.isPressed();
                boolean backwardKey = client.options.backKey.isPressed();
                if (upKey) {
                    if (forwardKey)
                        vel = client.player.getRotationVector().multiply(acceleration);
                    if (backwardKey)
                        vel = client.player.getRotationVector().negate().multiply(acceleration);
                    Objects.requireNonNull(client.player.getVehicle()).setVelocity(new Vec3d(vel.x, ticks < 40 ? 0.3 : -0.04, vel.z));
                    if (acceleration <= 3.0)
                        acceleration += 0.1;
                } else if (acceleration > 0.3) {
                    acceleration -= 0.2;
                }
            }

            if(ticks > 40)
                ticks = 0;

            if(!client.player.isOnGround()) {
                ticks++;
                if (ticks >= 40) client.player.setVelocity(client.player.getVelocity().add(0, -0.04, 0));
            } else ticks = 0;
            if (client.player.fallDistance > 2.5f && !ModuleManager.noFall.isEnabled().get()) {
                if (client.player.isFallFlying())
                    return;
                client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
            }
        }
    }
}
