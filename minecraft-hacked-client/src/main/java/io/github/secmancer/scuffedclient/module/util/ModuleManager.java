package io.github.secmancer.scuffedclient.module.util;

import io.github.secmancer.scuffedclient.gui.ClickGUI;
import io.github.secmancer.scuffedclient.module.misc.AutoEat;
import io.github.secmancer.scuffedclient.module.misc.AutoFish;
import io.github.secmancer.scuffedclient.module.movement.Sprint;
import io.github.secmancer.scuffedclient.module.movement.BoatFly;
import io.github.secmancer.scuffedclient.module.gui.*;
import io.github.secmancer.scuffedclient.module.movement.NoFall;
import io.github.secmancer.scuffedclient.module.render.Fullbright;
import io.github.secmancer.scuffedclient.module.render.Xray;
import io.github.secmancer.scuffedclient.util.config.LoadClientConfig;
import io.github.secmancer.scuffedclient.util.config.SaveClientConfig;
import io.github.secmancer.scuffedclient.util.log.ModLogger;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedHashMap;

public class ModuleManager
{
    public static ClickGUI gui;
    private static boolean inited = false;
    private static final boolean[] keys = new boolean[266];

    public static final LinkedHashMap<Class<? extends Module>, Module> modules = new LinkedHashMap<>();
    public static final AutoFish autoFish = new AutoFish();
    public static final BoatFly flying = new BoatFly();
    public static final NoFall noFall = new NoFall();
    public static final Sprint constantSprint = new Sprint();
    public static final AutoEat autoEat = new AutoEat();
    public static final Xray xray = new Xray();
    public static final Fullbright fullbright = new Fullbright();

    public static void init() {
        ModLogger.log("Starting the client...");
        ModLogger.log("Registering and assigning modules to their category...");
        addModule(autoFish, Category.MISC);
        addModule(autoEat, Category.MISC);
        addModule(flying, Category.MOVEMENT);
        addModule(noFall, Category.MOVEMENT);
        addModule(constantSprint, Category.MOVEMENT);
        addModule(xray, Category.RENDER);
        addModule(fullbright, Category.RENDER);
        addModule(new ClickGUIModule(), Category.GUI);
        addModule(new HUDEditorModule(), Category.GUI);
        addModule(new TabGUIModule(), Category.GUI);
        addModule(new WatermarkModule(), Category.GUI);
        addModule(new StatsModule(), Category.GUI);
        ModLogger.log("Modules have been loaded and assigned.");
        ModLogger.log("Registering tick events...");
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!inited) {
                for (int i=32;i<keys.length;i++) keys[i]= GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(),i)==GLFW.GLFW_PRESS;
                gui=new ClickGUI();
                HudRenderCallback.EVENT.register((cli, tickDelta)->gui.render());
                LoadClientConfig.init();
                inited=true;
            }
            for (int i=32;i<keys.length;i++) {
                if (keys[i]!=(GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(),i)==GLFW.GLFW_PRESS)) {
                    keys[i]=!keys[i];
                    if (keys[i]) {
                        if (i== ClickGUIModule.keybind.getKey()) gui.enterGUI();
                        if (i== HUDEditorModule.keybind.getKey()) gui.enterHUDEditor();
                        gui.handleKeyEvent(i);
                        SaveClientConfig.init();
                    }
                }
            }
        });
        ModLogger.log("Tick events have been registered.");
        ModLogger.log("Client successfully loaded! Enjoy your stay. :)");
    }

    public static void addModule(Module module, Category category) {
        modules.put(module.getClass(), module);
        category.modules.add(module);
    }

    public static void setAutoFishTimer(int time) {
        AutoFish.autoFishTimer = time;
    }
}
