package io.github.secmancer.scuffedclient.module.misc;

import com.lukflug.panelstudio.base.IToggleable;
import io.github.secmancer.scuffedclient.module.util.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class AutoEat extends Module {

    public AutoEat() {
        super("AutoEat", "Allows the player to automatically eat food in your inventory.", () -> true, true);
    }

    public IToggleable getToggle() { return this.isEnabled(); }

    @Override
    public void tick(MinecraftClient client) {
        if (isEnabled().get() && client.player != null) {
            int hunger = 6;
            if (client.player.getHungerManager().getFoodLevel() <= hunger) {
                int slot = -1;
                FoodComponent food = null;
                for (int i = 0; i < 9; i++) {
                    Item item = client.player.getInventory().getStack(i).getItem();

                    if (!item.isFood()) { continue; }

                    FoodComponent currFood = item.getFoodComponent();

                    if (food != null) {
                        assert currFood != null;
                        if (currFood.getHunger() > food.getHunger()) {
                           food = currFood;
                           slot = i;
                       }
                    } else {
                          food = currFood;
                          slot = i;
                    }
                }

                if (food != null) {
                    client.player.getInventory().selectedSlot = slot;
                    client.options.useKey.setPressed(true);
                }
            }
        }
    }
}
