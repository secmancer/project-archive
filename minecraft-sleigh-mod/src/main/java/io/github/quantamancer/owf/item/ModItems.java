package io.github.quantamancer.owf.item;

import io.github.quantamancer.owf.entity.player_sled.EntityPlayerSled;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final ItemPlayerSledSpawner OAK_SLED_SPAWNER = new ItemPlayerSledSpawner(EntityPlayerSled.SledType.OAK, new FabricItemSettings().group(ItemGroup.TRANSPORTATION));
    public static final ItemPlayerSledSpawner SPRUCE_SLED_SPAWNER = new ItemPlayerSledSpawner(EntityPlayerSled.SledType.SPRUCE, new FabricItemSettings().group(ItemGroup.TRANSPORTATION));
    public static final ItemPlayerSledSpawner BIRCH_SLED_SPAWNER = new ItemPlayerSledSpawner(EntityPlayerSled.SledType.BIRCH, new FabricItemSettings().group(ItemGroup.TRANSPORTATION));
    public static final ItemPlayerSledSpawner JUNGLE_SLED_SPAWNER = new ItemPlayerSledSpawner(EntityPlayerSled.SledType.JUNGLE, new FabricItemSettings().group(ItemGroup.TRANSPORTATION));
    public static final ItemPlayerSledSpawner ACACIA_SLED_SPAWNER = new ItemPlayerSledSpawner(EntityPlayerSled.SledType.ACACIA, new FabricItemSettings().group(ItemGroup.TRANSPORTATION));
    public static final ItemPlayerSledSpawner DARK_OAK_SLED_SPAWNER = new ItemPlayerSledSpawner(EntityPlayerSled.SledType.DARK_OAK, new FabricItemSettings().group(ItemGroup.TRANSPORTATION));

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier("owf", "oak_sled"), OAK_SLED_SPAWNER);
        Registry.register(Registry.ITEM, new Identifier("owf", "spruce_sled"), SPRUCE_SLED_SPAWNER);
        Registry.register(Registry.ITEM, new Identifier("owf", "birch_sled"), BIRCH_SLED_SPAWNER);
        Registry.register(Registry.ITEM, new Identifier("owf", "jungle_sled"), JUNGLE_SLED_SPAWNER);
        Registry.register(Registry.ITEM, new Identifier("owf", "acacia_sled"), ACACIA_SLED_SPAWNER);
        Registry.register(Registry.ITEM, new Identifier("owf", "dark_oak_sled"), DARK_OAK_SLED_SPAWNER);
    }

}
