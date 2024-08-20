package net.michaeljackson23.mineademia.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Unique;

public class AnimatedBipedModel<T extends LivingEntity> extends BipedEntityModel<T> {
    private final ModelPart root;
    private final ModelPart jointed_left_leg;
    private final ModelPart jointed_left_upper_leg;
    private final ModelPart jointed_left_lower_leg;
    private final ModelPart jointed_right_leg;
    private final ModelPart jointed_right_upper_leg;
    private final ModelPart jointed_right_lower_leg;
    private final ModelPart jointed_left_arm;
    private final ModelPart jointed_left_upper_arm;
    private final ModelPart jointed_left_fore_arm;
    private final ModelPart jointed_right_arm;
    private final ModelPart jointed_right_upper_arm;
    private final ModelPart jointed_right_fore_arm;

    private final static float palmSize = 0.001f;
    private final static float yPalmPos = 7.0001f;
    private final static float footSize = 0.001f;
    private final static float yFootPos = 7.0001f;
    private final static float elbowSize = 0.001f;
    private final static float kneeSize = 0.001f;

    public AnimatedBipedModel(ModelPart root, boolean thinArms) {
        super(root, RenderLayer::getEntityTranslucent);
        this.root = root.getChild("root");
        this.jointed_left_leg = root.getChild("jointed_left_leg");
        this.jointed_left_upper_leg = root.getChild("jointed_left_upper_leg");
        this.jointed_left_lower_leg = root.getChild("jointed_left_lower_leg");
        this.jointed_right_leg = root.getChild("jointed_right_leg");
        this.jointed_right_upper_leg = root.getChild("jointed_right_upper_leg");
        this.jointed_right_lower_leg = root.getChild("jointed_right_lower_leg");
        this.jointed_left_arm = root.getChild("jointed_left_arm");
        this.jointed_left_upper_arm = root.getChild("jointed_left_upper_arm");
        this.jointed_left_fore_arm = root.getChild("jointed_left_fore_arm");
        this.jointed_right_arm = root.getChild("jointed_right_arm");
        this.jointed_right_upper_arm = root.getChild("jointed_right_upper_arm");
        this.jointed_right_fore_arm = root.getChild("jointed_right_fore_arm");
    }

    public static TexturedModelData getTexturedModelData(Dilation dilation, boolean slim) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        if(slim)
            addSlimArms(root);
        else
            addWideArms(root);

        ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

        ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

        ModelPartData jointed_left_leg = root.addChild("jointed_left_leg", ModelPartBuilder.create(), ModelTransform.pivot(1.9F, -12.0F, 0.0F));

        ModelPartData jointed_left_upper_leg = jointed_left_leg.addChild("jointed_left_upper_leg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 56).cuboid(-2.0F, 6.01F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData jointed_left_lower_leg = jointed_left_leg.addChild("jointed_left_lower_leg", ModelPartBuilder.create().uv(16, 54).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 56).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 48).cuboid(-2.0F, 7.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 5.0F, 0.0F));

        ModelPartData jointed_right_leg = root.addChild("jointed_right_leg", ModelPartBuilder.create(), ModelTransform.pivot(-1.9F, -12.0F, 0.0F));

        ModelPartData jointed_right_upper_leg = jointed_right_leg.addChild("jointed_right_upper_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(-4, 24).cuboid(-2.0F, 6.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData jointed_right_lower_leg = jointed_right_leg.addChild("jointed_right_lower_leg", ModelPartBuilder.create().uv(0, 22).cuboid(-2.0F, 0.99F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(-4, 24).cuboid(-2.0F, 6.99F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 16).cuboid(-2.0F, 7.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 5.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    private static void addWideArms(ModelPartData modelPartData) {
        ModelPartData jointed_right_arm = modelPartData.addChild("jointed_right_arm", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        ModelPartData jointed_right_upper_arm = jointed_right_arm.addChild("jointed_right_upper_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-8.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(36, 24).cuboid(-8.0F, 4.01F, -2.0F, 4.0F, elbowSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 0.0F, 0.0F));

        ModelPartData jointed_right_fore_arm = jointed_right_arm.addChild("jointed_right_fore_arm", ModelPartBuilder.create().uv(40, 22).cuboid(-3.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(36, 24).cuboid(-3.0F, 1.0F, -2.0F, 4.0F, elbowSize, 4.0F, new Dilation(0.0F))
                .uv(40, 16).cuboid(-3.0F, yPalmPos, -2.0F, 4.0F, palmSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData jointed_left_arm = modelPartData.addChild("jointed_left_arm", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        ModelPartData jointed_left_upper_arm = jointed_left_arm.addChild("jointed_left_upper_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(28, 56).cuboid(-1.0F, 4.01F, -2.0F, 4.0F, elbowSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData jointed_left_fore_arm = jointed_left_arm.addChild("jointed_left_fore_arm", ModelPartBuilder.create().uv(32, 54).cuboid(-1.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(28, 56).cuboid(-1.0F, 1.0F, -2.0F, 4.0F, elbowSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData cube_r1 = jointed_left_fore_arm.addChild("cube_r1", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, yPalmPos, -2.0F, 4.0F, palmSize, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
    }

    private static void addSlimArms(ModelPartData modelPartData) {
        ModelPartData jointed_right_arm = modelPartData.addChild("jointed_right_arm", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        ModelPartData jointed_right_upper_arm = jointed_right_arm.addChild("jointed_right_upper_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-7.0F, -2.0F, -2.0F, 3.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(36, 24).cuboid(-7.0F, 4.01F, -2.0F, 3.0F, elbowSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 0.0F, 0.0F));

        ModelPartData jointed_right_fore_arm = jointed_right_arm.addChild("jointed_right_fore_arm", ModelPartBuilder.create().uv(40, 22).cuboid(-2.0F, 1.0F, -2.0F, 3.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(36, 24).cuboid(-2.0F, 1.0F, -2.0F, 3.0F, elbowSize, 4.0F, new Dilation(0.0F))
                .uv(40, 16).cuboid(-2.0F, yPalmPos, -2.0F, 3.0F, palmSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData jointed_left_arm = modelPartData.addChild("jointed_left_arm", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        ModelPartData jointed_left_upper_arm = jointed_left_arm.addChild("jointed_left_upper_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(28, 56).cuboid(-1.0F, 4.01F, -2.0F, 3.0F, elbowSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData jointed_left_fore_arm = jointed_left_arm.addChild("jointed_left_fore_arm", ModelPartBuilder.create().uv(32, 54).cuboid(-1.0F, 1.0F, -2.0F, 3.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(28, 56).cuboid(-1.0F, 1.0F, -2.0F, 3.0F, elbowSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData cube_r1 = jointed_left_fore_arm.addChild("cube_r1", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, yPalmPos, -2.0F, 3.0F, palmSize, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
    }
}
