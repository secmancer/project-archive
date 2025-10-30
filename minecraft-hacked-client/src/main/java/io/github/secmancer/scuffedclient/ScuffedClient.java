package io.github.secmancer.scuffedclient;

import io.github.secmancer.scuffedclient.module.util.ModuleManager;
import net.fabricmc.api.ModInitializer;

public class ScuffedClient implements ModInitializer  {
    @Override
    public void onInitialize() {
        ModuleManager.init();
    }
}
