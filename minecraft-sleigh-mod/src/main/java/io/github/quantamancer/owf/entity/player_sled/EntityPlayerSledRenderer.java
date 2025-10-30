package io.github.quantamancer.owf.entity.player_sled;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.stream.Stream;

public class EntityPlayerSledRenderer extends MobEntityRenderer<EntityPlayerSled, EntityPlayerSledModel> {
    private final Map<EntityPlayerSled.SledType, Pair<Identifier, EntityPlayerSledModel>> texturesAndModels;

    public EntityPlayerSledRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new EntityPlayerSledModel(), 1.0f);
        this.texturesAndModels = (Map)Stream.of(EntityPlayerSled.SledType.values()).collect(ImmutableMap.toImmutableMap((type) -> {
            return type;
        }, (type) -> {
            return Pair.of(new Identifier("owf:textures/entity/sled/" + type.getName() + ".png"), new EntityPlayerSledModel());
        }));
    }

    @Override
    public Identifier getTexture(EntityPlayerSled entity) {
        return (Identifier) ((Pair)this.texturesAndModels.get(entity.getSledType())).getFirst();
    }
}
