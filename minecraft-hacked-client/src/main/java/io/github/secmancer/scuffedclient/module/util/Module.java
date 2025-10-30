package io.github.secmancer.scuffedclient.module.util;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.setting.IModule;
import com.lukflug.panelstudio.setting.ISetting;
import io.github.secmancer.scuffedclient.gui.setting.Setting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Module implements IModule {
	public final String displayName,description;
	public final IBoolean visible;
	public final List<Setting<?>> settings=new ArrayList<Setting<?>>();
	public final boolean toggleable;
	private boolean enabled=false;
	private final MinecraftClient client = MinecraftClient.getInstance();
	
	public Module (String displayName, String description, IBoolean visible, boolean toggleable) {
		this.displayName=displayName;
		this.description=description;
		this.visible=visible;
		this.toggleable=toggleable;
	}

	public void onEnable(MinecraftClient client) {}

	public void onDisable(MinecraftClient client) {}

	public void enable(MinecraftClient client) {
		enabled = true;
		ClientTickEvents.START_CLIENT_TICK.register(this::tick);
		onEnable(client);
	}

	public void disable(MinecraftClient client) {
		enabled = false;
		onDisable(client);
	}

	public void tick(MinecraftClient client) {}
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public IBoolean isVisible() {
		return visible;
	}

	@Override
	public IToggleable isEnabled() {
		if (!toggleable) return null;
		return new IToggleable() {
			@Override
			public boolean isOn() {
				return enabled;
			}

			@Override
			public void toggle() {
				enabled=!enabled;

				if (enabled) {
					enable(client);
				} else {
					disable(client);
				}
			}
		};
	}

	@Override
	public Stream<ISetting<?>> getSettings() {
		return settings.stream().filter(setting->setting instanceof ISetting).sorted(Comparator.comparing(a -> a.displayName)).map(setting->(ISetting<?>)setting);
	}
}
