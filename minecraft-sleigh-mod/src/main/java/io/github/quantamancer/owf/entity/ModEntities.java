package io.github.quantamancer.owf.entity;

import io.github.quantamancer.owf.entity.player_sled.EntityPlayerSled;
import io.github.quantamancer.owf.entity.player_sled.EntityPlayerSledRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {

    public static final EntityType<EntityPlayerSled> SLED = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("owf", "sled"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, EntityPlayerSled::new).dimensions(new EntityDimensions(1F, 0.75F, true)).build()
    );

    @Environment(EnvType.CLIENT)
    public static void register() {
        FabricDefaultAttributeRegistry.register(SLED, EntityPlayerSled.createBaseHorseAttributes());
        EntityRendererRegistry.INSTANCE.register(SLED, EntityPlayerSledRenderer::new);
    }

}
