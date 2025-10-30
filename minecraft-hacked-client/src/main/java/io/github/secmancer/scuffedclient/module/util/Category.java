package io.github.secmancer.scuffedclient.module.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.lukflug.panelstudio.setting.ICategory;
import com.lukflug.panelstudio.setting.IClient;
import com.lukflug.panelstudio.setting.IModule;

public enum Category implements ICategory {
	COMBAT("Combat"),
	EXPLOITS("Exploits"),
	MISC("Misc"),
	MOVEMENT("Movement"),
	GUI("GUI"),
	RENDER("Render"),
	WORLD("World");

	public final String displayName;
	public final List<Module> modules=new ArrayList<Module>();
	
	private Category (String displayName) {
		this.displayName=displayName;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public Stream<IModule> getModules() {
		return modules.stream().map(module->module);
	}
	
	public static IClient getClient() {
		return new IClient() {
			@Override
			public Stream<ICategory> getCategories() {
				return Arrays.stream(Category.values());
			}
		};
	}
}
