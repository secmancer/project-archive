package io.github.quantamancer.owf.entity.player_sled;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class EntityPlayerSledModel extends EntityModel<EntityPlayerSled> {

    private final ModelPart feet;
    private final ModelPart base;
    private final ModelPart railing;
    private final ModelPart stands;

    public EntityPlayerSledModel() {
        //Texture Size: 128, 128
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelTransform feetPivot = ModelTransform.pivot(0.0F, 24.0F, 13.0F);
        modelPartData.addChild("feet1", ModelPartBuilder.create().uv(0, 51).cuboid(7.0F, -1.0F, -28.0F, 1.0F, 1.0F, 33.0F) , feetPivot);
        modelPartData.addChild("feet2", ModelPartBuilder.create().uv(15, 11).cuboid(7.0F, -2.0F, 5.0F, 1.0F, 1.0F, 1.0F) , feetPivot);
        modelPartData.addChild("feet3", ModelPartBuilder.create().uv(8, 15).cuboid(7.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F) , feetPivot);
        modelPartData.addChild("feet4", ModelPartBuilder.create().uv(5, 5).cuboid(-8.0F, -4.0F, 5.0F, 1.0F, 1.0F, 3.0F) , feetPivot);
        modelPartData.addChild("feet5", ModelPartBuilder.create().uv(43, 0).cuboid(-8.0F, -1.0F, -27.0F, 1.0F, 1.0F, 32.0F) , feetPivot);
        modelPartData.addChild("feet6", ModelPartBuilder.create().uv(4, 15).cuboid(-8.0F, -2.0F, 5.0F, 1.0F, 1.0F, 1.0F) , feetPivot);
        modelPartData.addChild("feet7", ModelPartBuilder.create().uv(12, 10).cuboid(-8.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F) , feetPivot);
        modelPartData.addChild("feet8", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -5.0F, 7.0F, 16.0F, 1.0F, 1.0F) , feetPivot);
        modelPartData.addChild("feet9", ModelPartBuilder.create().uv(0, 4).cuboid(7.0F, -4.0F, 5.0F, 1.0F, 1.0F, 3.0F) , feetPivot);

        feet = modelPartData.createPart(128, 128);
        modelData = new ModelData();
        modelPartData = modelData.getRoot();

        ModelTransform basePivot = ModelTransform.pivot(0.0F, 24.0F, 0.0F);
        modelPartData.addChild("bottom",ModelPartBuilder.create().uv(0, 0).cuboid(-9.0F, -4.0F, -14.0F, 18.0F, 1.0F, 32.0F), basePivot);

        base = modelPartData.createPart(128, 128);
        modelData = new ModelData();
        modelPartData = modelData.getRoot();

        ModelTransform railingPivot = ModelTransform.pivot(0.0F, 24.0F, -28.0F);
        modelPartData.addChild("railing1",ModelPartBuilder.create().uv(27, 27).cuboid(7.0F, -7.0F, 14.0F, 1.0F, 1.0F, 25.0F), railingPivot);
        modelPartData.addChild("railing2",ModelPartBuilder.create().uv(0, 25).cuboid(-8.0F, -7.0F, 14.0F, 1.0F, 1.0F, 25.0F), railingPivot);
        modelPartData.addChild("railing3",ModelPartBuilder.create().uv(0, 2).cuboid(-7.0F, -7.0F, 14.0F, 14.0F, 1.0F, 1.0F), railingPivot);

        railing = modelPartData.createPart(128, 128);
        modelData = new ModelData();
        modelPartData = modelData.getRoot();

        ModelTransform standsPivot = ModelTransform.pivot(-3.0F, 24.0F, 25.0F);
        modelPartData.addChild("stands1",ModelPartBuilder.create().uv(12, 12).cuboid(10.0F, -6.0F, -22.0F, 1.0F, 5.0F, 1.0F), standsPivot);
        modelPartData.addChild("stands2",ModelPartBuilder.create().uv(0, 8).cuboid(10.0F, -6.0F, -16.0F, 1.0F, 5.0F, 1.0F), standsPivot);
        modelPartData.addChild("stands3",ModelPartBuilder.create().uv(8, 9).cuboid(-5.0F, -6.0F, -22.0F, 1.0F, 5.0F, 1.0F), standsPivot);
        modelPartData.addChild("stands4",ModelPartBuilder.create().uv(4, 9).cuboid(-5.0F, -6.0F, -16.0F, 1.0F, 5.0F, 1.0F), standsPivot);
        modelPartData.addChild("stands5",ModelPartBuilder.create().uv(0, 14).cuboid(-5.0F, -6.0F, -37.0F, 1.0F, 5.0F, 1.0F), standsPivot);
        modelPartData.addChild("stands6",ModelPartBuilder.create().uv(13, 4).cuboid(10.0F, -6.0F, -37.0F, 1.0F, 5.0F, 1.0F), standsPivot);

        stands = modelPartData.createPart(128, 128);
    }

    @Override
    public void setAngles(EntityPlayerSled entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
      this.feet.yaw = 3.1416F;
      this.base.yaw = 3.1416F;
      this.railing.yaw = 3.1416F;
      this.stands.yaw = 3.1416F;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer	buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        feet.render(matrixStack, buffer, packedLight, packedOverlay);
        base.render(matrixStack, buffer, packedLight, packedOverlay);
        railing.render(matrixStack, buffer, packedLight, packedOverlay);
        stands.render(matrixStack, buffer, packedLight, packedOverlay);
    }

}
