package io.github.secmancer.scuffedclient.module.gui;

import io.github.secmancer.scuffedclient.gui.setting.BooleanSetting;
import io.github.secmancer.scuffedclient.gui.setting.KeybindSetting;
import io.github.secmancer.scuffedclient.module.util.Module;
import org.lwjgl.glfw.GLFW;

public class HUDEditorModule extends Module {
	public static final BooleanSetting showHUD=new BooleanSetting("Show HUD Panels","showHUD","Whether to show the HUD panels in the ClickGUI.",()->true,true);
	public static final KeybindSetting keybind=new KeybindSetting("Keybind","keybind","The key to toggle the module.",()->true,GLFW.GLFW_KEY_RIGHT_BRACKET);
	
	public HUDEditorModule() {
		super("HUDEditor","Module containing HUDEditor settings.",()->true,true);
		settings.add(showHUD);
		settings.add(keybind);
	}
}
