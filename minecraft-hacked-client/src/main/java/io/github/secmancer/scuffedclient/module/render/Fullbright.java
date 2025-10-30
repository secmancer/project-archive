package io.github.secmancer.scuffedclient.module.render;

import com.lukflug.panelstudio.base.IToggleable;
import io.github.secmancer.scuffedclient.module.util.Module;
import io.github.secmancer.scuffedclient.util.option.SimpleForceValueOption;
import net.minecraft.client.MinecraftClient;

public class Fullbright extends Module {
    private double oldGamma;
    public Fullbright() {
        super("Fullbright", "Lights up the entire world for the player to see", () -> true, true);
    }

    public IToggleable getToggle() { return this.isEnabled(); }

    @Override
    public void onEnable(MinecraftClient client) {
        if (client.player != null) {
            oldGamma = client.options.getGamma().getValue();
            @SuppressWarnings("unchecked")
            SimpleForceValueOption<Double> gamma =
                    (SimpleForceValueOption<Double>)(Object)client.options.getGamma();
            assert gamma != null;
            gamma.scuffedclient$forceSetValue(10000.0);
        }
    }

    @Override
    public void onDisable(MinecraftClient client) {
        if (client.player != null) {
            @SuppressWarnings("unchecked")
            SimpleForceValueOption<Double> gamma =
                    (SimpleForceValueOption<Double>)(Object)client.options.getGamma();
            assert gamma != null;
            gamma.scuffedclient$forceSetValue(oldGamma);
        }
    }
}
