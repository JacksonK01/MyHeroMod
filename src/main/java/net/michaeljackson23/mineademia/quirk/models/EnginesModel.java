// Made with Blockbench 4.10.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package net.michaeljackson23.mineademia.quirk.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class EnginesModel<T extends LivingEntity> extends BipedEntityModel<T> {
    private final ModelPart hat;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;
    private final ModelPart right_engines;
    private final ModelPart left_engines;
    private final ModelPart left_engines_fire;
    private final ModelPart right_engines_fire;


    public EnginesModel(ModelPart root) {
        super(root);
        this.hat = root.getChild("hat");
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
        this.right_engines = root.getChild("right_engines");
        this.left_engines = root.getChild("left_engines");
        this.left_engines_fire = root.getChild("left_engines_fire");
        this.right_engines_fire = root.getChild("right_engines_fire");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData hat = modelPartData.addChild("hat", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(16, 32).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 12.0F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(24, 16).cuboid(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 12.0F, 0.0F));

        ModelPartData right_engines = modelPartData.addChild("right_engines", ModelPartBuilder.create(), ModelTransform.pivot(-2.0F, 12.0F, 0.0F));

        ModelPartData e6_r1 = right_engines.addChild("e6_r1", ModelPartBuilder.create().uv(28, 32).cuboid(0.25F, 6.0F, 0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(32, 37).cuboid(0.25F, 7.5F, 0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(38, 34).cuboid(0.25F, 9.0F, 0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 1.0F, 0.0F, 0.0873F, 0.0F));

        ModelPartData e5_r1 = right_engines.addChild("e5_r1", ModelPartBuilder.create().uv(37, 29).cuboid(-1.25F, 7.5F, 0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(33, 33).cuboid(-1.25F, 6.0F, 0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(36, 16).cuboid(-1.25F, 9.0F, 0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 1.0F, 0.0F, -0.0873F, 0.0F));

        ModelPartData left_engines = modelPartData.addChild("left_engines", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 12.0F, 0.0F));

        ModelPartData e6_r2 = left_engines.addChild("e6_r2", ModelPartBuilder.create().uv(0, 4).cuboid(-1.25F, 6.0F, 0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(24, 0).cuboid(-1.25F, 7.5F, 0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(24, 4).cuboid(-1.25F, 9.0F, 0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 1.0F, 0.0F, -0.0873F, 0.0F));

        ModelPartData e5_r2 = left_engines.addChild("e5_r2", ModelPartBuilder.create().uv(20, 16).cuboid(0.25F, 7.5F, 0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(12, 32).cuboid(0.25F, 9.0F, 0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(0.25F, 6.0F, 0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 1.0F, 0.0F, 0.0873F, 0.0F));

        ModelPartData left_engines_fire = modelPartData.addChild("left_engines_fire", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 12.0F, 0.0F));

        ModelPartData fire12B_r1 = left_engines_fire.addChild("fire12B_r1", ModelPartBuilder.create().uv(0, 51).cuboid(-4.2577F, 2.9586F, 2.6065F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.4577F, 2.8414F, 0.8935F, 0.0774F, -0.0403F, -0.7854F));

        ModelPartData fire12A_r1 = left_engines_fire.addChild("fire12A_r1", ModelPartBuilder.create().uv(0, 51).cuboid(2.3423F, 3.9586F, 2.6065F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.4577F, 2.8414F, 0.8935F, -0.0175F, -0.0349F, 0.7854F));

        ModelPartData fire11B_r1 = left_engines_fire.addChild("fire11B_r1", ModelPartBuilder.create().uv(0, 51).cuboid(-3.8577F, 2.6586F, 2.6065F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.4577F, 1.8414F, 0.8935F, 0.0774F, -0.0403F, -0.7854F));

        ModelPartData fire11A_r1 = left_engines_fire.addChild("fire11A_r1", ModelPartBuilder.create().uv(0, 51).cuboid(1.9423F, 3.3586F, 2.6065F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.4577F, 1.8414F, 0.8935F, -0.0175F, -0.0349F, 0.7854F));

        ModelPartData fire10B_r1 = left_engines_fire.addChild("fire10B_r1", ModelPartBuilder.create().uv(0, 51).cuboid(-4.2F, 2.8586F, 2.6065F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.4577F, -0.1586F, 0.8935F, 0.0774F, -0.0403F, -0.7854F));

        ModelPartData fire10A_r1 = left_engines_fire.addChild("fire10A_r1", ModelPartBuilder.create().uv(0, 51).cuboid(2.2423F, 3.8264F, 2.6065F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.4577F, -0.1586F, 0.8935F, -0.0175F, -0.0349F, 0.7854F));

        ModelPartData fire9B_r1 = left_engines_fire.addChild("fire9B_r1", ModelPartBuilder.create().uv(0, 51).cuboid(-6.4F, 4.8F, 3.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 51).cuboid(-5.2F, 3.8F, 3.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 51).cuboid(-4.2F, 2.7F, 3.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0774F, -0.0403F, -0.7854F));

        ModelPartData fire9A_r1 = left_engines_fire.addChild("fire9A_r1", ModelPartBuilder.create().uv(0, 51).cuboid(4.1185F, 6.0F, 3.6258F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 51).cuboid(3.1185F, 4.8264F, 3.6258F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 51).cuboid(2.1185F, 3.8264F, 3.6258F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0175F, -0.0349F, 0.7854F));

        ModelPartData right_engines_fire = modelPartData.addChild("right_engines_fire", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 12.0F, 0.0F));

        ModelPartData fire6A_r1 = right_engines_fire.addChild("fire6A_r1", ModelPartBuilder.create().uv(0, 51).cuboid(-4.2577F, 2.9586F, 2.6065F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.5423F, 2.8414F, 0.8935F, 0.0774F, -0.0403F, -0.7854F));

        ModelPartData fire6A_r2 = right_engines_fire.addChild("fire6A_r2", ModelPartBuilder.create().uv(0, 51).cuboid(2.3423F, 3.9586F, 2.6065F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.5423F, 2.8414F, 0.8935F, -0.0175F, -0.0349F, 0.7854F));

        ModelPartData fire5B_r1 = right_engines_fire.addChild("fire5B_r1", ModelPartBuilder.create().uv(0, 51).cuboid(-3.8577F, 2.6586F, 2.6065F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.5423F, 1.8414F, 0.8935F, 0.0774F, -0.0403F, -0.7854F));

        ModelPartData fire5A_r1 = right_engines_fire.addChild("fire5A_r1", ModelPartBuilder.create().uv(0, 51).cuboid(1.9423F, 3.3586F, 2.6065F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.5423F, 1.8414F, 0.8935F, -0.0175F, -0.0349F, 0.7854F));

        ModelPartData fire4B_r1 = right_engines_fire.addChild("fire4B_r1", ModelPartBuilder.create().uv(0, 51).cuboid(-4.2F, 2.8586F, 2.6065F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.5423F, -0.1586F, 0.8935F, 0.0774F, -0.0403F, -0.7854F));

        ModelPartData fire4A_r1 = right_engines_fire.addChild("fire4A_r1", ModelPartBuilder.create().uv(0, 51).cuboid(2.2423F, 3.8264F, 2.6065F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.5423F, -0.1586F, 0.8935F, -0.0175F, -0.0349F, 0.7854F));

        ModelPartData fire3B_r1 = right_engines_fire.addChild("fire3B_r1", ModelPartBuilder.create().uv(0, 51).cuboid(-6.4F, 4.8F, 3.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 51).cuboid(-5.2F, 3.8F, 3.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 51).cuboid(-4.2F, 2.7F, 3.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, 0.0F, 0.0F, 0.0774F, -0.0403F, -0.7854F));

        ModelPartData fire3A_r1 = right_engines_fire.addChild("fire3A_r1", ModelPartBuilder.create().uv(0, 51).cuboid(4.1185F, 6.0F, 3.6258F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 51).cuboid(3.1185F, 4.8264F, 3.6258F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 51).cuboid(2.1185F, 3.8264F, 3.6258F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, 0.0F, 0.0F, -0.0175F, -0.0349F, 0.7854F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public void setEnginesVisible(boolean visible) {
        this.left_engines.visible = visible;
        this.right_engines.visible = visible;
    }

    public void setEnginesFireVisible(boolean visible) {
        this.left_engines_fire.visible = visible;
        this.right_engines_fire.visible = visible;
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        hat.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        right_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        left_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        right_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        left_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        right_engines.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        left_engines.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        left_engines_fire.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        right_engines_fire.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}