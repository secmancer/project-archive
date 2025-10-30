package io.github.quantamancer.owf;

import io.github.quantamancer.owf.entity.ModEntities;
import io.github.quantamancer.owf.item.ModItems;
import net.fabricmc.api.ModInitializer;

public class OhWhatFunItIs implements ModInitializer {

    @Override
    public void onInitialize() {
        ModItems.register();
        ModEntities.register();
    }

}
